package com.abcbank.controllers;


import com.abcbank.counter.Counter;
import com.abcbank.counter.CounterRepository;
import com.abcbank.counter.CounterTokensQueue;
import com.abcbank.service.ServiceCounterRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Queue;

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
}
