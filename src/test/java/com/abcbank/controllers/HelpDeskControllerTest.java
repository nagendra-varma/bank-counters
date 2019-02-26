package com.abcbank.controllers;

import com.abcbank.BankCountersApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = BankCountersApplication.class)
public class HelpDeskControllerTest {

    @Test
    public void shouldCreateAndServeCustomerRequestOnReachingHelpDesk() {
    }

    @Test
    public void shouldServeCustomerRequestForPremiumUser() {
    }

    @Test
    public void shouldServeCustomerRequestForNonPremiumUser() {
    }
}