package com.bank.bankingayman.entities;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;


@Entity
@DiscriminatorValue("CA")
public class CurrentAccount extends BankAccount{

    private double overDraft;

    public CurrentAccount() {
    }

    public CurrentAccount(double overDraft) {
        this.overDraft = overDraft;
    }

    public double getOverDraft() {
        return overDraft;
    }

    public void setOverDraft(double overDraft) {
        this.overDraft = overDraft;
    }
}
