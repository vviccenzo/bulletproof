package com.api.bulletproof.repository;

import com.api.bulletproof.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    @Query("""
                SELECT CASE
                    WHEN w.total >= :value THEN true
                    ELSE false
                END
                FROM Wallet w
                WHERE w.ownerId = :userId
            """)
    boolean hasEnoughBalanceForTransaction(UUID userId, BigDecimal value);
}
