package com.abcbank.controllers;


import com.abcbank.counter.Counter;
import com.abcbank.counter.CounterRepository;
import com.abcbank.counter.CounterTokensQueue;
import com.abcbank.service.ServiceCounterRepository;
import com.abcbank.service.ServiceRequestDelegator;
import com.abcbank.staff.Employee;
import com.abcbank.staff.EmployeeCounterMap;
import com.abcbank.staff.EmployeeRepository;
import com.abcbank.token.Token;
import com.abcbank.token.TokenRoleAuth;
import com.abcbank.token.TokenStatus;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Queue;

import static java.util.Objects.isNull;

@RestController
@RequestMapping("/counters")
@AllArgsConstructor
public class CountersController {

    @Autowired
    private ServiceCounterRepository serviceCounterRepository;

    @Autowired
    private CounterTokensQueue counterTokensQueue;

    @Autowired
    private CounterRepository counterRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeCounterMap employeeCounterMap;

    @Autowired
    private ServiceRequestDelegator serviceRequestDelegator;

    @Autowired
    private TokenRoleAuth tokenRoleAuth;

    @GetMapping("/{counterNo}")
    public Counter getCounter(@PathVariable(name = "counterNo") int counterNo) throws Exception {
        return serviceCounterRepository.getAllCounters().stream()
                .filter((counter -> counter.getNo() == counterNo))
                .findFirst()
                .orElseThrow(() -> new Exception("Counter not found with no : " + counterNo));
    }

    @GetMapping
    public List<Counter> getCounters() {
        return serviceCounterRepository.getAllCounters();
    }

    @GetMapping("/{counterNo}/tokens")
    public Queue getTokensForCounter(@PathVariable("counterNo") int counterNo) {
        Counter counter = counterRepository.getCounterByNo(counterNo);
        return counterTokensQueue.getTokensAssignedForCounter(counter);
    }

    @PutMapping("/{counterNo}/serveNextToken")
    public Token serveToken(@PathVariable("counterNo") Integer counterNo) throws Exception {
        Counter counter = counterRepository.getCounterByNo(counterNo);
        if (isNull(counter)) {
            throw new Exception("No counter found with no : " + counterNo);
        }
        Employee employee = employeeCounterMap.getAssignedEmployee(counter);
        if (isNull(employee)) {
            throw new Exception("No employee is assigned to counter - " + counterNo);
        }
        Token token = counterTokensQueue.getNextToken(counter);
        if (isNull(token)) {
            return null;
        }
        serviceRequestDelegator.execute(token, employee, counter);
        return token;
    }

    @PutMapping("/{counterNo}/status/{tokenStatus}")
    public Token markTokenStatus(@PathVariable("counterNo") Integer counterNo,
                                 @PathVariable("tokenStatus") TokenStatus tokenStatus) throws Exception {
        Counter counter = counterRepository.getCounterByNo(counterNo);
        Token token = counterTokensQueue.getNextToken(counter);
        Employee employee = employeeCounterMap.getAssignedEmployee(counter);
        if (tokenRoleAuth.isAuthorized(employee, tokenStatus)) {
            counterTokensQueue.setTokenStatus(token, tokenStatus);
            return token;
        } else {
            throw new Exception("Employee with role - " + employee.getRole() + " not authorized to change the token status");
        }
    }
}
