package com.educamais.app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Table(name = "alunos")
@Entity
public class Aluno extends User{
    @Column(unique = true)
    private String matricula;

    @ManyToOne
    @JoinColumn(name = "turma_id")
    private Turma turma;

    public Aluno(){
        super();
    }

    public Aluno(String matricula, Turma turma){
        this.matricula = matricula;
        this.turma = turma;
    }

    public String getMatricula(){
        return matricula;
    }

    public void setMatricula(String matricula){
        this.matricula = matricula;
    }

    public Turma getTurma(){
        return turma;
    }

    public void setTurma(Turma turma){
        this.turma = turma;
    }
}
