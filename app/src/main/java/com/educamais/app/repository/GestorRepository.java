package com.educamais.app.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.educamais.app.model.Gestor;

public interface GestorRepository extends JpaRepository<Gestor, UUID>{

    public UserDetails findByLogin(String login);
}
