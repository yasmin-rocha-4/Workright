package com.seuprojeto.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Historico {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Usuario usuario;

    @ManyToOne
    private Tarefa tarefa;

    private LocalDate data;
    private int totalHoras;
}