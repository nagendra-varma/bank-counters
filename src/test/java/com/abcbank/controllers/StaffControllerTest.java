package com.abcbank.controllers;

import com.abcbank.counter.Counter;
import com.abcbank.counter.CounterRepository;
import com.abcbank.staff.Employee;
import com.abcbank.staff.EmployeeRepository;
import com.abcbank.staff.Role;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"spring.config.name=myapp-test-h2","myapp.trx.datasource.url=jdbc:h2:mem:testdb"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class StaffControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private CounterRepository counterRepository;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void shouldAssignAnEmployeeToCounter() throws Exception {
        Employee employee = new Employee();
        employee.setName("Nagendra");
        employee.setRole(Role.MANAGER);
        employee.setId(1);
        employeeRepository.save(employee);

        Counter counter = counterRepository.getCounterByNo(1);

        mockMvc.perform(put("/staff/1/counter/1"))
                .andExpect(status().is(HttpStatus.OK.value()));

        mockMvc.perform(get("/staff/1/counter"))
                .andExpect(status().is(HttpStatus.OK.value()))
        .andExpect(content().json(counter.toJson()));
    }

    @Test
    public void shouldDeAssignEmployeeFromCounter() throws Exception {
        Employee employee = new Employee();
        employee.setName("Nagendra");
        employee.setRole(Role.MANAGER);
        employee.setId(1);
        employeeRepository.save(employee);

        mockMvc.perform(put("/staff/1/counter/1"))
                .andExpect(status().is(HttpStatus.OK.value()));
        mockMvc.perform(delete("/staff/1/counter"))
                .andExpect(status().is(HttpStatus.OK.value()));
    }
}