package dev.vinicius.infra.database;

import dev.vinicius.application.database.TransferRepository;
import dev.vinicius.domain.Transfer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class DatabaseTransferRepository implements TransferRepository {

    @Inject
    DataSource dataSource;

    @Override
    public Transfer findTransferById(String transferId) {
        try {
            var connection = dataSource.getConnection();
            var select = "SELECT * FROM transfer WHERE transfer_id = ?";
            var statement = connection.prepareStatement(select);
            statement.setString(1, transferId);
            var resultSet = statement.executeQuery();
            Transfer transfer = null;
            if (resultSet.next()) {
                transfer = new Transfer(
                        resultSet.getString("transfer_id"),
                        resultSet.getString("payer_id"),
                        resultSet.getString("payee_id"),
                        resultSet.getDouble("amount"),
                        resultSet.getString("description"),
                        resultSet.getDate("date")
                );
            }

            return transfer;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public void save(Transfer transfer) {
        try {
            var connection = dataSource.getConnection();
            var insert = "INSERT INTO transfer (transfer_id, payer_id, payee_id, amount, description, date) VALUES (?, ?, ?, ?, ?, ?)";
            var statement = connection.prepareStatement(insert);
            statement.setString(1, transfer.getTransferId());
            statement.setString(2, transfer.getPayerId());
            statement.setString(3, transfer.getPayeeId());
            statement.setDouble(4, transfer.getAmount());
            statement.setString(5, transfer.getDescription());
            statement.setDate(6, new java.sql.Date(transfer.getDate().getTime()));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeAll() {
        try {
            var connection = dataSource.getConnection();
            var delete = "DELETE FROM transfer";
            var statement = connection.prepareStatement(delete);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Transfer> findAllTransfers() {
        try {
            var connection = dataSource.getConnection();
            var select = "SELECT * FROM transfer";
            var statement = connection.prepareStatement(select);
            var resultSet = statement.executeQuery();
            var transfers = new ArrayList<Transfer>();
            while (resultSet.next()) {
                transfers.add(new Transfer(
                        resultSet.getString("transfer_id"),
                        resultSet.getString("payer_id"),
                        resultSet.getString("payee_id"),
                        resultSet.getDouble("amount"),
                        resultSet.getString("description"),
                        resultSet.getDate("date")
                ));

            }
            return transfers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
