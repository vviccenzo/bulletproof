package com.api.bulletproof.service;

import com.api.bulletproof.entity.Transaction;
import com.api.bulletproof.entity.User;
import com.api.bulletproof.entity.Wallet;
import com.api.bulletproof.repository.UserRepository;
import com.api.bulletproof.repository.WalletRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Component
public class SendMoneyService {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;

    SendMoneyService(UserRepository userRepository, WalletRepository walletRepository) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
    }

    public void transferMoney(User sender, User receiver, BigDecimal transferenceValue, Transaction transaction) {
        Wallet walletSender = getWallet(sender.getWalletId());
        Wallet walletReceiver = getWallet(receiver.getWalletId());

        BigDecimal senderNewValue = this.getSenderNewValue(walletSender, transferenceValue);
        BigDecimal receiverNewValue = this.getReceiverNewValue(walletReceiver, transferenceValue);

        transaction.setOldValueSender(walletSender.getTotal());
        transaction.setOldValueReceiver(walletReceiver.getTotal());

        walletSender.setTotal(senderNewValue);
        walletReceiver.setTotal(receiverNewValue);

        transaction.setReceiver(receiver);
        transaction.setSender(sender);
        transaction.setNewValueReceiver(receiverNewValue);
        transaction.setNewValueSender(senderNewValue);
        transaction.setValue(transferenceValue);

        this.userRepository.saveAllAndFlush(List.of(sender, receiver));
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
