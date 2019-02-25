package com.abcbank.counter;

import com.abcbank.service.ServiceType;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class CounterRepository {

    private HashMap<Integer, Counter> counterHashMap = new HashMap<>();

    public CounterRepository() {
        setDefaultCounters();
    }

    public Counter getCounterByNo(int counterNo) {
        return counterHashMap.get(counterNo);
    }


    private void setDefaultCounters() {
        Counter premOneCounter = new Counter();
        premOneCounter.setBranchCode(1);
        premOneCounter.setNo(1);
        premOneCounter.setOnline(true);
        premOneCounter.setServiceType(ServiceType.PREMIUM);
        counterHashMap.put(premOneCounter.getNo(), premOneCounter);

        Counter nonPremOneCounter = new Counter();
        nonPremOneCounter.setBranchCode(1);
        nonPremOneCounter.setNo(2);
        nonPremOneCounter.setOnline(true);
        nonPremOneCounter.setServiceType(ServiceType.NON_PREMIUM);
        counterHashMap.put(nonPremOneCounter.getNo(), nonPremOneCounter);
    }
}
