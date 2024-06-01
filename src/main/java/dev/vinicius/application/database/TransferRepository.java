package dev.vinicius.application.database;

import dev.vinicius.domain.Transfer;

import java.util.List;

public interface TransferRepository {
    Transfer findTransferById(String transferId);

    void save(Transfer transfer);

    void removeAll();

    List<Transfer> findAllTransfers();
}
