package com.abcbank;

import com.abcbank.customer.Customer;
import com.abcbank.customer.CustomerRepository;
import com.abcbank.helpdesk.HelpDesk;
import com.google.common.collect.ImmutableList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static com.abcbank.service.ServiceRequest.ACC_BAL_CHECK;
import static com.abcbank.service.ServiceRequest.DEMAND_DRAFT;
import static com.abcbank.service.ServiceType.PREMIUM;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BankCountersApplication.class)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
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
        helpDesk.onCustomerAtDesk(customer, ImmutableList.of(ACC_BAL_CHECK));
        assertThat(customerRepository.findAll()).hasSize(1);
    }

    @Test
    public void shouldGetIfUserExistsWithSameId() {
        Customer customer = getCustomer();
        helpDesk.onCustomerAtDesk(customer, singletonList(ACC_BAL_CHECK));
        helpDesk.onCustomerAtDesk(customer, singletonList(ACC_BAL_CHECK));

        assertThat(customerRepository.findAll()).hasSize(1);
    }

    @Test
    public void shouldGetATokenWhenCustomerRequestsAtDesk() {
        Customer customer = getCustomer();
        assertNotNull(helpDesk.onCustomerAtDesk(customer, singletonList(DEMAND_DRAFT)));
    }

    private Customer getCustomer() {
        Customer customer = new Customer();
        customer.setName("Nagendra");
        customer.setId(1);
        customer.setAddress("Hyderabad");
        customer.setServiceType(PREMIUM);
        return customer;
    }
}