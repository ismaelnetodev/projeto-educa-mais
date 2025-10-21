package com.educamais.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.educamais.app.model.Turma;

public interface TurmaRepository extends JpaRepository<Turma, Long>{

    boolean existsByNomeAndAnoLetivo(String nome, int anoLetivo);

}
