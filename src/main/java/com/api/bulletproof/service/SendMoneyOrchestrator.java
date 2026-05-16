package com.api.bulletproof.service;

import com.api.bulletproof.dto.SendMoneyDTO;
import com.api.bulletproof.entity.Transaction;
import com.api.bulletproof.entity.TransactionStatus;
import com.api.bulletproof.entity.User;
import com.api.bulletproof.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class SendMoneyOrchestrator {

    private final UserService userService;
    private final SendMoneyService sendMoneyService;
    private final StringRedisTemplate redisTemplate;
    private final SendMoneyValidate sendMoneyValidate;
    private final TransactionRepository transactionRepository;

    SendMoneyOrchestrator(UserService userService, SendMoneyValidate sendMoneyValidate, SendMoneyService sendMoneyService, StringRedisTemplate redisTemplate, TransactionRepository transactionRepository) {
        this.redisTemplate = redisTemplate;
        this.userService = userService;
        this.sendMoneyService = sendMoneyService;
        this.sendMoneyValidate = sendMoneyValidate;
        this.transactionRepository = transactionRepository;
    }

    @Transactional(rollbackOn = Exception.class, value = Transactional.TxType.REQUIRES_NEW)
    public void execute(SendMoneyDTO dto) {
        String idempotency = dto.buildIdempotencyKey();
        Boolean isNew = redisTemplate.opsForValue().setIfAbsent(idempotency, "processing", Duration.ofMinutes(2));
        if (Boolean.FALSE.equals(isNew)) {
            throw new RuntimeException("Transaction in proccessing status.");
        }

        Transaction transaction = new Transaction();

        try {
            User sender = this.userService.findUser(dto.sender());
            this.sendMoneyValidate.validateSender(sender, dto.value());

            User receiver = this.userService.findUser(dto.receiver());
            this.sendMoneyService.transferMoney(sender, receiver, dto.value(), transaction);

            transaction.setStatus(TransactionStatus.FINISHED);
            this.transactionRepository.save(transaction);
        } catch (Exception e) {
            transaction.setStatus(TransactionStatus.CANCELED);
            this.transactionRepository.save(transaction);
            throw e;
        } finally {
            redisTemplate.delete(idempotency);
        }
    }
}
