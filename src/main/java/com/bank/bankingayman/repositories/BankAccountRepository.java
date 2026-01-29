package com.bank.bankingayman.repositories;
import com.bank.bankingayman.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
}
