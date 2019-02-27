package com.abcbank.token;

import com.abcbank.staff.Employee;
import com.abcbank.staff.Role;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.abcbank.staff.Role.MANAGER;
import static com.abcbank.staff.Role.OPERATOR;
import static java.util.Arrays.asList;

@Service
public class TokenRoleAuth {

    private static final List<Role> authorizedRoles = asList(OPERATOR, MANAGER);

    public boolean isAuthorized(Employee employee, TokenStatus tokenStatus) {
        Role role = employee.getRole();
        return authorizedRoles.contains(role);
    }
}
