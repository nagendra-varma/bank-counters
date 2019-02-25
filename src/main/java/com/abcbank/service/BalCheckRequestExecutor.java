package com.abcbank.service;

import com.abcbank.staff.Employee;
import com.abcbank.token.Token;
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
        log.info("BalCheck request completed for token : " + token.getId());
    }
}
