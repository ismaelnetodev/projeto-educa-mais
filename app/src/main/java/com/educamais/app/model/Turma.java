package com.educamais.app.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "turmas")
public class Turma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private int anoLetivo;

    @OneToMany(mappedBy = "turma")
    private List<Aluno> alunos;

    public Turma(){}

    public Turma(String nome, int anoLetivo){
        this.nome = nome;
        this.anoLetivo = anoLetivo;
    }

    public String getNome(){
        return nome;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public int getAnoLetivo(){
        return anoLetivo;
    }

    public void setAnoLetivo(int anoLetivo){
        this.anoLetivo = anoLetivo;
    }

    public List<Aluno> getAlunos(){
        return alunos;
    }

    public void setAlunos(List<Aluno> alunos){
        this.alunos = alunos;
    }
    
}
