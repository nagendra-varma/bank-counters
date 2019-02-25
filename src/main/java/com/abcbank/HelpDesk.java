package com.abcbank;


import com.abcbank.counter.CounterManager;
import com.abcbank.customer.Customer;
import com.abcbank.customer.CustomerRepository;
import com.abcbank.service.ServiceRequest;
import com.abcbank.token.Token;
import com.abcbank.token.TokenGenerator;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

@Service
@AllArgsConstructor
public class HelpDesk {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TokenGenerator tokenGenerator;

    @Autowired
    private CounterManager counterManager;


    public Token onCustomerAtDesk(Customer customer, List<ServiceRequest> serviceRequestList) {
        Customer dbCustomer =  createIfUserNotExists(customer);
        Token token = tokenGenerator.generate(dbCustomer, serviceRequestList);
        return counterManager.serveToken(token);
    }

    public Customer createCustomer(Customer customer) {
        return createIfUserNotExists(customer);
    }

    private Customer createIfUserNotExists(Customer customer) {
        if (!isNull(customer.getId())) {
            Optional<Customer> savedCustomer = customerRepository.findById(customer.getId());
            return savedCustomer.orElseGet(() -> customerRepository.save(customer));
        }
        return customerRepository.save(customer);
    }
}
