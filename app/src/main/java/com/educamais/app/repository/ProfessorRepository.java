package com.educamais.app.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.educamais.app.model.Professor;

public interface ProfessorRepository extends JpaRepository<Professor, UUID>{
    public UserDetails findByLogin(String login);
}
