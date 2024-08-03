package dev.vinicius.application.usecase;

import dev.vinicius.application.database.BalanceRepository;
import dev.vinicius.domain.Balance;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.Objects;

@ApplicationScoped
public class CreateBalance {

    private final BalanceRepository balanceRepository;

    CreateBalance(BalanceRepository balanceRepository) {
        this.balanceRepository = Objects.requireNonNull(balanceRepository);
    }

    @Transactional
    public Output execute(Input input) {
        var balance = Balance.create(input.userId(), input.amount());
        balanceRepository.save(balance);
        return new Output(
                balance.getBalanceId(),
                balance.getUserId(),
                balance.getAmount()
        );
    }

    public record Input(
            String userId,
            Double amount
    ) {
    }

    public record Output(
            String balanceId,
            String userId,
            Double amount
    ) {
    }
}
