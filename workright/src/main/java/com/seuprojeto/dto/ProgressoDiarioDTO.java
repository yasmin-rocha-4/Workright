package com.seuprojeto.dto;

import java.time.LocalDate;

public record ProgressoDiarioDTO(
    LocalDate data,
    int totalHoras,
    boolean metaAtingida
) {}