package com.educamais.app.services;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.educamais.app.dtos.QuestaoCadastroDTO;
import com.educamais.app.dtos.QuestaoResponseDTO;
import com.educamais.app.model.Professor;
import com.educamais.app.model.Questao;
import com.educamais.app.repository.ProfessorRepository;
import com.educamais.app.repository.QuestaoRepository;

@Service
public class QuestaoService {
    private final QuestaoRepository questaoRepository;
    private final ProfessorRepository professorRepository;

    public QuestaoService(QuestaoRepository questaoRepository, ProfessorRepository professorRepository){
        this.questaoRepository = questaoRepository;
        this.professorRepository = professorRepository;
    }

    @Transactional
    public Questao criarQuestao(QuestaoCadastroDTO data){
        Professor professor = getProfessorLogado();

        if (professor == null) throw new RuntimeException("Professor não autenticado.");

        Questao questao = new Questao();
        questao.setDisciplina(data.disciplina());
        questao.setAlternativas(data.alternativas());
        questao.setRespostaCorreta(data.respostaCorreta());
        questao.setEnunciado(data.enunciado());
        questao.setProfessorCriador(professor);
        questao.setTipo(data.tipo());

        return this.questaoRepository.save(questao);
    }

    @Transactional(readOnly = true)
    public List<Questao> getAllQuestoes(){
        List<Questao> questao = questaoRepository.findAll();
        return questao;
    }

    @Transactional(readOnly = true)
    public Questao getQuestao(Long id){     
        return questaoRepository.findById(id).orElse(null);
    }

    @Transactional
    public Questao updateQuestao(QuestaoCadastroDTO data, Long id){
        Professor professor = getProfessorLogado();

        Questao questao = questaoRepository.findById(id).orElseThrow(() -> new RuntimeException("Questão não encontrada."));

        if (!questao.getProfessorCriador().getId().equals(professor.getId())){
            throw new RuntimeException("Acesso Negado: Você só pode editar suas próprias questões.");
        }

        questao.setDisciplina(data.disciplina());
        questao.setAlternativas(data.alternativas());
        questao.setRespostaCorreta(data.respostaCorreta());
        questao.setEnunciado(data.enunciado());
        questao.setTipo(data.tipo());

        return this.questaoRepository.save(questao);
    }

    @Transactional
    public boolean deleteQuestao(Long id){
        Professor professor = getProfessorLogado();

        Optional<Questao> questaoOpcional = questaoRepository.findById(id);
        Questao questao = questaoOpcional.get();

        if (!questao.getProfessorCriador().getId().equals(professor.getId())) {
            throw new RuntimeException("Acesso Negado: Você só pode deletar suas próprias questões.");
        }

        questaoRepository.deleteById(id);
        return false;
    }

    private Professor getProfessorLogado(){
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        Professor professor = (Professor) professorRepository.findByLogin(login);

        if (professor == null){
            throw new RuntimeException("Professor não autenticado");
        }

        return professor;
    }
}
