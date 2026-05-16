package com.api.bulletproof.service;

import com.api.bulletproof.entity.User;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class SendMoneyValidate {

    public void validateSender(User sender, BigDecimal transactionValue) {
        boolean hasEnoughBalance = sender.getTotal().compareTo(transactionValue) >= 0;
        if (!hasEnoughBalance) {
            throw new RuntimeException("Saldo insuficiente");
        }
    }

}
