package com.api.bulletproof.service;

import com.api.bulletproof.entity.User;
import com.api.bulletproof.entity.Wallet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class SendMoneyValidate {

    private final WalletService walletService;

    SendMoneyValidate(WalletService walletService) {
        this.walletService = walletService;
    }

    public void validateSender(User sender, BigDecimal transactionValue) {
        if (transactionValue.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Valor deve ser maior que zero");
        }

        Wallet wallet = walletService.findById(sender.getWalletId());
        boolean hasEnoughBalance = wallet.getTotal().compareTo(transactionValue) >= 0;
        if (!hasEnoughBalance) {
            throw new RuntimeException("Saldo insuficiente");
        }
    }
}
