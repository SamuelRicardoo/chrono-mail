package com.maquina.tempo.service;

import com.maquina.tempo.entities.User;
import com.maquina.tempo.entities.VerificationToken;
import com.maquina.tempo.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TokenService {

    @Autowired
    VerificationTokenRepository verificationTokenRepository;

    //Registro de Token
    public void registerToken(User user) {
        VerificationToken token = new VerificationToken();
        token.setToken(UUID.randomUUID());
        token.setCreated(LocalDateTime.now());
        token.setExpiration(LocalDateTime.now().plusMinutes(30));
        token.setUser(user);
        verificationTokenRepository.save(token);
        user.addToken(token);
    }




}