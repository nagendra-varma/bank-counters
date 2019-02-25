package com.abcbank.controllers;

import com.abcbank.counter.Counter;
import com.abcbank.counter.CounterRepository;
import com.abcbank.counter.CounterTokensQueue;
import com.abcbank.service.ServiceRequestDelegator;
import com.abcbank.staff.Employee;
import com.abcbank.staff.EmployeeCounterRepository;
import com.abcbank.staff.EmployeeRepository;
import com.abcbank.token.Token;
import com.abcbank.token.TokenRoleAuth;
import com.abcbank.token.TokenStatus;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.StreamSupport.stream;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/staff")
@AllArgsConstructor
public class StaffController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeCounterRepository employeeCounterRepository;

    @Autowired
    private CounterRepository counterRepository;

    @Autowired
    private CounterTokensQueue counterTokensQueue;

    @Autowired
    private TokenRoleAuth tokenRoleAuth;

    @Autowired
    private ServiceRequestDelegator serviceRequestDelegator;

    @GetMapping
    public List<Employee> getAll() {
        return stream(employeeRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    @PostMapping
    public Employee newEmployee(@RequestBody Employee employee) {
        return employeeRepository.save(employee);
    }

    @PostMapping("/{id}/counter/{counterNo}")
    public Counter assignCounter(@PathVariable("id") Integer id, @PathVariable("counterNo") Integer counterNo) throws Exception {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (!employee.isPresent()) {
            throw new Exception("Employee with id - " + id + " not found");
        }
        Counter counter = counterRepository.getCounterByNo(counterNo);
        employeeCounterRepository.assignEmployeeForCounter(employee.get(), counter);
        return counter;
    }

    @DeleteMapping("/{id}/counters/{counterNo}")
    @ResponseStatus(OK)
    public void deAssignCounter(@PathVariable("id") Integer id, @PathVariable("counterNo") Integer counterNo) throws Exception {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (!employee.isPresent()) {
            throw new Exception("Employee with id - " + id + " not found");
        }
        Counter counter = counterRepository.getCounterByNo(counterNo);
        Employee assignedEmployee = employeeCounterRepository.getEmployeeForCounter(counter);
        if (assignedEmployee.getId().equals(employee.get().getId())) {
            employeeCounterRepository.deAssignFromCounter(employee.get(), counter);
        } else {
            throw new Exception("Employee with id - " + id + " not assigned to counter " + counterNo);
        }
    }

    @PutMapping("/{id}/counters/{counterNo}/serveNextToken")
    public void serveToken(@PathVariable("id") Integer id, @PathVariable("counterNo") Integer counterNo) throws Exception {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (!employee.isPresent()) {
            throw new Exception("Employee with id - " + id + " not found");
        }
        Counter counter = counterRepository.getCounterByNo(counterNo);
        Employee assignedEmployee = employeeCounterRepository.getEmployeeForCounter(counter);
        if (assignedEmployee.getId().equals(employee.get().getId())) {
            Token token = counterTokensQueue.getNextTokenToServeAssignedForCounter(counter);
            serviceRequestDelegator.execute(token, employee.get(), counter);
        } else {
            throw new Exception("Employee with id - " + id + " not assigned to counter " + counterNo);
        }
    }

    @PutMapping("/{counterNo}/status/{tokenStatus}")
    public Token markTokenStatus(@PathVariable("counterNo") Integer counterNo,
                                 @PathVariable("tokenStatus") TokenStatus tokenStatus) throws Exception {
        Counter counter = counterRepository.getCounterByNo(counterNo);
        Token token = counterTokensQueue.getNextTokenToServeAssignedForCounter(counter);
        Employee employee = employeeCounterRepository.getEmployeeForCounter(counter);
        if (tokenRoleAuth.isAuthorized(employee, tokenStatus)) {
            counterTokensQueue.setTokenStatus(token, tokenStatus);
            return token;
        } else {
            throw new Exception("Employee with role - " + employee.getRole() + " not authorized to change the token status");
        }
    }
}
