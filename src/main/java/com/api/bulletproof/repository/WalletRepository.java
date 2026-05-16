package com.api.bulletproof.repository;

import com.api.bulletproof.entity.Wallet;
import jakarta.persistence.LockModeType;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, UUID> {

    @Override
    @Lock(value = LockModeType.PESSIMISTIC_READ)
    Optional<Wallet> findById(@NonNull UUID id);
}
