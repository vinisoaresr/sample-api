package dev.vinicius.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

import java.util.Date;

@Getter
@Entity(name = "transfer")
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String TransferId;

    private String payerId; // Quem paga

    private String payeeId; // Quem recebe

    private double amount;

    private String description;

    private Date date;

    public Transfer() {
    }

    public Transfer(String transferId, String payerId, String payeeId, double amount, String description, Date date) {
        this.TransferId = transferId;
        this.payerId = payerId;
        this.payeeId = payeeId;
        this.amount = amount;
        this.description = description;
        this.date = date;
    }

    public static Transfer create(String payerId, String payeeId, double amount, String description) {
        return new Transfer(null, payerId, payeeId, amount, description, new Date());
    }

}
