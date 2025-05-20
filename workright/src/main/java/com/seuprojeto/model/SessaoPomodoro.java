package com.seuprojeto.model;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
public class SessaoPomodoro {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int duracao;
    private int pausas;
    private int ciclos;
    private LocalTime horaInicio;
    private String status;

    @ManyToOne
    private Tarefa tarefa;
}