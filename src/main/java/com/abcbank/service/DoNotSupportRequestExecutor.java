package com.abcbank.service;

import com.abcbank.staff.Employee;
import com.abcbank.token.Token;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@CommonsLog
@Component
@Scope("prototype")
public class DoNotSupportRequestExecutor implements ServiceExecutor {
    @Override
    public void execute(Token token, Employee employee) {
        log.info("Do Not support this service request : " + token.getNextServiceRequest().get());
    }
}
