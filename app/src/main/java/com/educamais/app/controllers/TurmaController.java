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
import com.educamais.app.services.TurmaService;

@RestController
@RequestMapping("/turmas")
public class TurmaController {
    private final TurmaService turmaService;

    public TurmaController(TurmaService turmaService){
        this.turmaService = turmaService;
    }

    @PostMapping
    public ResponseEntity<Turma> criarTurma(@RequestBody TurmaCadastroDTO data){
        Turma turmaSalva = this.turmaService.criarTurma(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(turmaSalva);
    }

}
