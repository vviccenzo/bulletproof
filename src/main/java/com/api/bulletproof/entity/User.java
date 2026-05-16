package com.api.bulletproof.entity;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "wallet_id")
    private UUID walletId;

    @OneToMany(mappedBy = "receiver")
    private List<Transaction> received;

    @OneToMany(mappedBy = "sender")
    private List<Transaction> sent;

    public List<Transaction> getSent() {
        return sent;
    }

    public void setSent(List<Transaction> sent) {
        this.sent = sent;
    }

    public List<Transaction> getReceived() {
        return received;
    }

    public void setReceived(List<Transaction> received) {
        this.received = received;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getWalletId() {
        return walletId;
    }

    public void setWalletId(UUID walletId) {
        this.walletId = walletId;
    }
}
