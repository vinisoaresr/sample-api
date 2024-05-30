package dev.vinicius.application;

import dev.vinicius.entity.Transfer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class CreateTransfer {

    @Inject
    TransferRepository transferRepository;

    @Transactional
    public Output execute(Input input) {
        if (input.payerId() == null) {
            throw new IllegalArgumentException("payerId is required");
        }

        if (input.payeeId() == null) {
            throw new IllegalArgumentException("payeeId is required");
        }

        if (input.amount() <= 0) {
            throw new IllegalArgumentException("amount should be greater than 0");
        }

        var transfer = Transfer.create(input.payerId(), input.payeeId(), input.amount(), input.description());
        transferRepository.save(transfer);
        return new Output(transfer.getTransferId());
    }

    public record Input(
            String payerId,
            String payeeId,
            String description,
            double amount
    ) {
    }

    public record Output(String transferId) {
    }
}
