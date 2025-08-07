package com.maquina.tempo.service;

import com.maquina.tempo.entities.User;
import com.maquina.tempo.entities.VerificationToken;
import com.maquina.tempo.repository.UserRepository;
import com.maquina.tempo.repository.TokenRepository;
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
    TokenRepository verificationTokenRepository;

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

        String activationLink = "http://localhost:8080/activation/action?uuid=" + token.getToken();

        String htmlMessage = "<p>Olá, " + user.getName() + "!</p>"
                + "<p>Clique no link abaixo para ativar sua conta. O link é válido por 30 minutos:</p>"
                + "<p><a href=\"" + activationLink + "\">Clique aqui</a></p>";

        emailService.sendEmail(
                user.getEmail(),
                "Ativação de conta",
                htmlMessage);
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

    public ResponseEntity sedEmailResetPassword(Long userID) {
        Optional<User> user = userRepository.findById(userID);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User userEmail =  user.get();

        VerificationToken token = new VerificationToken();
        token.setToken(UUID.randomUUID());
        token.setCreated(LocalDateTime.now());
        token.setExpiration(LocalDateTime.now().plusMinutes(30));
        token.setUser(userEmail);
        verificationTokenRepository.save(token);
        userEmail.addToken(token);

        String htmlMessage = "<p>Olá, " + userEmail.getName() + "!</p>"
                + "<p>Seu codigo de recuperação de conta. O Codigo é válido por 30 minutos:</p>"
                + "<p>" + token.getToken() + "</p>";

        emailService.sendEmail(userEmail.getEmail(), "Recuperação de conta", htmlMessage);

        return ResponseEntity.ok().build();
    }


}