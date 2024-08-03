package dev.vinicius.application;

import dev.vinicius.application.database.BalanceRepository;
import dev.vinicius.application.usecase.CreateBalance;
import dev.vinicius.application.usecase.CreateBalance.Input;
import dev.vinicius.application.usecase.CreateUser;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class CreateBalanceTest {

    @Inject
    CreateBalance createBalance;

    @Inject
    CreateUser createUser;

    @Inject
    BalanceRepository balanceRepository;

    @BeforeEach
    @Transactional
    void setUp() {
        balanceRepository.removeAll();
    }

    private String createUser() {
        String document = Integer.toString((int) (Math.random() * 1000000000));
        String email = document + "@user.com";
        var payerInput = new CreateUser.Input("whatever", email, document, "user", "12345");
        var output = createUser.execute(payerInput);
        return output.userId();
    }

    @Test
    void shouldBeCreateBalance() {
        var userId = createUser();
        var balance = balanceRepository.findByUserId(userId);
        Assertions.assertNull(balance);

        var input = new Input(userId, 500.0);
        var output = createBalance.execute(input);

        Assertions.assertNotNull(output.balanceId());
        Assertions.assertEquals(userId, output.userId());
        Assertions.assertEquals(500.0, output.amount());
    }

}

