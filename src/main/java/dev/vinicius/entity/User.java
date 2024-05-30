package dev.vinicius.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String userId;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String document;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserType type;

    public User() {
    }

    public User(String userId, String name, String email, String document, String password, String type) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.document = document;
        this.password = password;
        this.type = UserType.valueOf(type.toUpperCase());
    }

    public static User create(String name, String email, String document, String password, String type) {
        return new User(null, name, email, document, password, type);
    }
}