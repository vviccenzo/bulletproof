package com.api.bulletproof.service;

import com.api.bulletproof.entity.User;
import com.api.bulletproof.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findUser(UUID id) {
        return this.userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado, com ID: " + id));
    }
}
