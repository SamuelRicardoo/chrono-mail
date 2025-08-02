package com.maquina.tempo.service;

import com.maquina.tempo.dto.UserDTO;
import com.maquina.tempo.dto.UserLogin;
import com.maquina.tempo.entities.User;
import com.maquina.tempo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    //Cadastro USer
    public ResponseEntity saveUser(UserDTO userDTO) {

        if(!userExists(userDTO.email())){
            User user = fromDTO(userDTO);
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.OK).body("Created");
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("User Exist");
    }

    //Login de usuario
    public ResponseEntity loginUser(UserLogin login) {

        if(userExists(login.email())){
            Optional<User> userOptional = userRepository.findByEmail(login.email());
            User user = userOptional.orElse(null);
            if(login.password().equals(user.getPassword())){
                return ResponseEntity.status(HttpStatus.OK).body(user);
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Password Mismatch");
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("User dont Exist");
    }


    //Verificação
    public boolean userExists(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return true;
        }
        return false;
    }

    private User fromDTO(UserDTO userDTO) {
        return new User(userDTO.name(),userDTO.email(),userDTO.password());
    }
}