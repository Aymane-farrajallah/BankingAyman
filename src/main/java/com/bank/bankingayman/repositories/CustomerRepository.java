package com.bank.bankingayman.repositories;
import com.bank.bankingayman.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
