package com.abcbank.controllers;

import com.abcbank.counter.Counter;
import com.abcbank.counter.CounterRepository;
import com.abcbank.staff.Employee;
import com.abcbank.staff.EmployeeCounterMap;
import com.abcbank.staff.EmployeeRepository;
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
    private EmployeeCounterMap employeeCounterMap;

    @Autowired
    private CounterRepository counterRepository;

    @GetMapping
    public List<Employee> getAll() {
        return stream(employeeRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    @PostMapping
    public Employee newEmployee(@RequestBody Employee employee) {
        return employeeRepository.save(employee);
    }

    @PutMapping("/{id}/counter/{counterNo}")
    public Counter assignCounter(@PathVariable("id") Integer id, @PathVariable("counterNo") Integer counterNo) throws Exception {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (!employee.isPresent()) {
            throw new Exception("Employee with id - " + id + " not found");
        }
        Counter counter = counterRepository.getCounterByNo(counterNo);
        employeeCounterMap.assignEmployeeForCounter(employee.get(), counter);
        return counter;
    }

    @GetMapping("/{id}/counter")
    public Counter getAssignedCounterForEmployee(@PathVariable("id") Integer id) throws Exception {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (!employee.isPresent()) {
            throw new Exception("Employee with id - " + id + " not found");
        }
        Optional<Counter> counter = employeeCounterMap.getCounterAssignedForEmployee(employee.get());
        return counter.orElse(null);
    }

    @DeleteMapping("/{id}/counter")
    @ResponseStatus(OK)
    public void deAssignCounter(@PathVariable("id") Integer id) throws Exception {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (!employee.isPresent()) {
            throw new Exception("Employee with id - " + id + " not found");
        }
        Optional<Counter> counter = employeeCounterMap.getCounterAssignedForEmployee(employee.get());
        if (counter.isPresent()) {
            employeeCounterMap.deAssignFromCounter(employee.get(), counter.get());
        } else {
            throw new Exception("Employee with id - " + id + " not assigned to any counter");
        }
    }
}
