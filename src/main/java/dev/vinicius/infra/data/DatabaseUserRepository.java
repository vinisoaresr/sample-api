package dev.vinicius.infra.data;

import dev.vinicius.application.UserRepository;
import dev.vinicius.entity.User;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DatabaseUserRepository implements UserRepository, PanacheRepositoryBase<User,String> {

    @Override
    public void save(User user) {
        persist(user);
    }

    @Override
    public void removeAll() {
        deleteAll();
    }

    @Override
    public User findByDocument(String document) {
        return find("document", document).firstResult();
    }

    @Override
    public User findByEmail(String email) {
        return find("email", email).firstResult();
    }

    @Override
    public User findById(String userId) {
        return find("userId", userId).firstResult();
    }

}