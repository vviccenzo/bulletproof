package com.api.bulletproof.service;

import com.api.bulletproof.entity.User;
import com.api.bulletproof.entity.Wallet;
import com.api.bulletproof.repository.WalletRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class SendMoneyValidate {

    private final WalletRepository walletRepository;

    SendMoneyValidate(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }


    public void validateSender(User sender, BigDecimal transactionValue) {
        Wallet wallet = getWallet(sender.getWalletId());
        boolean hasEnoughBalance = wallet.getTotal().compareTo(transactionValue) >= 0;
        if (!hasEnoughBalance) {
            throw new RuntimeException("Saldo insuficiente");
        }
    }

    private Wallet getWallet(UUID id) {
        return this.walletRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }
}
