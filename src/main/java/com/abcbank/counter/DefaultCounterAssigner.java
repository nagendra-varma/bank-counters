package com.abcbank.counter;

import com.abcbank.service.ServiceRequest;
import com.abcbank.service.ServiceCounterRepository;
import com.abcbank.token.Token;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class DefaultCounterAssigner implements CounterAssigner {

    @Autowired
    private ServiceCounterRepository serviceCounterRepository;

    @Autowired
    private CounterTokensQueue counterTokensQueue;

    @Override
    public void assign(Token token) {
        Optional<ServiceRequest> first = token.getNextServiceRequest();
        first.ifPresent(serviceRequest -> {
            Optional<Counter> availableCounter = serviceCounterRepository.getCounterForService(serviceRequest, token.getServiceType());
            availableCounter.ifPresent(counter -> {
                token.setAssignedCounter(counter);
                counterTokensQueue.addTokenToCounterQueue(counter, token);
            });
        });
    }
}
