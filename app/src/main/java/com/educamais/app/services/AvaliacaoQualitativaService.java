package com.educamais.app.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.educamais.app.dtos.AvaliacaoCadastroDTO;
import com.educamais.app.model.Aluno;
import com.educamais.app.model.AvaliacaoQualitativa;
import com.educamais.app.model.Professor;
import com.educamais.app.repository.AlunoRepository;
import com.educamais.app.repository.AvaliacaoQualitativaRepository;
import com.educamais.app.repository.ProfessorRepository;


@Service
public class AvaliacaoQualitativaService {
    private final AvaliacaoQualitativaRepository avaliacaoQualitativaRepository;
    private final AlunoRepository alunoRepository;
    private final ProfessorRepository professorRepository;

    public AvaliacaoQualitativaService(
        AvaliacaoQualitativaRepository avaliacaoQualitativaRepository,
        AlunoRepository alunoRepository,
        ProfessorRepository professorRepository
    ){
        this.avaliacaoQualitativaRepository = avaliacaoQualitativaRepository;
        this.alunoRepository = alunoRepository;
        this.professorRepository = professorRepository;
    }


    public AvaliacaoQualitativa registrarAvaliacao(AvaliacaoCadastroDTO data){
        String login = SecurityContextHolder.getContext().getAuthentication().getName();

        Professor professor = (Professor) professorRepository.findByLogin(login);

        if (professor == null) throw new RuntimeException("Professor não autenticado");

        Aluno aluno = alunoRepository.findById(data.alunoId()).orElseThrow(() -> new RuntimeException("Aluno não encontrado."));

        AvaliacaoQualitativa novaAvaliacao = new AvaliacaoQualitativa();
        novaAvaliacao.setAluno(aluno);
        novaAvaliacao.setProfessor(professor);
        novaAvaliacao.setDataAvaliacao(LocalDateTime.now());
        novaAvaliacao.setAssiduidade(data.assiduidade());
        novaAvaliacao.setParticipacao(data.participacao());
        novaAvaliacao.setResponsabilidade(data.responsabilidade());
        novaAvaliacao.setSociabilidade(data.sociabilidade());
        novaAvaliacao.setObservacao(data.observacao());

        return avaliacaoQualitativaRepository.save(novaAvaliacao);
    }

    @Transactional(readOnly = true)
    public List<AvaliacaoQualitativa> getHistoricoDoAluno(UUID alunoId){
        if (!alunoRepository.existsById(alunoId)) throw new RuntimeException("Aluno não encontrado.");

        return avaliacaoQualitativaRepository.findByAlunoIdOrderByDataAvaliacaoDesc(alunoId);
    }

}
