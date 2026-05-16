package com.api.bulletproof.controller;

import com.api.bulletproof.dto.SendMoneyDTO;
import com.api.bulletproof.service.SendMoneyOrchestrator;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bulletproof")
class MainController {

    private final SendMoneyOrchestrator sendMoneyOrchestrator;

    MainController(SendMoneyOrchestrator sendMoneyOrchestrator) {
        this.sendMoneyOrchestrator = sendMoneyOrchestrator;
    }

    @GetMapping("/health")
    void healthCheck() {
        System.out.print("to vivo");
    }

    @PostMapping
    void sendMoney(@RequestBody SendMoneyDTO input) {
        this.sendMoneyOrchestrator.execute(input);
    }

}

// - 1 criar entidades de transação -> feito
// - 2 implementar idepotencia -> feito
// - 3 estudar lock banco
// - 4 cache -> feito
// - 5 circuitbreaker
// - 6 fallback
// - 7 fila de requisições
