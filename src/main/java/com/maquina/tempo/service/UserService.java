package com.maquina.tempo.service;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.maquina.tempo.dto.RecoverUserDTO;
import com.maquina.tempo.dto.UserDTO;
import com.maquina.tempo.dto.UserLogin;
import com.maquina.tempo.entities.User;
import com.maquina.tempo.entities.VerificationToken;
import com.maquina.tempo.repository.TokenRepository;
import com.maquina.tempo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TokenService tokenService;

    @Autowired
    TokenRepository tokenRepository;

    //Cadastro USer
    @Transactional
    public ResponseEntity saveUser(UserDTO userDTO) {

        if(!userExists(userDTO.email())){
            User user = fromDTO(userDTO);
            userRepository.save(user);
            tokenService.registerToken(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("Created");
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("User Exist");
    }

    //Login de usuario
    public ResponseEntity loginUser(UserLogin login) {

        if(userExists(login.email())){
            Optional<User> userOptional = userRepository.findByEmail(login.email());
            User user = userOptional.orElse(null);
            if(login.password().equals(user.getPassword()) && user.isActive()){
                return ResponseEntity.status(HttpStatus.OK).body(user);
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Password Mismatch or Account is not Active");
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("User dont Exist");
    }

    @Transactional
    public ResponseEntity recoverUser(RecoverUserDTO recoverUserDTO, UUID token) {

        Optional<User> recoverUser = userRepository.findUserByToken(token);

        if(!recoverUser.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        User user = recoverUser.get();

        if(recoverUserDTO.newPassword().equals(recoverUserDTO.confirmPassword())){
            user.setPassword(recoverUserDTO.newPassword());
            userRepository.save(user);
        }
        return ResponseEntity.status(HttpStatus.OK).body(user);
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