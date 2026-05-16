package com.api.bulletproof.service;

import com.api.bulletproof.entity.Transaction;
import com.api.bulletproof.entity.Wallet;
import com.api.bulletproof.repository.WalletRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Component
public class SendMoneyService {

    private final WalletService walletService;
    private final WalletRepository walletRepository;

    SendMoneyService(WalletService walletService, WalletRepository walletRepository) {
        this.walletService = walletService;
        this.walletRepository = walletRepository;
    }

    public void transferMoney(UUID walletSenderId, UUID walletReceiverId, BigDecimal transferenceValue, Transaction transaction) {
        Wallet walletSender = this.walletService.findById(walletSenderId);
        Wallet walletReceiver = this.walletService.findById(walletReceiverId);

        this.withdraw(transferenceValue, transaction, walletSender);
        this.deposit(transferenceValue, transaction, walletReceiver);

        transaction.setValue(transferenceValue);

        this.walletRepository.saveAllAndFlush(List.of(walletSender, walletReceiver));
    }

    private void deposit(BigDecimal transferenceValue, Transaction transaction, Wallet walletReceiver) {
        BigDecimal receiverNewValue = this.getReceiverNewValue(walletReceiver, transferenceValue);
        transaction.setOldValueReceiver(walletReceiver.getTotal());
        transaction.setNewValueReceiver(receiverNewValue);
        walletReceiver.setTotal(receiverNewValue);
    }

    private void withdraw(BigDecimal transferenceValue, Transaction transaction, Wallet walletSender) {
        BigDecimal senderNewValue = this.getSenderNewValue(walletSender, transferenceValue);
        transaction.setOldValueSender(walletSender.getTotal());
        transaction.setNewValueSender(senderNewValue);
        walletSender.setTotal(senderNewValue);
    }

    private BigDecimal getSenderNewValue(Wallet sender, BigDecimal transferenceValue) {
        return sender.getTotal().subtract(transferenceValue);
    }

    private BigDecimal getReceiverNewValue(Wallet receiver, BigDecimal transferenceValue) {
        return receiver.getTotal().add(transferenceValue);
    }
}
