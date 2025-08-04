package com.maquina.tempo.service;

import com.maquina.tempo.entities.User;
import com.maquina.tempo.entities.VerificationToken;
import com.maquina.tempo.repository.UserRepository;
import com.maquina.tempo.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class TokenService {

    @Autowired
    VerificationTokenRepository verificationTokenRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    UserRepository userRepository;

    //Registro de Token
    public void registerToken(User user) {
        VerificationToken token = new VerificationToken();
        token.setToken(UUID.randomUUID());
        token.setCreated(LocalDateTime.now());
        token.setExpiration(LocalDateTime.now().plusMinutes(30));
        token.setUser(user);
        verificationTokenRepository.save(token);
        user.addToken(token);

        emailService.sendEmail(
                user.getEmail(),
                "Ativação de conta",
                "Olá, " + user.getName() + "!\n\nClique no link abaixo para ativar sua conta. O link é válido por 30 minutos:\n\n" +
                        "http://localhost:8080/api/activate?token=" + token.getToken());

    }

    public ResponseEntity activateToken(UUID uuidUser) {

        Optional<VerificationToken> token = verificationTokenRepository.findByToken(uuidUser);

        if (token.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        VerificationToken verificationToken = token.get();

        if (verificationToken.getExpiration().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Token Invalid");
        }

        var user = verificationToken.getUser();
        user.setActive(true);
        userRepository.save(user);
        return ResponseEntity.ok().body("Account Activity");
    }






}