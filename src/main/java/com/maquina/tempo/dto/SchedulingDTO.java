package com.maquina.tempo.dto;

import java.time.LocalDate;

public record SchedulingDTO(String toAddress, String fromAddress, LocalDate date, String mensage) {}