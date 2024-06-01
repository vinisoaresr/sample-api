package dev.vinicius.application;

import dev.vinicius.application.database.UserRepository;
import dev.vinicius.application.usecase.CreateUser;
import dev.vinicius.application.usecase.GetUser;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class GetUserTest {

    @Inject
    GetUser getUser;

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
    void shouldBeGetUser() {
        String userId = makeNewUser();
        var user = getUser.execute(userId);

        Assertions.assertNotNull(user);
        Assertions.assertEquals(userId, user.userId());
        Assertions.assertEquals("user", user.userType());
    }

    @Test
    void shouldBeReturnNullWhenUserNotFound() {
        var user = getUser.execute("any-id");
        Assertions.assertNull(user);
    }

    private String makeNewUser() {
        var input = new CreateUser.Input(
                "Vinicius",
                "vini@teste.com",
                "12345678901",
                "user",
                "123456");

        var output = createUser.execute(input);

        return output.userId();
    }
}


