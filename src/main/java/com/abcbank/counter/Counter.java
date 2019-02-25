package com.abcbank.counter;


import com.abcbank.service.ServiceType;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Counter {
    private int no = -1;
    private int branchCode;
    private boolean online;
    private ServiceType serviceType;

    public boolean isAvailable() {
        return no != -1;
    }
}
