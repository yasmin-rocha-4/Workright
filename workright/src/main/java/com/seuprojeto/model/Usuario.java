package com.seuprojeto.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Usuario {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;
    private String senha;
    private String papel;

    @OneToMany(mappedBy = "usuario")
    private List<Tarefa> tarefas;
}