package com.educamais.app.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import com.educamais.app.model.Aluno;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, UUID>{

    public UserDetails findByLogin(String login);

}
