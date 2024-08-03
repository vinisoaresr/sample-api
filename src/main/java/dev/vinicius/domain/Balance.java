package dev.vinicius.domain;

import lombok.Getter;
import java.sql.Date;

@Getter
public class Balance {

    private final String balanceId;

    private final String userId;

    private Date date;

    private Double amount;

    public Balance(String balanceId, String userId, Date date, Double amount) {
        this.balanceId = balanceId;
        this.userId = userId;
        this.date = date;
        this.amount = amount;
    }

    public static Balance create(String user, Double balance) {
        var uuid = java.util.UUID.randomUUID().toString();
        var date = new Date(System.currentTimeMillis());
        return new Balance(uuid, user, date, balance);
    }
}
