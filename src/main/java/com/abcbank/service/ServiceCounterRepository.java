package com.abcbank.service;

import com.abcbank.counter.Counter;
import com.abcbank.counter.CounterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

@Service
public class ServiceCounterRepository {

    private Map<ServiceRequest, List<Counter>> counterHashMap = new HashMap<>();

    private CounterRepository counterRepository;

    @Autowired
    public ServiceCounterRepository(CounterRepository counterRepository) {
        this.counterRepository = counterRepository;
        setDefaultCounters();
    }

    /**
     * Returns a list of counters available to service a specific request considering the service type PREMIUM, NON_PREMIUM
     * @param serviceRequest
     * @param serviceType
     * @return
     */
    public Optional<Counter> getCounterForService(ServiceRequest serviceRequest, ServiceType serviceType) {
        return counterHashMap.getOrDefault(serviceRequest, Collections.emptyList())
                .stream()
                .filter(c -> c.getServiceType() == serviceType)
                .findFirst();
    }

    public boolean canServeRequestByCounter(Counter counter, ServiceRequest serviceRequest) {
        return counterHashMap.getOrDefault(serviceRequest, Collections.emptyList()).contains(counter);
    }


    public List<Counter> getAllCounters() {
        List<Counter> counterList = new ArrayList<>();
        counterHashMap.forEach((key, value) -> counterList.addAll(value));
        return counterList.stream().distinct().collect(toList());
    }


    private void setDefaultCounters() {
        counterHashMap.put(ServiceRequest.ACC_BAL_CHECK, asList(counterRepository.getCounterByNo(1), counterRepository.getCounterByNo(2)));
        counterHashMap.put(ServiceRequest.ADDRESS_CHANGE, asList(counterRepository.getCounterByNo(1), counterRepository.getCounterByNo(2)));
        counterHashMap.put(ServiceRequest.DEMAND_DRAFT, asList(counterRepository.getCounterByNo(1), counterRepository.getCounterByNo(2)));
    }
}
