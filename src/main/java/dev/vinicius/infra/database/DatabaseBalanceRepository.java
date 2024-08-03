package dev.vinicius.infra.database;

import dev.vinicius.application.database.BalanceRepository;
import dev.vinicius.domain.Balance;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;

@ApplicationScoped
public class DatabaseBalanceRepository implements BalanceRepository {

    @Inject
    DataSource dataSource;

    @Override
    public Balance findByUserId(String userId) {
        try {
            var connection = dataSource.getConnection();
            var statement = connection.prepareStatement("SELECT * FROM balance WHERE user_id = ? ORDER BY date DESC LIMIT 1");
            statement.setString(1, userId);
            var resultSet = statement.executeQuery();
            Balance balance = null;

            if (resultSet.next()) {
                balance = new Balance(
                        resultSet.getString("balance_id"),
                        resultSet.getString("user_id"),
                        resultSet.getDate("date"),
                        resultSet.getDouble("balance")
                );
            }
            return balance;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void save(Balance balance) {
        try {
            var connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            var statement = connection.prepareStatement("INSERT INTO balance (balance_id, balance, date, user_id) VALUES (?, ?, ?, ?)");
            statement.setString(1, balance.getBalanceId());
            statement.setDouble(2, balance.getAmount());
            statement.setDate(3, new java.sql.Date(balance.getDate().getTime()));
            statement.setString(4, balance.getUserId());
            statement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeAll() {
        try {
            var connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            var statement = connection.prepareStatement("DELETE FROM balance");
            statement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
