package com.abcbank.service;

import com.abcbank.counter.Counter;
import com.abcbank.counter.CounterManager;
import com.abcbank.staff.Employee;
import com.abcbank.token.Token;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Scope("prototype")
@AllArgsConstructor
public class ServiceRequestDelegator {

    @Autowired
    private ServiceExecutorFactory serviceExecutorFactory;

    @Autowired
    private ServiceCounterRepository serviceCounterRepository;

    @Autowired
    private CounterManager counterManager;

    public void execute(Token token, Employee employee, Counter counter) {
        token.markLastRequestProcessing();
        token.getServiceRequestList().forEach(srd -> {
            Optional<ServiceRequest> serviceRequest = token.getNextServiceRequest();
            if (serviceRequest.isPresent()) {
                if (serviceCounterRepository.canServeRequestByCounter(counter, serviceRequest.get())) {
                    ServiceExecutor serviceExecutor = serviceExecutorFactory.getExecutor(token.getNextServiceRequest().get());
                    serviceExecutor.execute(token, employee);
                }
            }
        });
        if (token.getNextServiceRequest().isPresent()) {
            counterManager.serveToken(token);
        }
    }
}
