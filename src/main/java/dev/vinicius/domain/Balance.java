package dev.vinicius.domain;

import lombok.Getter;

import java.sql.Date;

@Getter
public class Balance {

    private final String balanceId;

    private final String userId;

    private final Date date;

    private final Double balance;

    public Balance(String balanceId, String user, Date date, Double balance) {
        this.balanceId = balanceId;
        this.userId = user;
        this.date = date;
        this.balance = balance;
    }

    public static Balance create(String user, Double balance) {
        var uuid = java.util.UUID.randomUUID().toString();
        return new Balance(uuid, user, new Date(System.currentTimeMillis()), balance);
    }
}
