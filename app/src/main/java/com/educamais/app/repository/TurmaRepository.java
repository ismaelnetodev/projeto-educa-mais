package com.educamais.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.educamais.app.model.Turma;

@Repository
public interface TurmaRepository extends JpaRepository<Turma, Long>{

    boolean existsByNomeAndAnoLetivo(String nome, int anoLetivo);

}
