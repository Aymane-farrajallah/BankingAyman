package com.bank.bankingayman.services;
import com.bank.bankingayman.dtos.CustomerDTO;
import com.bank.bankingayman.entities.*;
import com.bank.bankingayman.enums.OperationType;
import com.bank.bankingayman.exceptions.BalanceNotSufficientException;
import com.bank.bankingayman.exceptions.BankAccountNotFoundException;
import com.bank.bankingayman.exceptions.CustomerNotFoundException;
import com.bank.bankingayman.mappers.BankAccountMapperImpl;
import com.bank.bankingayman.repositories.AccountOperationRepository;
import com.bank.bankingayman.repositories.BankAccountRepository;
import com.bank.bankingayman.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@AllArgsConstructor
@Slf4j
@Service
@Transactional
public class BankServiceImpl implements BankAccountService{

    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;
    private BankAccountMapperImpl dtoMapper;


    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("Saving new Customer");
            Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
            Customer savedCustomer = customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedCustomer);
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        log.info("Saving new Customer");
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedCustomer);
    }

    @Override
    public void deleteCustomer(Long customerId) {
        customerRepository.deleteById(customerId);
    }

    @Override
    public CurrentAccount saveCurrentBankAccount(double initialBalance, Long CustomerId, double overDraft) throws CustomerNotFoundException {

        Customer customer = customerRepository.findById(CustomerId).orElse(null);
        if(customer == null){
            throw new CustomerNotFoundException("Customer not found");
        }

        CurrentAccount currentAccount = new CurrentAccount();

        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount.setCustomer(customer);
        currentAccount.setOverDraft(overDraft);
        CurrentAccount saved = bankAccountRepository.save(currentAccount);

        return saved;
    }

    @Override
    public SavingAccount saveSavingBankAccount(double initialBalance, Long CustomerId, double interestRate) throws CustomerNotFoundException {

        Customer customer = customerRepository.findById(CustomerId).orElse(null);

        if(customer == null){
            throw new CustomerNotFoundException("Customer not found");
        }

        SavingAccount savingAccount = new SavingAccount();

        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setBalance(initialBalance);
        savingAccount.setCustomer(customer);
        savingAccount.setInterestRate(interestRate);
        SavingAccount saved = bankAccountRepository.save(savingAccount);

        return saved;
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        List<Customer> customers = customerRepository.findAll();
        List<CustomerDTO> customerDTOS = customers.stream()
                .map(customer -> dtoMapper.fromCustomer(customer))
                .collect(Collectors.toList());

//        List<CustomerDTO> customerDTOS = new ArrayList<>();
//        for(Customer customer : customers){
//            CustomerDTO customerDTO=dtoMapper.fromCustomer(customer);
//            customerDTOS.add(customerDTO);
//        }
            return customerDTOS;
    };

    @Override
    public CustomerDTO getCustomer(Long customerid) throws CustomerNotFoundException{
        Customer customer = customerRepository.findById(customerid)
                .orElseThrow(()-> new CustomerNotFoundException("Customer Not Found"));
        return dtoMapper.fromCustomer(customer);
    };

    @Override
    public BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException {

        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElse(null);
        if(bankAccount == null){
            throw new BankAccountNotFoundException("bank account not found");
        }
        return bankAccount;
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException {
        BankAccount bankAccount = getBankAccount(accountId);
        if(amount > bankAccount.getBalance())
            throw new BalanceNotSufficientException("Balance not Sufficient");
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setdescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance() - amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount = getBankAccount(accountId);

        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setdescription(description);
        accountOperation.setOperationDate(new Date());

        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);

        bankAccount.setBalance(bankAccount.getBalance() + amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount ) throws BankAccountNotFoundException, BalanceNotSufficientException {
        debit(accountIdSource , amount , "debit" );
        credit(accountIdDestination , amount , "credit" );
    }

    @Override
    public List<BankAccount> BankAccountList() {
        return bankAccountRepository.findAll();
    }


}
