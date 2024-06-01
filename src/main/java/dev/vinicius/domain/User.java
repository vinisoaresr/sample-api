package dev.vinicius.domain;

import lombok.Getter;

@Getter
public class User {
    private final String userId;

    private final String name;

    private final String document;

    private final String email;

    private final String password;

    private final UserType type;

    public User(String userId, String name, String email, String document, String password, String type) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.document = document;
        this.password = password;
        this.type = UserType.valueOf(type.toUpperCase());
    }

    public static User create(String name, String email, String document, String password, String type) {
        var uuid = java.util.UUID.randomUUID().toString();
        return new User(uuid, name, email, document, password, type);
    }
}