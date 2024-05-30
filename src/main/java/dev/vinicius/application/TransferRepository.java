package dev.vinicius.application;

import dev.vinicius.entity.Transfer;

import java.util.List;

public interface TransferRepository {
    Transfer findTransferById(String transferId);

    void save(Transfer transfer);

    void removeAll();

    List<Transfer> findAllTransfers();
}
