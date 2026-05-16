package com.api.bulletproof.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "total", nullable = false)
    private BigDecimal total = BigDecimal.ZERO;

    @Version
    private Long version;

    @Column(name = "owner_id", nullable = false)
    private UUID ownerId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public UUID getOwner() {
        return ownerId;
    }

    public void setOwner(UUID owner) {
        this.ownerId = owner;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
