package com.abcbank.controllers;

import com.abcbank.BankCountersApplication;
import com.abcbank.customer.Customer;
import com.abcbank.customer.CustomerRepository;
import com.abcbank.token.Token;
import com.abcbank.token.TokenRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.abcbank.service.ServiceType.NON_PREMIUM;
import static com.abcbank.service.ServiceType.PREMIUM;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = BankCountersApplication.class)
@WebAppConfiguration
public class HelpDeskControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void shouldCreateAndServeCustomerRequestOnReachingHelpDesk() throws Exception {
        Customer customer = new Customer();
        customer.setName("Nagendra");
        customer.setServiceType(NON_PREMIUM);
        customer.setAddress("Hyderabad");
        mockMvc.perform(post("/helpDesk")
                .contentType("application/json")
                .content(customer.toJson()))
                .andExpect(status().isOk());
        assertThat(customerRepository.findAll()).hasSize(1);
    }

    @Test
    public void shouldServeCustomerRequestForPremiumUser() throws Exception {
        Customer customer = new Customer();
        customer.setName("Nagendra");
        customer.setServiceType(PREMIUM);
        customer.setAddress("Hyderabad");
        customer.setId(1);
        customerRepository.save(customer);
        mockMvc.perform(post("/helpDesk/1/ACC_BAL_CHECK"))
                .andExpect(status().isOk());
        assertThat(tokenRepository.getAllTokens()).hasSize(1);
    }

    @Test
    public void shouldServeCustomerRequestForNonPremiumUser() throws Exception {
        Customer customer = new Customer();
        customer.setName("Nagendra");
        customer.setServiceType(NON_PREMIUM);
        customer.setAddress("Hyderabad");
        customer.setId(1);
        customerRepository.save(customer);
        mockMvc.perform(post("/helpDesk/1/ACC_BAL_CHECK"))
                .andExpect(status().isOk());
        assertThat(tokenRepository.getAllTokens()).hasSize(1);
    }
}