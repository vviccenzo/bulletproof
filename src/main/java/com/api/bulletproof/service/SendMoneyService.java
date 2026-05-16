package com.api.bulletproof.service;

import com.api.bulletproof.entity.Transaction;
import com.api.bulletproof.entity.User;
import com.api.bulletproof.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class SendMoneyService {

    private final UserRepository userRepository;

    SendMoneyService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void transferMoney(User sender, User receiver, BigDecimal transferenceValue, Transaction transaction) {
        BigDecimal senderNewValue = this.getSenderNewValue(sender, transferenceValue);
        BigDecimal receiverNewValue = this.getReceiverNewValue(receiver, transferenceValue);

        transaction.setOldValueSender(sender.getTotal());
        transaction.setOldValueReceiver(receiver.getTotal());

        sender.setTotal(senderNewValue);
        receiver.setTotal(receiverNewValue);

        transaction.setReceiver(receiver);
        transaction.setSender(sender);
        transaction.setNewValueReceiver(receiverNewValue);
        transaction.setNewValueSender(senderNewValue);
        transaction.setValue(transferenceValue);

        this.userRepository.saveAllAndFlush(List.of(sender, receiver));
    }

    private BigDecimal getSenderNewValue(User sender, BigDecimal transferenceValue) {
        return sender.getTotal().subtract(transferenceValue);
    }

    private BigDecimal getReceiverNewValue(User receiver, BigDecimal transferenceValue) {
        return receiver.getTotal().add(transferenceValue);
    }

}
