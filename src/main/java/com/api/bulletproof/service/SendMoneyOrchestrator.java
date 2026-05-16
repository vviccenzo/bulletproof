package com.api.bulletproof.service;

import com.api.bulletproof.dto.SendMoneyDTO;
import com.api.bulletproof.entity.Transaction;
import com.api.bulletproof.entity.User;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
public class SendMoneyOrchestrator {

    private final UserService userService;
    private final SendMoneyService sendMoneyService;
    private final StringRedisTemplate redisTemplate;
    private final SendMoneyValidate sendMoneyValidate;
    private final TransactionService transactionService;

    SendMoneyOrchestrator(UserService userService, SendMoneyValidate sendMoneyValidate, SendMoneyService sendMoneyService, StringRedisTemplate redisTemplate, TransactionService transactionService) {
        this.redisTemplate = redisTemplate;
        this.userService = userService;
        this.sendMoneyService = sendMoneyService;
        this.sendMoneyValidate = sendMoneyValidate;
        this.transactionService = transactionService;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void execute(SendMoneyDTO dto) {
        String idempotency = dto.buildIdempotencyKey();
        validateIfTransactionIsInProcessing(idempotency);

        this.sendMoneyValidate.validateSender(dto);

        User sender = this.userService.findUser(dto.sender());
        User receiver = this.userService.findUser(dto.receiver());

        Transaction transaction = new Transaction(receiver, sender);

        try {
            this.sendMoneyService.transferMoney(sender.getWalletId(), receiver.getWalletId(), dto.value(), transaction);
            transaction.finish();
        } catch (Exception e) {
            transaction.cancel();
            throw e;
        } finally {
            this.transactionService.save(transaction);
            redisTemplate.delete(idempotency);
        }
    }

    private void validateIfTransactionIsInProcessing(String idempotency) {
        Boolean isNew = redisTemplate.opsForValue().setIfAbsent(idempotency, "processing", Duration.ofMinutes(2));
        if (Boolean.FALSE.equals(isNew)) {
            throw new RuntimeException("Transaction in proccessing status.");
        }
    }
}
