package com.abcbank.staff;

import com.abcbank.counter.Counter;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmployeeCounterRepository {

    public static final Map<Counter, Employee> counterEmployeesHashMap = new HashMap<>();

    public void assignEmployeeForCounter(Employee employee, Counter counter) {
        counterEmployeesHashMap.put(counter, employee);
    }

    public Employee getEmployeeForCounter(Counter counter) {
        return counterEmployeesHashMap.get(counter);
    }

    public void deAssignFromCounter(Employee employee, Counter counter) {
        counterEmployeesHashMap.remove(counter);
    }
}
