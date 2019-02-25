package com.abcbank.controllers;

import com.abcbank.HelpDesk;
import com.abcbank.service.ServiceRequest;
import com.abcbank.customer.Customer;
import com.abcbank.customer.CustomerRepository;
import com.abcbank.token.Token;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static java.util.Collections.singletonList;

@RestController
@RequestMapping("/helpDesk")
@AllArgsConstructor
public class HelpDeskController {

    @Autowired
    private HelpDesk helpDesk;

    @Autowired
    private CustomerRepository customerRepository;

    @PostMapping
    public Customer newCustomerAtDesk(@RequestBody Customer customer) {
        return helpDesk.createCustomer(customer);
    }

    @PostMapping("/{customerNo}/{serviceRequest}")
    public Token customerAtHelpDesk(@PathVariable("customerNo") Integer customerNo, @PathVariable("serviceRequest") ServiceRequest serviceRequest) throws Exception {
        Optional<Customer> customer = customerRepository.findById(customerNo);
        if (!customer.isPresent()) {
            throw new Exception("Customer not found with id : " + customerNo);
        }
        return helpDesk.onCustomerAtDesk(customer.get(), singletonList(serviceRequest));
    }
}
