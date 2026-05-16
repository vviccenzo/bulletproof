package com.api.bulletproof.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private BigDecimal value;

    private BigDecimal oldValueSender;

    private BigDecimal oldValueReceiver;

    private BigDecimal newValueSender;

    private BigDecimal newValueReceiver;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    private LocalDateTime createdAt;

    @Enumerated(value = EnumType.STRING)
    private TransactionStatus status = TransactionStatus.PROCESSING;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = TransactionStatus.PROCESSING;
        }
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BigDecimal getOldValueSender() {
        return oldValueSender;
    }

    public void setOldValueSender(BigDecimal oldValueSender) {
        this.oldValueSender = oldValueSender;
    }

    public BigDecimal getOldValueReceiver() {
        return oldValueReceiver;
    }

    public void setOldValueReceiver(BigDecimal oldValueReceiver) {
        this.oldValueReceiver = oldValueReceiver;
    }

    public BigDecimal getNewValueSender() {
        return newValueSender;
    }

    public void setNewValueSender(BigDecimal newValueSender) {
        this.newValueSender = newValueSender;
    }

    public BigDecimal getNewValueReceiver() {
        return newValueReceiver;
    }

    public void setNewValueReceiver(BigDecimal newValueReceiver) {
        this.newValueReceiver = newValueReceiver;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }
}
