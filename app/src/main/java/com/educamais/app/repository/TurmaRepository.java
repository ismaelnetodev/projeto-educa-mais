package com.educamais.app.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.educamais.app.model.Turma;

@Repository
public interface TurmaRepository extends JpaRepository<Turma, Long>{

    boolean existsByNomeAndAnoLetivo(String nome, int anoLetivo);

    @Query("SELECT a FROM Turma a WHERE UPPER(a.nome) LIKE UPPER(CONCAT(:termoBusca, '%'))")
    List<Turma> buscaRapidaPorNome(@Param("termoBusca") String termoBusca, Pageable pageable);

}
