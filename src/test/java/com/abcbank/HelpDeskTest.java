package com.abcbank;

import com.abcbank.customer.Customer;
import com.abcbank.customer.CustomerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BankCountersApplication.class)
@WebAppConfiguration
public class HelpDeskTest {


    @Autowired
    HelpDesk helpDesk;

    @Autowired
    CustomerRepository customerRepository;

    @Test
    public void shouldCreateUserIfDoesNotExists() {
        Customer customer = new Customer();
        customer.setName("Nagendra");
        customer.setAddress("Hyderabad");
        helpDesk.onCustomerAtDesk(customer, emptyList());
        assertThat(customerRepository.findAll()).hasSize(1);
    }

    @Test
    public void shouldGetIfUserExistsWithSameId() {
        Customer customer = getCustomer();
        helpDesk.onCustomerAtDesk(customer, emptyList());
        helpDesk.onCustomerAtDesk(customer, emptyList());

        assertThat(customerRepository.findAll()).hasSize(1);
    }

    @Test
    public void shouldGetATokenWhenCustomerRequestsAtDesk() {
        Customer customer = getCustomer();
        assertNotNull(helpDesk.onCustomerAtDesk(customer, emptyList()));
    }

    private Customer getCustomer() {
        Customer customer = new Customer();
        customer.setName("Nagendra");
        customer.setId(1);
        customer.setAddress("Hyderabad");
        return customer;
    }
}