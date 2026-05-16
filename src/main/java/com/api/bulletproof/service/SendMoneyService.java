package com.api.bulletproof.service;

import com.api.bulletproof.entity.Transaction;
import com.api.bulletproof.entity.User;
import com.api.bulletproof.repository.TransactionRepository;
import com.api.bulletproof.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class SendMoneyService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    SendMoneyService(TransactionRepository transactionRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    public void transferMoney(User sender, User receiver, BigDecimal transferenceValue) {
        BigDecimal senderNewValue = this.getSenderNewValue(sender, transferenceValue);
        BigDecimal receiverNewValue = this.getReceiverNewValue(receiver, transferenceValue);

        sender.setTotal(senderNewValue);
        receiver.setTotal(receiverNewValue);

        Transaction transaction = new Transaction();
        transaction.setReceiver(receiver);
        transaction.setSender(sender);
        transaction.setNewValueReceiver(receiverNewValue);
        transaction.setNewValueSender(senderNewValue);
        transaction.setValue(transferenceValue);

        this.transactionRepository.save(transaction);
        this.userRepository.saveAllAndFlush(List.of(sender, receiver));
    }

    private BigDecimal getSenderNewValue(User sender, BigDecimal transferenceValue) {
        return sender.getTotal().subtract(transferenceValue);
    }

    private BigDecimal getReceiverNewValue(User receiver, BigDecimal transferenceValue) {
        return receiver.getTotal().add(transferenceValue);
    }

}
