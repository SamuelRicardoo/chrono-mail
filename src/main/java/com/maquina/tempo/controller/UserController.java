package com.maquina.tempo.controller;

import com.maquina.tempo.dto.RecoverUserDTO;
import com.maquina.tempo.dto.UserDTO;
import com.maquina.tempo.dto.UserLogin;
import com.maquina.tempo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO user, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result);
        }
        ResponseEntity responseUser = userService.saveUser(user);
        return ResponseEntity.ok(responseUser);
    }

    @PostMapping("/login")
    ResponseEntity<?> login(@Valid @RequestBody UserLogin user, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result);
        }
        ResponseEntity responseUser = userService.loginUser(user);
        return ResponseEntity.ok(responseUser);
    }

    @PutMapping("/recover")
    ResponseEntity<?> recoverUser(@Valid @RequestBody RecoverUserDTO user, @Valid @RequestParam UUID token, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result);
        }
        ResponseEntity responseUser = userService.recoverUser(user, token);
        return ResponseEntity.ok(responseUser);
    }
}
