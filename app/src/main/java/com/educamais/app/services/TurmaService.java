package com.educamais.app.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.educamais.app.dtos.TurmaCadastroDTO;
import com.educamais.app.model.Turma;
import com.educamais.app.repository.TurmaRepository;

@Service
public class TurmaService {

    private final TurmaRepository turmaRepository;

    public TurmaService(TurmaRepository turmaRepository){
        this.turmaRepository = turmaRepository;
    }

    public Turma criarTurma(TurmaCadastroDTO data){
        if (turmaRepository.existsByNomeAndAnoLetivo(data.nome(), data.anoLetivo())){
            return null;
        }

        Turma novaTurma = new Turma();
        novaTurma.setNome(data.nome());
        novaTurma.setAnoLetivo(data.anoLetivo());

        return this.turmaRepository.save(novaTurma);
    }

    public List<Turma> listarTurmas(){
        return this.turmaRepository.findAll();
    }

}
