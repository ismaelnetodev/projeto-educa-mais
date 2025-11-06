package com.educamais.app.services;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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

    public Questao criarQuestao(QuestaoCadastroDTO data){
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        Professor professor = (Professor) professorRepository.findByLogin(login);

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

    public List<Questao> getAllQuestoes(){
        List<Questao> questao = questaoRepository.findAll();
        return questao;
    }

    public Questao getQuestao(Long id){
        Optional<Questao> questao = questaoRepository.findById(id);

        return questao.get();

    }

    public Questao updateQuestao(QuestaoCadastroDTO data, Long id){
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        Professor professor = (Professor) professorRepository.findByLogin(login);

        Optional<Questao> questao = questaoRepository.findById(id);

        if (professor == null) throw new RuntimeException("Professor não autenticado.");
        if (questao.isEmpty()) return null;

        Questao questaoAtualizada = new Questao();
        questaoAtualizada.setDisciplina(data.disciplina());
        questaoAtualizada.setAlternativas(data.alternativas());
        questaoAtualizada.setRespostaCorreta(data.respostaCorreta());
        questaoAtualizada.setEnunciado(data.enunciado());
        questaoAtualizada.setProfessorCriador(professor);
        questaoAtualizada.setTipo(data.tipo());

        return this.questaoRepository.save(questaoAtualizada);
    }

    public boolean deleteQuestao(Long id){
        Optional<Questao> questao = questaoRepository.findById(id);

        if (questao.isPresent()){
            questaoRepository.deleteById(id);
            return true;
        }

        return false;
    }
}
