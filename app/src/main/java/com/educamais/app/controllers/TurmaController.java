package com.educamais.app.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.educamais.app.dtos.TurmaCadastroDTO;
import com.educamais.app.model.Turma;
import com.educamais.app.repository.TurmaRepository;

@RestController
@RequestMapping("/turmas")
public class TurmaController {
    private final TurmaRepository turmaRepository;

    public TurmaController(TurmaRepository turmaRepository){
        this.turmaRepository = turmaRepository;
    }

    @PostMapping
    public ResponseEntity<Turma> criarTurma(@RequestBody TurmaCadastroDTO data){
        Turma novaTurma = new Turma();
        novaTurma.setNome(data.nome());
        novaTurma.setAnoLetivo(data.anoLetivo());

        Turma turmaSalva = this.turmaRepository.save(novaTurma);
        return ResponseEntity.status(HttpStatus.CREATED).body(turmaSalva);
    }

    @GetMapping
    public ResponseEntity<Turma> listarTurmas(){
        //implementar
    }
}
