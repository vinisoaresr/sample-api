package dev.vinicius.application;

import dev.vinicius.application.database.UserRepository;
import dev.vinicius.application.usecase.CreateUser;
import dev.vinicius.application.usecase.CreateUser.Input;
import dev.vinicius.application.usecase.CreateUser.Output;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dev.vinicius.domain.User;
import dev.vinicius.infra.exceptions.DocumentAlreadyExistsException;
import dev.vinicius.infra.exceptions.EmailAlreadyExistsException;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@QuarkusTest
class CreateUserTest {
    @Inject
    CreateUser createUser;
    @Inject
    UserRepository userRepository;

    @BeforeEach
    @Transactional
    void setUp() {
        userRepository.removeAll();
    }

    @Test
    void shouldBeCreateUser() {
        Input input = new Input(
                "Vinicius",
                "vini@teste.com",
                "12345678901",
                "user",
                "123456");

        Output output = createUser.execute(input);
        User user = userRepository.findByDocument(input.document());

        Assertions.assertNotNull(output.userId());
        Assertions.assertNotNull(user);
        Assertions.assertNotNull(user.getUserId());
        Assertions.assertEquals(input.name(), user.getName());
        Assertions.assertEquals(input.email(), user.getEmail());
        Assertions.assertEquals(input.document(), user.getDocument());
        Assertions.assertEquals(input.type(), user.getType().name().toLowerCase());
        Assertions.assertEquals(input.password(), user.getPassword());
    }

    @Test
    void shouldBeThrowDocumentAlreadyExistsException() {
        Input input = new Input(
                "Vinicius",
                "vini@teste.com",
                "same-document",
                "user",
                "123");

        Output output = createUser.execute(input);

        Assertions.assertNotNull(output.userId());

        Input input2 = new Input(
                "Carol",
                "carol@teste.com",
                "same-document",
                "user",
                "123");

        DocumentAlreadyExistsException exception = Assertions.assertThrows(
                DocumentAlreadyExistsException.class,
                () -> createUser.execute(input2));

        Assertions.assertEquals("Document already exists", exception.getMessage());
    }

    @Test
    void shouldBeThrowEmailAlreadyExistsException() {
        Input input = new Input(
                "Vinicius",
                "vini@teste.com",
                "same-document",
                "user",
                "123");

        Output output = createUser.execute(input);

        Assertions.assertNotNull(output.userId());

        Input input2 = new Input(
                "Carol",
                "vini@teste.com",
                "diff-document",
                "user",
                "123");

        EmailAlreadyExistsException exception = Assertions.assertThrows(
                EmailAlreadyExistsException.class,
                () -> createUser.execute(input2));

        Assertions.assertEquals("Email already exists", exception.getMessage());
    }

}

