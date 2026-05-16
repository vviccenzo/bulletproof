package com.api.bulletproof.service;

import com.api.bulletproof.entity.Wallet;
import com.api.bulletproof.repository.WalletRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class WalletService {

    private final WalletRepository walletRepository;

    WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public Wallet findById(UUID id) {
        return this.walletRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }
}
