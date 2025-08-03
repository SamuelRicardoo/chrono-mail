package com.maquina.tempo.service;

import com.maquina.tempo.dto.UserDTO;
import com.maquina.tempo.entities.User;
import com.maquina.tempo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserServiceTest {

        @InjectMocks
        private UserService userService;

        @Mock
        private UserRepository userRepository;

        @Test
        void saveUserWhereNotExist() {
            UserDTO dto = new UserDTO("Samuel", "dssdsdsdsdw@email.com", "123");
            Mockito.when(userRepository.findByEmail(dto.email())).thenReturn(Optional.empty());
            ResponseEntity response = userService.saveUser(dto);
            assertEquals("Created", response.getBody());
            Mockito.verify(userRepository).save(Mockito.any(User.class));
        }

        @Test
        void dontSaveUserWhereExist() {

            UserDTO dto = new UserDTO("Samuel", "dssdsdsdsdw@email.com", "123");
            Mockito.when(userRepository.findByEmail(dto.email())).thenReturn(Optional.of(new User("Exist", dto.email(), "123")));

            ResponseEntity response = userService.saveUser(dto);

            assertEquals("User Exist", response.getBody());
            Mockito.verify(userRepository, Mockito.never()).save(Mockito.any());
        }

}
