package com.educamais.app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;


@Table(name = "alunos")
@Entity(name = "alunos")
public class Aluno extends User{
    private String nome;
    private String matricula;
}
