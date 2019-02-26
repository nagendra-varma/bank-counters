package com.abcbank.service;

import com.abcbank.counter.CounterManager;
import com.abcbank.staff.Employee;
import com.abcbank.token.Token;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@CommonsLog
@Component
@Scope("prototype")
public class AddressChangeRequestExecutor implements ServiceExecutor {

    @Autowired
    private CounterManager counterManager;

    @Override
    public void execute(Token token, Employee employee) {
        log.info("Address change request completed for token : " + token.getId());
        token.onRecentRequestCompleted("Address successfully changed");
    }
}
