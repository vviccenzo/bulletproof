package com.api.bulletproof.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record SendMoneyDTO(UUID receiver, UUID sender, BigDecimal value) {
}
