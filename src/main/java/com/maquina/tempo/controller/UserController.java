package com.maquina.tempo.controller;

import com.maquina.tempo.dto.UserDTO;
import com.maquina.tempo.dto.UserLogin;
import com.maquina.tempo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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


}
