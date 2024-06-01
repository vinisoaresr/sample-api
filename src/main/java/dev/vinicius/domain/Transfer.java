package dev.vinicius.domain;

import lombok.Getter;
import java.util.Date;

@Getter
public class Transfer {

    private final String TransferId;

    private final String payerId;

    private final String payeeId;

    private final double amount;

    private final String description;

    private final Date date;

    public Transfer(String transferId, String payer, String payee, double amount, String description, Date date) {
        this.TransferId = transferId;
        this.payerId = payer;
        this.payeeId = payee;
        this.amount = amount;
        this.description = description;
        this.date = date;
    }

    public static Transfer create(String payerId, String payeeId, double amount, String description) {
        var uuid = java.util.UUID.randomUUID().toString();
        return new Transfer(uuid, payerId, payeeId, amount, description, new Date());
    }
}
