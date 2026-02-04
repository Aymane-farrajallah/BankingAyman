package com.bank.bankingayman.web;
import com.bank.bankingayman.entities.Customer;
import com.bank.bankingayman.services.BankAccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j

public class CustomerRestController {
    private BankAccountService bankAccountService;

    @GetMapping("/customers")
    public List<Customer> customers(){
        return bankAccountService.listCustomers();
    }
}
