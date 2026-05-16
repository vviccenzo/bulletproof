package com.api.bulletproof.service;

import com.api.bulletproof.entity.Transaction;
import com.api.bulletproof.repository.TransactionRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TransactionService {

    private final TransactionRepository transactionRepository;

    TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void save(Transaction transaction) {
        this.transactionRepository.save(transaction);
    }
}
