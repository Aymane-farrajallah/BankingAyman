package com.bank.bankingayman;

import com.bank.bankingayman.entities.*;
import com.bank.bankingayman.enums.AccountStatus;
import com.bank.bankingayman.enums.OperationType;
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
                Customer customer = new Customer();
                customer.setName(name);
                customer.setEmail(name + "@gmail.com");
                bankAccountService.saveCustomer(customer);
            });
            bankAccountService.listCustomers().forEach(customer -> {
                try {
                    bankAccountService.saveCurrentBankAccount(Math.random()*90000 , customer.getId() , 9000);
                    bankAccountService.saveSavingBankAccount(Math.random()*90000 , customer.getId() , 9000);
                } catch (CustomerNotFoundException e) {
                    e.printStackTrace();
                }
            });
        };
    }

    //@Bean
    CommandLineRunner start(CustomerRepository customerRepository ,
                            BankAccountRepository bankAccountRepository ,
                            AccountOperationRepository accountOperationRepository) {
        return args -> {
            Stream.of("Hassan","Yassine","Aicha").forEach(name -> {
                Customer customer = new Customer();
                customer.setName(name);
                customer.setEmail(name + "@gmail.com");
                customerRepository.save(customer);
            });
            customerRepository.findAll().forEach(customer -> {
                CurrentAccount  currentAccount = new CurrentAccount();
                currentAccount.setBalance(Math.random() * 90000);
                currentAccount.setId(UUID.randomUUID().toString());
                currentAccount.setCreatedAt(new Date());
                currentAccount.setStatus(AccountStatus.CREATED);
                currentAccount.setCustomer(customer);
                currentAccount.setOverDraft(9000);
                bankAccountRepository.save(currentAccount);

                SavingAccount savingAccount = new SavingAccount();
                savingAccount.setBalance(Math.random() * 90000);
                savingAccount.setId(UUID.randomUUID().toString());
                savingAccount.setCreatedAt(new Date());
                savingAccount.setStatus(AccountStatus.CREATED);
                savingAccount.setCustomer(customer);
                savingAccount.setInterestRate(5);
                bankAccountRepository.save(savingAccount);
            });

            bankAccountRepository.findAll().forEach(acc -> {
                for(int i = 0 ; i <10; i++){
                    AccountOperation accountOperation = new AccountOperation();
                    accountOperation.setOperationDate(new Date());
                    accountOperation.setAmount(Math.random() * 12000);
                    accountOperation.setType(Math.random()>0.5? OperationType.DEBIT : OperationType.CREDIT);
                    accountOperation.setBankAccount(acc);
                    accountOperationRepository.save(accountOperation);
                }
            });
        };
    }
}
