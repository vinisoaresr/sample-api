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
