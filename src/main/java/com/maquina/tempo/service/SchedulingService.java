package com.maquina.tempo.service;

import com.maquina.tempo.dto.SchedulingDTO;
import com.maquina.tempo.entities.Scheduling;
import com.maquina.tempo.entities.User;
import com.maquina.tempo.repository.SchedulingRepository;
import com.maquina.tempo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SchedulingService {

    @Autowired
    private SchedulingRepository schedulingRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity schedulingSend(SchedulingDTO schedulingDTO, Long idUser) {

        User user = userRepository.findById(idUser).orElse(null);

        if (user != null) {
            return ResponseEntity.notFound().build();
        }

        if (schedulingDTO.date().isBefore(LocalDate.now())) {
            return ResponseEntity.badRequest().build();
        }

        Scheduling scheduling = fromDTO(schedulingDTO);
        scheduling.setUser(user);

        schedulingRepository.save(scheduling);
        return ResponseEntity.ok().build();
    }

    @Scheduled(fixedRate = 86400000) // 24H
    public void processScheduling() {

        List<Scheduling> schedulings = schedulingRepository.findBySchedulingTimeToday();

        if (schedulings.isEmpty()) {
            return;
        }
        for (Scheduling scheduling : schedulings) {
            emailService.sendEmailScheduling(scheduling.getToAddress(), scheduling.getUser().getName(), scheduling.getMensage());
            scheduling.setSend(true);
            schedulingRepository.save(scheduling);
        }
    }

    public Scheduling fromDTO(SchedulingDTO schedulingDTO) {
        Scheduling scheduling = new Scheduling();
        scheduling.setMensage(schedulingDTO.mensage());
        scheduling.setFromAddress(schedulingDTO.fromAddress());
        scheduling.setDate(schedulingDTO.date());
        scheduling.setMensage(schedulingDTO.mensage());
        return scheduling;
    }
}