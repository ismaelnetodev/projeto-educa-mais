package com.educamais.app.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.educamais.app.dtos.AssociacaoProfessorDTO;
import com.educamais.app.dtos.TurmaCadastroDTO;
import com.educamais.app.dtos.TurmaResponseDTO;
import com.educamais.app.model.Turma;
import com.educamais.app.services.TurmaService;

import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("/turmas")
public class TurmaController {
    private final TurmaService turmaService;

    public TurmaController(TurmaService turmaService){
        this.turmaService = turmaService;
    }

    @GetMapping
    public ResponseEntity<List<Turma>> listarTurmas(){
        List<Turma> turmas = turmaService.listarTurmas();
        List<TurmaResponseDTO> turmaResponseDTO = turmas.stream()
            .map(TurmaResponseDTO::new)
            .collect(Collectors.toList());
        return ResponseEntity.ok().body(turmas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Turma> getTurma(@PathVariable Long id) {
        Optional<Turma> turma = turmaService.getTurma(id);
        if (turma.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(turma.get());
    }

    @PostMapping
    public ResponseEntity<?> criarTurma(@RequestBody TurmaCadastroDTO data){
        Turma turmaSalva = this.turmaService.criarTurma(data);

        if (turmaSalva == null){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Uma turma com o mesmo nome e ano letivo já existe");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(turmaSalva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Turma> atualizarTurma(@PathVariable Long id, @RequestBody TurmaCadastroDTO data) {
        Turma turmaAtualizada = this.turmaService.updateTurma(id, data);
        if (turmaAtualizada == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(turmaAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Turma> deletarTurma(@PathVariable Long id){
        Turma turmaDeletada = this.turmaService.deleteTurma(id);
        if (turmaDeletada == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{turmaId}/associar-professor")
    public ResponseEntity<Turma> associarProfessor(@PathVariable Long turmaId, @RequestBody AssociacaoProfessorDTO data) {
        try{
            Turma turmaAtualizada = turmaService.associarProfessor(turmaId, data.professorId());
            return ResponseEntity.ok(turmaAtualizada);
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }
    

}
