package com.bank.bankingayman.entities;
import com.bank.bankingayman.enums.OperationType;
import jakarta.persistence.*;

import java.util.Date;

@Entity
public class AccountOperation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date operationDate;
    private double amount;
    @Enumerated(EnumType.STRING)
    private OperationType type;

    @ManyToOne
    private BankAccount bankAccount;
    private String description;

    public AccountOperation() {
    }

    public AccountOperation(Long id, Date operationDate, double amount,
                            String description , OperationType type, BankAccount bankAccount) {
        this.id = id;
        this.operationDate = operationDate;
        this.amount = amount;
        this.type = type;
        this.bankAccount = bankAccount;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getdescription() {
        return description;
    }

    public void setdescription(String description) {
        this.description = description;
    }

    public Date getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(Date operationDate) {
        this.operationDate = operationDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public OperationType getType() {
        return type;
    }

    public void setType(OperationType type) {
        this.type = type;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }
}
