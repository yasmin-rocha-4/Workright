package com.seuprojeto.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public int getDuracao() { return duracao; }
    public void setDuracao(int duracao) { this.duracao = duracao; }
    public int getPausas() { return pausas; }
    public void setPausas(int pausas) { this.pausas = pausas; }
    public int getCiclos() { return ciclos; }
    public void setCiclos(int ciclos) { this.ciclos = ciclos; }
    public LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Tarefa getTarefa() { return tarefa; }
    public void setTarefa(Tarefa tarefa) { this.tarefa = tarefa; }
}
