package com.maquina.tempo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class MaquinaTempoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MaquinaTempoApplication.class, args);
    }

}
