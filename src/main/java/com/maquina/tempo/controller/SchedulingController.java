package com.maquina.tempo.controller;

import com.maquina.tempo.dto.RecoverUserDTO;
import com.maquina.tempo.dto.SchedulingDTO;
import com.maquina.tempo.service.SchedulingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/scheduling")
public class SchedulingController {

    @Autowired
    private SchedulingService schedulingService;

    @PostMapping("/schedulingSend")
    public ResponseEntity<?> schedulingEmail(@Valid @RequestBody SchedulingDTO schedulingDTO,
                                             @Valid @RequestParam Long id, BindingResult result)
    {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result);
        }

        ResponseEntity response = schedulingService.schedulingSend(schedulingDTO, id);

        return ResponseEntity.ok().body(response);
    }




}