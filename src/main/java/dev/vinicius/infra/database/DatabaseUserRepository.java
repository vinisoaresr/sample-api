package dev.vinicius.infra.database;

import dev.vinicius.application.database.UserRepository;
import dev.vinicius.domain.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@ApplicationScoped
public class DatabaseUserRepository implements UserRepository {

    @Inject
    DataSource dataSource;

    @Override
    public void save(User user) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO \"user\" (user_id, name, email, document, password, type) VALUES (?, ?, ?, ?, ?, ?)");
            statement.setString(1, user.getUserId());
            statement.setString(2, user.getName());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getDocument());
            statement.setString(5, user.getPassword());
            statement.setString(6, user.getType().name());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void removeAll() {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM \"user\"");
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public User findByDocument(String document) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"user\" WHERE document = ?");
            statement.setString(1, document);
            var resultSet = statement.executeQuery();
            User user = null;
            if (resultSet.next()) {
                user = new User(
                        statement.getResultSet().getString("user_id"),
                        statement.getResultSet().getString("name"),
                        statement.getResultSet().getString("email"),
                        statement.getResultSet().getString("document"),
                        statement.getResultSet().getString("password"),
                        statement.getResultSet().getString("type")
                );
            }
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User findByEmail(String email) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"user\" WHERE email = ?");
            statement.setString(1, email);
            var resultSet = statement.executeQuery();
            User user = null;
            if (resultSet.next()) {
                user = new User(
                        statement.getResultSet().getString("user_id"),
                        statement.getResultSet().getString("name"),
                        statement.getResultSet().getString("email"),
                        statement.getResultSet().getString("document"),
                        statement.getResultSet().getString("password"),
                        statement.getResultSet().getString("type")
                );
            }
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public User findById(String userId) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"user\" WHERE user_id = ?");
            statement.setString(1, userId);
            var resultSet = statement.executeQuery();
            User user = null;
            if (resultSet.next()) {
                user = new User(
                        statement.getResultSet().getString("user_id"),
                        statement.getResultSet().getString("name"),
                        statement.getResultSet().getString("email"),
                        statement.getResultSet().getString("document"),
                        statement.getResultSet().getString("password"),
                        statement.getResultSet().getString("type")
                );
            }
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}