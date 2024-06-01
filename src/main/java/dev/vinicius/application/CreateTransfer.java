package dev.vinicius.application;

import dev.vinicius.domain.Transfer;
import dev.vinicius.domain.UserType;
import dev.vinicius.infra.exceptions.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class CreateTransfer {

    @Inject
    GetUser getUser;

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
            throw new IllegalArgumentException("amount should be greater than zero");
        }

        var payerUser = getUser.execute(input.payerId());

        if (payerUser == null) {
            throw new UserNotFoundException("payer user not found");
        }

        var payeeUser = getUser.execute(input.payeeId());

        if (payeeUser == null) {
            throw new UserNotFoundException("payee user not found");
        }

        if (payerUser.userType().equals(UserType.SHOPKEEPER.name().toLowerCase())) {
            throw new UnauthorizedOperationException("shopkeeper can't allow to make transfers");
        }

        var transfer = Transfer.create(input.payerId, input.payeeId, input.amount(), input.description());

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
