package dev.vinicius.application;

import dev.vinicius.application.database.BalanceRepository;
import dev.vinicius.application.database.TransferRepository;
import dev.vinicius.application.database.UserRepository;
import dev.vinicius.application.usecase.CreateBalance;
import dev.vinicius.application.usecase.CreateTransfer;
import dev.vinicius.application.usecase.CreateTransfer.Input;
import dev.vinicius.application.usecase.CreateUser;
import dev.vinicius.infra.exceptions.*;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class CreateTransferTest {

    @Inject
    CreateTransfer createTransfer;

    @Inject
    CreateUser createUser;

    @Inject
    CreateBalance createBalance;

    @Inject
    TransferRepository transferRepository;

    @Inject
    BalanceRepository balanceRepository;

    @Inject
    UserRepository userRepository;

    @BeforeEach
    @Transactional
    void setUp() {
        transferRepository.removeAll();
        userRepository.removeAll();
    }

    private String createShopkeeper() {
        var shopkeeperInput = new CreateUser.Input("shopkeeper", "shopkeeper@shop.com", "123456789", "shopkeeper", "12345");
        var output = createUser.execute(shopkeeperInput);
        return output.userId();
    }

    private String createUser() {
        String document = Integer.toString((int) (Math.random() * 1000000000));
        String email = document + "@user.com";
        var payerInput = new CreateUser.Input("whatever", email, document, "user", "12345");
        var output = createUser.execute(payerInput);

        var balanceInput = new CreateBalance.Input(output.userId(), 501.0);
        createBalance.execute(balanceInput);
        return output.userId();
    }

    @Test
    void shouldBeCreateTransfer() {
        var payerId = createUser();
        var payeeId = createUser();
        var input = new Input(payerId, payeeId, "any description", 500.0);
        var output = createTransfer.execute(input);

        Assertions.assertNotNull(output, "Any output should be returned");

        var transfer = transferRepository.findTransferById(output.transferId());
        Assertions.assertNotNull(transfer);
        Assertions.assertNotNull(transfer.getTransferId(), "TransferId should be present");
        Assertions.assertEquals(input.payerId(), transfer.getPayerId());
        Assertions.assertEquals(input.payeeId(), transfer.getPayeeId());
        Assertions.assertEquals(input.description(), transfer.getDescription());
        Assertions.assertEquals(input.amount(), transfer.getAmount());
    }

    @Test
    void shouldBeOnlyOneTransferCreated() {
        var payerId = createUser();
        var payeeId = createUser();
        var input = new Input(payerId, payeeId, "any description", 500.0);
        var output = createTransfer.execute(input);

        var transfer = transferRepository.findTransferById(output.transferId());
        Assertions.assertNotNull(transfer);

        var transfers = transferRepository.findAllTransfers();
        Assertions.assertEquals(1, transfers.size(), "More than one transfer was found, but only one should be present");
    }

    @Test
    void shouldBeThrowExceptionWhenPayerIdIsNull() {
        var payeeId = createUser();
        var input = new Input(null, payeeId, "any description", 500.0);

        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> createTransfer.execute(input));

        Assertions.assertEquals("payerId is required", exception.getMessage());
    }

    @Test
    void shouldBeThrowExceptionWhenPayeeIdIsNull() {
        var payerId = createUser();
        var input = new Input(payerId, null, "any description", 500.0);

        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> createTransfer.execute(input));

        Assertions.assertEquals("payeeId is required", exception.getMessage());
    }

    @Test
    void shouldBeThrowExceptionWhenAmountIsInvalid() {
        var payerId = createUser();
        var payeeId = createUser();
        var input = new Input(payerId, payeeId, "any description", -500.0);

        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> createTransfer.execute(input));

        Assertions.assertEquals("amount should be greater than zero", exception.getMessage());
    }

    @Test
    void shouldBeThrowUserNotFoundExceptionWhenPayerIdNotFound() {
        var payeeId = createUser();
        var input = new Input("invalid-id", payeeId, "any description", 500.0);

        Exception exception = Assertions.assertThrows(UserNotFoundException.class, () -> createTransfer.execute(input));

        Assertions.assertEquals("payer user not found", exception.getMessage());
    }

    @Test
    void shouldBeThrowUserNotFoundExceptionWhenPayeeIdNotFound() {
        var payerId = createUser();
        var input = new Input(payerId, "invalid-id", "any description", 500.0);

        Exception exception = Assertions.assertThrows(UserNotFoundException.class, () -> createTransfer.execute(input));

        Assertions.assertEquals("payee user not found", exception.getMessage());
    }

    @Test
    void shouldBeThrowExceptionWhenPayerIdIsShopkeeper() {
        var payerId = createShopkeeper();
        var payeeId = createUser();
        var input = new Input(payerId, payeeId, "description", 500.0);

        Exception exception = Assertions.assertThrows(UnauthorizedOperationException.class, () -> createTransfer.execute(input));

        Assertions.assertEquals("shopkeeper can't allow to make transfers", exception.getMessage());
    }

    @Test
    void shouldBeThrowUnauthorizedOperationExceptionWhenPayerHasNoBalance() {
        var payerId = createUser();
        var payeeId = createUser();
        var input = new Input(payerId, payeeId, "description", 1000.0);

        Exception exception = Assertions.assertThrows(UnauthorizedOperationException.class, () -> createTransfer.execute(input));
        Assertions.assertEquals("payer has no balance to make this transfer", exception.getMessage());
    }

    @Test
    void shouldBeCreateNewBalanceWhenTransferIsCreated() {
        var payerId = createUser(); // balance = 501.0
        var payeeId = createUser(); // balance = 501.0
        var input = new Input(payerId, payeeId, "description", 500.0);
        createTransfer.execute(input);

        var payerBalance = balanceRepository.findByUserId(payerId);
        var payeeBalance = balanceRepository.findByUserId(payeeId);

        Assertions.assertEquals(1.0, payerBalance.getAmount());
        Assertions.assertEquals(1001.0, payeeBalance.getAmount());

    }

    // verify if ExternalAuthorizationService is called with correct parameters
    // verify if ExternalAuthorizationService is called once (mock)
    // verify if transaction is rollbacked when ExternalAuthorizationService throws an exception
}


