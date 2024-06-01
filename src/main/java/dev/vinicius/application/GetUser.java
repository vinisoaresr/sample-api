package dev.vinicius.application;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class GetUser {

    @Inject
    UserRepository userRepository;

    public Output execute(String userId) {
        var user = userRepository.findById(userId);

        if (user != null) {
            return new Output(
                    user.getUserId(),
                    user.getType().name().toLowerCase()
            );
        }

        return null;
    }

    public record Output(String userId, String userType) {
    }
}
