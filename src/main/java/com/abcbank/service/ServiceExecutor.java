package com.abcbank.service;

import com.abcbank.staff.Employee;
import com.abcbank.token.Token;

public interface ServiceExecutor {
    void execute(Token token, Employee employee);
}
