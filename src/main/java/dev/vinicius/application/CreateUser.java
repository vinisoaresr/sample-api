package dev.vinicius.application;

import dev.vinicius.entity.User;
import dev.vinicius.infra.exceptions.DocumentAlreadyExistsException;
import dev.vinicius.infra.exceptions.EmailAlreadyExistsException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.Objects;

@ApplicationScoped
public class CreateUser {

    private final UserRepository userRepository;

    CreateUser(UserRepository userRepository) {
        this.userRepository = Objects.requireNonNull(userRepository);
    }

    @Transactional
    public Output execute(Input input) {
        User user = userRepository.findByDocument(input.document());

        if (user != null) {
            throw new DocumentAlreadyExistsException("Document already exists");
        }

        user = userRepository.findByEmail(input.email());

        if (user != null) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        user = User.create(
                input.name(),
                input.email(),
                input.document(),
                input.password());

        userRepository.save(user);
        return new Output(user.getUserId());
    }

    public record Input(String name, String email, String document, String password) {
    }

    public record Output(String userId) {
    }
}
