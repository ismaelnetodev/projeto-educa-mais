package com.educamais.app.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.educamais.app.dtos.RespostaSimuladoDTO;
import com.educamais.app.dtos.SimuladoAlunoResponseDTO;
import com.educamais.app.dtos.SimuladoGerarDTO;
import com.educamais.app.dtos.SimuladoResponseDTO;
import com.educamais.app.dtos.SimuladoSubmeterDTO;
import com.educamais.app.model.Aluno;
import com.educamais.app.model.Professor;
import com.educamais.app.model.Questao;
import com.educamais.app.model.Simulado;
import com.educamais.app.model.SimuladoAluno;
import com.educamais.app.model.Turma;
import com.educamais.app.repository.AlunoRepository;
import com.educamais.app.repository.ProfessorRepository;
import com.educamais.app.repository.QuestaoRepository;
import com.educamais.app.repository.SimuladoAlunoRepository;
import com.educamais.app.repository.SimuladoRepository;
import com.educamais.app.repository.TurmaRepository;

@Service
public class SimuladoService {
    private final SimuladoRepository simuladoRepository;
    private final SimuladoAlunoRepository simuladoAlunoRepository;
    private final QuestaoRepository questaoRepository;
    private final ProfessorRepository professorRepository;
    private final AlunoRepository alunoRepository;
    private final TurmaRepository turmaRepository;

    public SimuladoService(SimuladoRepository simuladoRepository,
                           SimuladoAlunoRepository simuladoAlunoRepository,
                           QuestaoRepository questaoRepository,
                           ProfessorRepository professorRepository,
                           AlunoRepository alunoRepository,
                           TurmaRepository turmaRepository) {
        this.simuladoRepository = simuladoRepository;
        this.simuladoAlunoRepository = simuladoAlunoRepository;
        this.questaoRepository = questaoRepository;
        this.professorRepository = professorRepository;
        this.alunoRepository = alunoRepository;
        this.turmaRepository = turmaRepository;
    }

    @Transactional
    public SimuladoResponseDTO gerarSimuladoAleatorio(SimuladoGerarDTO data){
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        Professor professor = (Professor) professorRepository.findByLogin(login);
        if (professor == null) throw new RuntimeException("Professor não autenticado");

        Turma turma = turmaRepository.findById(data.turmaId())
            .orElseThrow(() -> new RuntimeException("Turma não encontrada."));

        List<Questao> bancoDeQuestoes = questaoRepository.findByDisciplina(data.disciplina());

        if (bancoDeQuestoes.size() < data.numeroQuestoes()){
            throw new RuntimeException("Não há questões suficientes de " + data.disciplina() + " pra gerar o simulado.");
        }

        Collections.shuffle(bancoDeQuestoes);
        List<Questao> questoesSelecionadas = bancoDeQuestoes.subList(0, data.numeroQuestoes());

        Simulado novoSimulado = new Simulado();
        novoSimulado.setTitulo(data.titulo());
        novoSimulado.setDataCriacao(LocalDateTime.now());
        novoSimulado.setProfessor(professor);
        novoSimulado.setTurma(turma);
        novoSimulado.setQuestoes(questoesSelecionadas);

        Simulado simuladoSalvo = simuladoRepository.save(novoSimulado);

        return new SimuladoResponseDTO(simuladoSalvo);

    }

    @Transactional(readOnly = true)
    public List<SimuladoResponseDTO> getSimuladoParaAluno(){
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        Aluno aluno = (Aluno) alunoRepository.findByLogin(login);
        if (aluno == null) throw new RuntimeException("Aluno não autenticado.");

        List<Simulado> simulados = simuladoRepository.findByTurmaId(aluno.getTurma().getId());

        return simulados.stream()
                .map(SimuladoResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SimuladoResponseDTO getSimuladoParaFazer(Long simuladoId) {
        Simulado simulado = simuladoRepository.findById(simuladoId)
            .orElseThrow(() -> new RuntimeException("Simulado não encontrado."));
        
        // (Lógica de segurança futura: verificar se o aluno é da turma)
        
        return new SimuladoResponseDTO(simulado);
    }

    @Transactional
    public SimuladoAlunoResponseDTO submeterSimulado(Long simuladoId, SimuladoSubmeterDTO data){
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        Aluno aluno = (Aluno) alunoRepository.findByLogin(login);
        if (aluno == null) throw new RuntimeException("Aluno não autenticado.");

        Simulado simulado = simuladoRepository.findById(simuladoId).orElseThrow(() -> new RuntimeException("Nenhum simulado encontrado."));

        boolean jaSubmeteu = simuladoAlunoRepository.existsByAlunoIdAndSimuladoId(aluno.getId(), simuladoId);

        if (jaSubmeteu){
            throw new RuntimeException("Este simulado já foi submetido por este aluno.");
        }

        if (!simulado.getTurma().getId().equals(aluno.getTurma().getId())) {
            throw new RuntimeException("Acesso Negado: Este simulado não pertence à sua turma.");
        }

        double notaFinal = 0.0;

        Map<Long, String> repostasMap = data.respostas().stream()
                .collect(Collectors.toMap(RespostaSimuladoDTO::questaoId, RespostaSimuladoDTO::respostaDada));

        for (Questao questao : simulado.getQuestoes()){
            String respostaAluno = repostasMap.get(questao.getId());
            
            if (respostaAluno != null && respostaAluno.equals(questao.getRespostaCorreta())){
                notaFinal += questao.getPontuacao();
            }
        }

        SimuladoAluno simuladoAluno = new SimuladoAluno();
        simuladoAluno.setAluno(aluno);
        simuladoAluno.setDataSubmissao(LocalDateTime.now());
        simuladoAluno.setNotaFinal(notaFinal);
        simuladoAluno.setRespostas(repostasMap);
        simuladoAluno.setSimulado(simulado);

        SimuladoAluno resultadoSalvo = simuladoAlunoRepository.save(simuladoAluno);

        return new SimuladoAlunoResponseDTO(resultadoSalvo);
    }

    @Transactional(readOnly = true)
    public List<SimuladoResponseDTO> getSimuladosDoProfessorLogado(){
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        Professor professor = (Professor) professorRepository.findByLogin(login);

        if (professor == null) throw new RuntimeException("Professor não autenticado");

        List<Simulado> simulados = simuladoRepository.findByProfessorId(professor.getId());    
        return simulados.stream()
                .map(SimuladoResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SimuladoAlunoResponseDTO> getResultadosDoSimulado(Long simuladoId){
        if (!simuladoRepository.existsById(simuladoId)) throw new RuntimeException("Simulado não encontrado");

        List<SimuladoAluno> resultados = simuladoAlunoRepository.findBySimuladoId(simuladoId);

        return resultados.stream()
                .map(SimuladoAlunoResponseDTO::new)
                .collect(Collectors.toList());
    }
}
