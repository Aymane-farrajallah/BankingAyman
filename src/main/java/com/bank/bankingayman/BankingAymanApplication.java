package com.bank.bankingayman;

import com.bank.bankingayman.dtos.CustomerDTO;
import com.bank.bankingayman.entities.*;
import com.bank.bankingayman.enums.AccountStatus;
import com.bank.bankingayman.enums.OperationType;
import com.bank.bankingayman.exceptions.BalanceNotSufficientException;
import com.bank.bankingayman.exceptions.BankAccountNotFoundException;
import com.bank.bankingayman.exceptions.CustomerNotFoundException;
import com.bank.bankingayman.repositories.AccountOperationRepository;
import com.bank.bankingayman.repositories.BankAccountRepository;
import com.bank.bankingayman.repositories.CustomerRepository;
import com.bank.bankingayman.services.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class BankingAymanApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankingAymanApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(BankAccountService bankAccountService){
        return args ->  {
            Stream.of("Hassan", "Imane","Mohamed").forEach(name -> {
                CustomerDTO customer = new CustomerDTO();
                customer.setName(name);
                customer.setEmail(name + "@gmail.com");
                bankAccountService.saveCustomer(customer);
            });
            bankAccountService.listCustomers().forEach(customer -> {
                try {
                    bankAccountService.saveCurrentBankAccount(Math.random()*90000 , customer.getId() , 9000);
                    bankAccountService.saveSavingBankAccount(Math.random()*90000 , customer.getId() , 9000);
                    List<BankAccount> bankAccounts = bankAccountService.BankAccountList();
                    for (BankAccount bankAccount : bankAccounts) {
                        for (int i = 0; i < 10; i++) {
                            bankAccountService.credit(bankAccount.getId(), Math.random()*90000 , "credit");
                        }
                        for (int i = 0; i < 10; i++) {
                            bankAccountService.credit(bankAccount.getId(), Math.random()*90000 , "credit");
                            bankAccountService.debit(bankAccount.getId(), Math.random()*90000 , "debit");
                        }
                    }
                } catch (CustomerNotFoundException e) {
                    e.printStackTrace();
                } catch (BankAccountNotFoundException | BalanceNotSufficientException e) {
                    throw new RuntimeException(e);
                }
            });
        };
    }
}
