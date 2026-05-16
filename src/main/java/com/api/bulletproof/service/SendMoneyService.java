package com.api.bulletproof.service;

import com.api.bulletproof.entity.Transaction;
import com.api.bulletproof.entity.User;
import com.api.bulletproof.entity.Wallet;
import com.api.bulletproof.repository.WalletRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Component
public class SendMoneyService {

    private final WalletRepository walletRepository;

    SendMoneyService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public void transferMoney(User sender, User receiver, BigDecimal transferenceValue, Transaction transaction) {
        Wallet walletSender = getWallet(sender.getWalletId());
        Wallet walletReceiver = getWallet(receiver.getWalletId());

        withdraw(sender, transferenceValue, transaction, walletSender);
        deposit(receiver, transferenceValue, transaction, walletReceiver);

        transaction.setValue(transferenceValue);

        this.walletRepository.saveAllAndFlush(List.of(walletSender, walletReceiver));
    }

    private void deposit(User receiver, BigDecimal transferenceValue, Transaction transaction, Wallet walletReceiver) {
        BigDecimal receiverNewValue = this.getReceiverNewValue(walletReceiver, transferenceValue);
        transaction.setOldValueReceiver(walletReceiver.getTotal());
        transaction.setReceiver(receiver);
        transaction.setNewValueReceiver(receiverNewValue);
        walletReceiver.setTotal(receiverNewValue);
    }

    private void withdraw(User sender, BigDecimal transferenceValue, Transaction transaction, Wallet walletSender) {
        BigDecimal senderNewValue = this.getSenderNewValue(walletSender, transferenceValue);
        transaction.setOldValueSender(walletSender.getTotal());
        transaction.setNewValueSender(senderNewValue);
        transaction.setSender(sender);
        walletSender.setTotal(senderNewValue);
    }

    private BigDecimal getSenderNewValue(Wallet sender, BigDecimal transferenceValue) {
        return sender.getTotal().subtract(transferenceValue);
    }

    private BigDecimal getReceiverNewValue(Wallet receiver, BigDecimal transferenceValue) {
        return receiver.getTotal().add(transferenceValue);
    }

    private Wallet getWallet(UUID id) {
        return this.walletRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }
}
