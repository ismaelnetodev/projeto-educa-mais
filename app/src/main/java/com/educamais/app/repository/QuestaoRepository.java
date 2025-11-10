package com.educamais.app.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.educamais.app.model.Disciplina;
import com.educamais.app.model.Professor;
import com.educamais.app.model.Questao;

@Repository
public interface QuestaoRepository extends JpaRepository<Questao, Long> {
    List<Questao> findByDisciplinaId(Long disciplinaId);
    List<Questao> findByDisciplinaAndProfessorCriador(Disciplina disciplina, Professor professor);
}
