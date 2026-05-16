package com.api.bulletproof.service;

import com.api.bulletproof.dto.SendMoneyDTO;
import com.api.bulletproof.entity.User;
import com.api.bulletproof.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SendMoneyOrchestrator {

    private final UserRepository userRepository;
    private final SendMoneyService sendMoneyService;
    private final SendMoneyValidate sendMoneyValidate;

    SendMoneyOrchestrator(UserRepository userRepository, SendMoneyValidate sendMoneyValidate, SendMoneyService sendMoneyService) {
        this.userRepository = userRepository;
        this.sendMoneyService = sendMoneyService;
        this.sendMoneyValidate = sendMoneyValidate;
    }

    @Transactional(rollbackOn = Exception.class)
    public void execute(SendMoneyDTO dto) {
        User sender = this.findUser(dto.sender());
        this.sendMoneyValidate.validateSender(sender, dto.value());

        User receiver = this.findUser(dto.receiver());
        this.sendMoneyService.transferMoney(sender, receiver, dto.value());
    }

    private User findUser(UUID id) {
        return this.userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado, com ID: " + id));
    }

}
