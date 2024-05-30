package dev.vinicius.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Getter
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String userId;
    @Column
    String name;
    @Column
    String email;
    @Column
    String password;
    @Column
    String document;

    public User() {
    }

    public User(String userId, String name, String email, String document, String password) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.document = document;
        this.password = password;
    }

    public static User create(String name, String email, String document, String password) {
        return new User(null, name, email, document, password);
    }
}