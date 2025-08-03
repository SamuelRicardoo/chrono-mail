package com.maquina.tempo.service;

import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class EmailServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    EmailService emailService;

    @Captor
    ArgumentCaptor<SimpleMailMessage> simpleMailMessage;

    @Test
    void sendEmailTest(){
        emailService.sendEmail("destinatario@email.com", "Assunto", "Corpo do e-mail");

        Mockito.verify(javaMailSender).send(simpleMailMessage.capture());

        SimpleMailMessage message = simpleMailMessage.getValue();
        assertEquals("Assunto", message.getSubject());
        assertEquals("destinatario@email.com", message.getTo()[0]);
        assertEquals("Corpo do e-mail", message.getText());
    }
}