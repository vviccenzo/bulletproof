package com.api.bulletproof.service;

import com.api.bulletproof.dto.SendMoneyDTO;
import org.springframework.stereotype.Component;

@Component
public class SendMoneyValidate {

    private final UserService userService;

    SendMoneyValidate(UserService userService) {
        this.userService = userService;
    }

    public void validateSender(SendMoneyDTO dto) {
        boolean hasEnoughBalance = this.userService.hasBalanceForTransaction(dto.sender(), dto.value());
        if (!hasEnoughBalance) {
            throw new RuntimeException("Saldo insuficiente");
        }
    }
}
