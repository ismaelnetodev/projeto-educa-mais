package com.educamais.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.educamais.app.model.Aluno;

public interface AlunoRepository extends JpaRepository<Aluno, String>{
    Optional<Aluno> findByLogin(String login);
}
