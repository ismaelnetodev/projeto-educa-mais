package com.educamais.app.services;

import java.util.List;
import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.educamais.app.dtos.AlunoPerfilDTO;
import com.educamais.app.model.Aluno;
import com.educamais.app.model.AvaliacaoQualitativa;
import com.educamais.app.model.SimuladoAluno;
import com.educamais.app.repository.AlunoRepository;
import com.educamais.app.repository.AvaliacaoQualitativaRepository;
import com.educamais.app.repository.SimuladoAlunoRepository;


@Service
public class PerfilService {
    private final AlunoRepository alunoRepository;
    private final AvaliacaoQualitativaRepository avaliacaoQualitativaRepository;
    private final SimuladoAlunoRepository simuladoAlunoRepository;
    
    public PerfilService(AlunoRepository alunoRepository, AvaliacaoQualitativaRepository avaliacaoQualitativaRepository, SimuladoAlunoRepository simuladoAlunoRepository){
        this.alunoRepository = alunoRepository;
        this.avaliacaoQualitativaRepository = avaliacaoQualitativaRepository;
        this.simuladoAlunoRepository = simuladoAlunoRepository;
    }

    @Transactional(readOnly = true)
    public AlunoPerfilDTO getMeuPerfil(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Aluno aluno = alunoRepository.findByMatricula(username).orElseThrow(() -> new RuntimeException("Aluno não encontrado."));

        UUID alunoId = aluno.getId();

        List<AvaliacaoQualitativa> avaliacoes = avaliacaoQualitativaRepository.findByAlunoIdOrderByDataAvaliacaoDesc(alunoId);
        List<SimuladoAluno> simulados = simuladoAlunoRepository.findByAlunoId(alunoId);

        return new AlunoPerfilDTO(aluno, avaliacoes, simulados);
        
    }

}
