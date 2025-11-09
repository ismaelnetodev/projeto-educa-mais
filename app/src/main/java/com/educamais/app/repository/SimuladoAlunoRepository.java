package com.educamais.app.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.educamais.app.model.SimuladoAluno;

@Repository
public interface SimuladoAlunoRepository extends JpaRepository<SimuladoAluno, Long>{
    List<SimuladoAluno> findByAlunoId(UUID alunoId);
    List<SimuladoAluno> findBySimuladoId(Long simuladoId);

    boolean existsByAlunoIdAndSimuladoId(UUID alunoId, Long simuladoId);
}
