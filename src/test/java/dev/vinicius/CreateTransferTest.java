package dev.vinicius;

import dev.vinicius.application.CreateTransfer;
import dev.vinicius.application.CreateTransfer.Input;
import dev.vinicius.application.TransferRepository;
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
    TransferRepository transferRepository;

    @BeforeEach
    @Transactional
    void setUp() {
        transferRepository.removeAll();
    }

    @Test
    void shouldBeCreateTransfer() {
        var input = new Input("payerId", "payeeId", "description", 500.0);
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
        var input = new Input("payerId", "payeeId", "description", 500.0);
        var output = createTransfer.execute(input);

        var transfer = transferRepository.findTransferById(output.transferId());
        Assertions.assertNotNull(transfer);

        var transfers = transferRepository.findAllTransfers();
        Assertions.assertEquals(1, transfers.size(), "More than one transfer was found, but only one should be present");
    }

}

