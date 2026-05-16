package com.api.bulletproof.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record SendMoneyDTO(UUID receiver, UUID sender, BigDecimal value) {

    public String buildIdempotencyKey() {
        StringBuilder key = new StringBuilder();
        key.append(this.receiver);
        key.append(" - ");
        key.append(this.sender);
        key.append(" - ");
        key.append("SENDING_MONEY");

        return key.toString();
    }
}
