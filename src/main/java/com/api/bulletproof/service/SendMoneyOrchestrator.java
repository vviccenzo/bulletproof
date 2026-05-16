package com.api.bulletproof.service;

import com.api.bulletproof.dto.SendMoneyDTO;
import com.api.bulletproof.entity.Transaction;
import com.api.bulletproof.entity.TransactionStatus;
import com.api.bulletproof.entity.User;
import com.api.bulletproof.repository.TransactionRepository;
import com.api.bulletproof.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
public class SendMoneyOrchestrator {

    private final UserRepository userRepository;
    private final SendMoneyService sendMoneyService;
    private final StringRedisTemplate redisTemplate;
    private final SendMoneyValidate sendMoneyValidate;
    private final TransactionRepository transactionRepository;

    SendMoneyOrchestrator(UserRepository userRepository, SendMoneyValidate sendMoneyValidate, SendMoneyService sendMoneyService, StringRedisTemplate redisTemplate,
                          TransactionRepository transactionRepository) {
        this.redisTemplate = redisTemplate;
        this.userRepository = userRepository;
        this.sendMoneyService = sendMoneyService;
        this.sendMoneyValidate = sendMoneyValidate;
        this.transactionRepository = transactionRepository;
    }

    @Transactional(rollbackOn = Exception.class)
    public void execute(SendMoneyDTO dto) {
        String idempotency = dto.buildIdempotencyKey();
        Boolean isNew = redisTemplate.opsForValue().setIfAbsent(idempotency, "processing", Duration.ofMinutes(2));
        if (Boolean.FALSE.equals(isNew)) {
            throw new RuntimeException("Transaction in proccessing status.");
        }

        Transaction transaction = new Transaction();

        try {
            User sender = this.findUser(dto.sender());
            this.sendMoneyValidate.validateSender(sender, dto.value());

            User receiver = this.findUser(dto.receiver());
            this.sendMoneyService.transferMoney(sender, receiver, dto.value(), transaction);

            transaction.setStatus(TransactionStatus.FINISHED);
            this.transactionRepository.save(transaction);
        } catch(Exception e) {
            transaction.setStatus(TransactionStatus.CANCELED);
            this.transactionRepository.save(transaction);
            throw e;
        }
    }

    private User findUser(UUID id) {
        return this.userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado, com ID: " + id));
    }
}
