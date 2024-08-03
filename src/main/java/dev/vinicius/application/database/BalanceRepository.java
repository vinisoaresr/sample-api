package dev.vinicius.application.database;

import dev.vinicius.domain.Balance;

public interface BalanceRepository {
    Balance findByUserId(String userId);

    void save(Balance balance);

    void removeAll();
}
