package com.api.bulletproof.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record SendMoneyDTO(@NotNull UUID receiver, @NotNull UUID sender, @NotNull @Min(0) BigDecimal value) {

    public String buildIdempotencyKey() {
        return this.receiver +
                " - " +
                this.sender +
                " - " +
                "SENDING_MONEY";
    }
}
