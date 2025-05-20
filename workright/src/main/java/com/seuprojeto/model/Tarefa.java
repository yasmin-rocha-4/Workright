package com.seuprojeto.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Tarefa {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String descricao;
    private String status;
    private LocalDate prazo;

    @ManyToOne
    private Usuario usuario;

    @OneToMany(mappedBy = "tarefa")
    private List<SessaoPomodoro> sessoes;
}