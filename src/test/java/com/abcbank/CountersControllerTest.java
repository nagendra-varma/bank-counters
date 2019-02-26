package com.abcbank;

import com.abcbank.counter.Counter;
import com.abcbank.counter.CounterRepository;
import com.abcbank.counter.CounterTokensQueue;
import com.abcbank.customer.Customer;
import com.abcbank.customer.CustomerRepository;
import com.abcbank.service.ServiceType;
import com.abcbank.token.Token;
import com.abcbank.token.TokenGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.Queue;

import static com.abcbank.service.ServiceRequest.ACC_BAL_CHECK;
import static com.abcbank.service.ServiceType.PREMIUM;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BankCountersApplication.class)
@WebAppConfiguration
public class CountersControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private CounterTokensQueue counterTokensQueue;

    @Autowired
    private TokenGenerator tokenGenerator;

    @Autowired
    private CounterRepository counterRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void shouldReturnAllCounters() throws Exception {
        Counter counterOne = new Counter();
        counterOne.setNo(1);
        counterOne.setBranchCode(1);
        counterOne.setServiceType(PREMIUM);
        counterOne.setOnline(true);

        Counter counterTwo = new Counter();
        counterTwo.setNo(2);
        counterTwo.setBranchCode(1);
        counterTwo.setServiceType(ServiceType.NON_PREMIUM);
        counterTwo.setOnline(true);
        mockMvc.perform(get("/counters"))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(
                        content().json("[" + counterOne.toJson() + "," + counterTwo.toJson() + "]")
                );
    }

    @Test
    public void shouldReturnCounterByNo() throws Exception {
        Counter counterOne = new Counter();
        counterOne.setNo(1);
        counterOne.setBranchCode(1);
        counterOne.setServiceType(PREMIUM);
        counterOne.setOnline(true);
        mockMvc.perform(get("/counters/1"))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(
                        content().json(counterOne.toJson())
                );
    }

    @Test
    public void shouldReturnTokensAssignedForCounter() throws Exception {
        Counter counterOne = counterRepository.getCounterByNo(1);

        Customer customer = new Customer();
        customer.setName("Nagendra");
        customer.setId(1);
        customer.setServiceType(PREMIUM);
        customerRepository.save(customer);
        Token token = tokenGenerator.generate(customer, Collections.singletonList(ACC_BAL_CHECK));
        counterTokensQueue.addTokenToCounterQueue(counterOne, token);
        mockMvc.perform(get("/counters/1/tokens"))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(
                        content().json("[" + token.toJson() +"]")
                );
    }

}