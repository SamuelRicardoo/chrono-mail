package com.maquina.tempo.controller;

import com.maquina.tempo.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/activation")
public class EmailController {

    @Autowired
    TokenService tokenService;

    @GetMapping("/action")
    public ResponseEntity activation(@RequestParam UUID uuid) {
        ResponseEntity response = tokenService.activateToken(uuid);
        return ResponseEntity.ok(response);
    }

    @GetMapping("recoverPassword")
    public ResponseEntity recoverPassword(@RequestParam Long idUser) {
        ResponseEntity response = tokenService.sedEmailResetPassword(idUser);
        return ResponseEntity.ok(response);
    }
}
