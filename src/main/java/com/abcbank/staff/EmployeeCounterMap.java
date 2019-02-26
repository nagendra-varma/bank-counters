package com.abcbank.staff;

import com.abcbank.counter.Counter;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class EmployeeCounterMap {

    public static final Map<Counter, Employee> counterEmployeesMap = new HashMap<>();

    public void assignEmployeeForCounter(Employee employee, Counter counter) throws Exception {
        if (counterEmployeesMap.values().contains(employee)) {
            throw new Exception("Failed to assign, Employee with id - " + employee.getId() + " is already assigned to counter - " + counter.getNo());
        }
        counterEmployeesMap.put(counter, employee);
    }

    public Employee getAssignedEmployee(Counter counter) {
        return counterEmployeesMap.get(counter);
    }

    public Optional<Counter> getCounterAssignedForEmployee(Employee employee) {
        return counterEmployeesMap.entrySet().stream()
                .filter(entry -> entry.getValue().getId().equals(employee.getId()))
                .map(Map.Entry::getKey)
                .findFirst();
    }

    public void deAssignFromCounter(Employee employee, Counter counter) {
        counterEmployeesMap.remove(counter);
    }
}
