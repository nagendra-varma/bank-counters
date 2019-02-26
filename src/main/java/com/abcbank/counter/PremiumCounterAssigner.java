package com.abcbank.counter;

import com.abcbank.service.ServiceRequest;
import com.abcbank.service.ServiceType;
import com.abcbank.service.ServiceCounterRepository;
import com.abcbank.token.Token;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.abcbank.service.ServiceType.NON_PREMIUM;

@Service
@AllArgsConstructor
public class PremiumCounterAssigner implements CounterAssigner {

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
            if (!availableCounter.isPresent() && token.getServiceType() == ServiceType.PREMIUM) {
                Optional<Counter> nonPremiumCounter = serviceCounterRepository.getCounterForService(serviceRequest, NON_PREMIUM);
                nonPremiumCounter.ifPresent(npc -> {
                    token.setAssignedCounter(npc);
                    counterTokensQueue.addTokenToCounterQueue(npc, token);
                });
            }
        });
    }
}
