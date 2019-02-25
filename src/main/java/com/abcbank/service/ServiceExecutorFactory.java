package com.abcbank.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@AllArgsConstructor
public class ServiceExecutorFactory {

    @Autowired
    private BalCheckRequestExecutor balCheckRequestExecutor;

    @Autowired
    private AddressChangeRequestExecutor addressChangeRequestExecutor;

    @Autowired
    private DemandDraftRequestExecutor demandDraftRequestExecutor;

    public ServiceExecutor getExecutor(ServiceRequest serviceRequest) {
        switch (serviceRequest) {
            case ACC_BAL_CHECK:
                return balCheckRequestExecutor;
            case ADDRESS_CHANGE:
                return addressChangeRequestExecutor;
            case DEMAND_DRAFT:
                return demandDraftRequestExecutor;
                default:
                    return new DoNotSupportRequestExecutor();
        }
    }
}
