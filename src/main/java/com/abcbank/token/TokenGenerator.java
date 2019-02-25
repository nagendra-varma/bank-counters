package com.abcbank.token;

import com.abcbank.customer.Customer;
import com.abcbank.service.ServiceRequest;
import com.abcbank.service.ServiceRequestDetails;
import com.abcbank.service.ServiceType;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TokenGenerator {

    @Autowired
    private TokenRepository tokenRepository;

    public Token generate(Customer customer, List<ServiceRequest> serviceRequestList) {
        Token token = tokenRepository.getNewToken();
        assignPriority(token, customer.getServiceType());
        assignServiceRequests(token, serviceRequestList);
        return token;
    }

    private void assignServiceRequests(Token token, List<ServiceRequest> serviceRequestList) {
        List<ServiceRequestDetails> serviceRequestDetailsList = serviceRequestList.stream().map(serviceRequest -> {
            ServiceRequestDetails serviceRequestDetails = new ServiceRequestDetails();
            serviceRequestDetails.setServiceRequest(serviceRequest);
            return serviceRequestDetails;
        }).collect(Collectors.toList());
        token.setServiceRequestList(new ArrayDeque<>(serviceRequestDetailsList));
    }

    private void assignPriority(Token token, ServiceType serviceType) {
        token.setServiceType(serviceType);
    }
}
