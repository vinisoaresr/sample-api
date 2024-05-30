package dev.vinicius.application;

import dev.vinicius.entity.User;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface UserRepository {
    void save(User user);

    void removeAll();

    User findByDocument(String document);

    User findByEmail(String email);
}