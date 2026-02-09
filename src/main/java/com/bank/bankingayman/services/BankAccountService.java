package com.bank.bankingayman.services;

import com.bank.bankingayman.dtos.BankAccountDTO;
import com.bank.bankingayman.dtos.CurrentBankAccountDTO;
import com.bank.bankingayman.dtos.CustomerDTO;
import com.bank.bankingayman.dtos.SavingBankAccountDTO;
import com.bank.bankingayman.entities.BankAccount;
import com.bank.bankingayman.entities.CurrentAccount;
import com.bank.bankingayman.entities.Customer;
import com.bank.bankingayman.entities.SavingAccount;
import com.bank.bankingayman.exceptions.BalanceNotSufficientException;
import com.bank.bankingayman.exceptions.BankAccountNotFoundException;
import com.bank.bankingayman.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {

    CustomerDTO saveCustomer(CustomerDTO customerDTO);

    CustomerDTO updateCustomer(CustomerDTO customerDTO);

    void deleteCustomer(Long customerId);

    CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance , Long CustomerId , double overDraft) throws CustomerNotFoundException;
    SavingBankAccountDTO saveSavingBankAccount(double initialBalance , Long CustomerId , double interestRate) throws CustomerNotFoundException;
    List<CustomerDTO> listCustomers();

    CustomerDTO getCustomer(Long customerid) throws CustomerNotFoundException;

    BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException;
    void debit(String accountId, double amount , String description) throws BankAccountNotFoundException, BalanceNotSufficientException;
    void credit(String accountId, double amount , String description) throws BankAccountNotFoundException;
    void transfer(String accountIdSource , String accountIdDestination, double amount ) throws BankAccountNotFoundException, BalanceNotSufficientException;
    List<BankAccount> BankAccountList();
}
