package dev.vinicius.application;

import dev.vinicius.domain.User;

public interface UserRepository {
    void save(User user);

    void removeAll();

    User findByDocument(String document);

    User findByEmail(String email);

    User findById(String userId);
}