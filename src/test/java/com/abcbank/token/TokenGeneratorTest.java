package com.abcbank.token;

import com.abcbank.BankCountersApplication;
import com.abcbank.customer.Customer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.abcbank.service.ServiceType.PREMIUM;
import static com.abcbank.token.TokenStatus.CREATED;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BankCountersApplication.class)
public class TokenGeneratorTest {

    @Autowired
    TokenGenerator tokenGenerator;

    @Test
    public void shouldGenerateTokenWithPriorityForPremiumCustomer() {
        Customer customer = getCustomer();
        customer.setServiceType(PREMIUM);
        Token token = tokenGenerator.generate(customer, emptyList());
        assertThat(token.getServiceType()).isEqualTo(PREMIUM);
        assertThat(token.getTokenStatus()).isEqualTo(CREATED);
    }

    private Customer getCustomer() {
        Customer customer = new Customer();
        customer.setName("Nagendra");
        customer.setId(1);
        customer.setAddress("Hyderabad");
        return customer;
    }
}