package com.abcbank.service;

import com.abcbank.staff.Employee;
import com.abcbank.token.Token;
import com.abcbank.token.TokenStatus;
import lombok.NoArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@CommonsLog
@Component
@Scope("prototype")
public class BalCheckRequestExecutor implements ServiceExecutor {

    @Override
    public void execute(Token token, Employee employee) {
        token.onRecentRequestCompleted("Balance is $100000000000");
        log.info("BalCheck request completed for token : " + token.getId());
    }
}
