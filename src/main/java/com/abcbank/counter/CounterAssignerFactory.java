package com.abcbank.counter;

import com.abcbank.token.Token;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.abcbank.service.ServiceType.PREMIUM;

@Component
@AllArgsConstructor
public class CounterAssignerFactory {

    @Autowired
    private DefaultCounterAssigner defaultCounterAssigner;

    @Autowired
    private PremiumCounterAssigner premiumCounterAssigner;

    public CounterAssigner getCounterAssigner(Token token) {
        if (token.getServiceType() == PREMIUM) {
            return premiumCounterAssigner;
        }
        return defaultCounterAssigner;
    }
}
