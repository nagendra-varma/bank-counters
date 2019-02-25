package com.abcbank.counter;

import com.abcbank.service.ServiceRequest;
import com.abcbank.token.Token;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CounterManager {

    @Autowired
    private CounterAssignerFactory counterAssignerFactory;


    public Token serveToken(Token token) {
        assignCounter(token);
        return token;
    }

    private void assignCounter(Token token) {
        Optional<ServiceRequest> serviceRequest = token.getNextServiceRequest();
        if (serviceRequest.isPresent()) {
            CounterAssigner counterAssigner = counterAssignerFactory.getCounterAssigner(token);
            counterAssigner.assign(token);
        } else {
            throw new IllegalStateException("No service requests found to serve");
        }
    }
}
