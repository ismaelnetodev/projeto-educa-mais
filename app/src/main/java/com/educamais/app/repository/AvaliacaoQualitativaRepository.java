package com.educamais.app.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.educamais.app.model.AvaliacaoQualitativa;

public interface AvaliacaoQualitativaRepository extends JpaRepository<AvaliacaoQualitativa, Long>{
    List<AvaliacaoQualitativa> findByAlunoIdOrderByDataAvaliacaoDesc(UUID alunoId);
}
