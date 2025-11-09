package com.educamais.app.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.educamais.app.model.Simulado;

@Repository
public interface SimuladoRepository extends JpaRepository<Simulado, Long>{
    List<Simulado> findByTurmaId(Long turmaId);
    List<Simulado> findByProfessorId(UUID professorId);
}
