package com.abcbank;

import com.abcbank.counter.Counter;
import com.abcbank.counter.CounterRepository;
import com.abcbank.counter.CounterTokensQueue;
import com.abcbank.customer.Customer;
import com.abcbank.customer.CustomerRepository;
import com.abcbank.service.ServiceType;
import com.abcbank.staff.Employee;
import com.abcbank.staff.EmployeeCounterMap;
import com.abcbank.staff.EmployeeRepository;
import com.abcbank.staff.Role;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.abcbank.service.ServiceRequest.ACC_BAL_CHECK;
import static com.abcbank.service.ServiceType.NON_PREMIUM;
import static com.abcbank.service.ServiceType.PREMIUM;
import static com.abcbank.staff.Role.GENERAL;
import static com.abcbank.token.TokenStatus.COMPLETED;
import static com.abcbank.token.TokenStatus.IN_PROGRESS;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeCounterMap employeeCounterMap;

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
        Token token = tokenGenerator.generate(customer, singletonList(ACC_BAL_CHECK));
        counterTokensQueue.addTokenToCounterQueue(counterOne, token);
        mockMvc.perform(get("/counters/1/tokens"))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(
                        content().json("[" + token.toJson() +"]")
                );
    }

    @Test
    public void shouldEmployeeBeAbleToServeNextTokenWaitingInQueue() throws Exception {
        Employee employee = new Employee();
        employee.setId(1);
        employee.setRole(Role.MANAGERS);
        employee.setName("Nagendra");
        employeeRepository.save(employee);
        Counter counter = counterRepository.getCounterByNo(1);
        employeeCounterMap.assignEmployeeForCounter(employee, counter);

        Customer customer = new Customer();
        customer.setName("Nagendra");
        customer.setId(1);
        customer.setServiceType(PREMIUM);
        Token token = tokenGenerator.generate(customer, singletonList(ACC_BAL_CHECK));
        counterTokensQueue.addTokenToCounterQueue(counter, token);

        mockMvc.perform(put("/counters/1/serveNextToken"))
                .andExpect(status().isOk())
                .andExpect(content().json(token.toJson()));
        assertThat(token.getTokenStatus()).isEqualTo(IN_PROGRESS);
        assertThat(token.getNextServiceRequest().isPresent()).isFalse();
    }

    @Test
    public void shouldOperatorsAndManagersOnlyBeAbleToCloseToken() throws Exception {
        Employee employee = new Employee();
        employee.setId(1);
        employee.setRole(Role.MANAGERS);
        employee.setName("Nagendra");
        employeeRepository.save(employee);
        Counter counter = counterRepository.getCounterByNo(1);
        employeeCounterMap.assignEmployeeForCounter(employee, counter);

        Customer customer = new Customer();
        customer.setName("Nagendra");
        customer.setId(1);
        customer.setServiceType(PREMIUM);
        Token token = tokenGenerator.generate(customer, singletonList(ACC_BAL_CHECK));
        counterTokensQueue.addTokenToCounterQueue(counter, token);

        mockMvc.perform(put("/counters/1/serveNextToken"))
                .andExpect(status().isOk());

        mockMvc.perform(put("/counters/1/status/COMPLETED"))
                .andExpect(status().isOk());
        assertThat(token.getTokenStatus()).isEqualTo(COMPLETED);
    }

    @Test(expected = Exception.class)
    public void shouldNotAllGeneralRoleEmployeesToUpdateTokenStatus() throws Exception {
        Employee employee = new Employee();
        employee.setName("Nagu");
        employee.setRole(GENERAL);
        employee.setId(3);
        employeeRepository.save(employee);

        Counter counter = counterRepository.getCounterByNo(2);
        employeeCounterMap.assignEmployeeForCounter(employee, counter);

        Customer customer = new Customer();
        customer.setName("Nagu");
        customer.setId(1);
        customer.setServiceType(NON_PREMIUM);
        Token token2 = tokenGenerator.generate(customer, singletonList(ACC_BAL_CHECK));
        counterTokensQueue.addTokenToCounterQueue(counter, token2);
        mockMvc.perform(put("/counters/2/serveNextToken"))
                .andExpect(status().isOk());

        mockMvc.perform(put("/counters/2/status/COMPLETED"));
    }
}