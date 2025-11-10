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

import com.educamais.app.dtos.AlunoResponseDTO;
import com.educamais.app.dtos.AssociacaoProfessorDTO;
import com.educamais.app.dtos.TurmaCadastroDTO;
import com.educamais.app.dtos.TurmaResponseDTO;
import com.educamais.app.model.Aluno;
import com.educamais.app.model.Turma;
import com.educamais.app.services.TurmaService;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@RequestMapping("/turmas")
public class TurmaController {
    private final TurmaService turmaService;

    public TurmaController(TurmaService turmaService){
        this.turmaService = turmaService;
    }

    @GetMapping
    public ResponseEntity<List<TurmaResponseDTO>> listarTurmas(){
        List<Turma> turmas = turmaService.listarTurmas();

        if (turmas.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        List<TurmaResponseDTO> dtos = turmas.stream()
            .map(TurmaResponseDTO::new)
            .collect(Collectors.toList());
        return ResponseEntity.ok().body(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TurmaResponseDTO> getTurma(@PathVariable Long id) {
        Optional<Turma> turma = turmaService.getTurma(id);
        if (turma.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        TurmaResponseDTO turmaResponseDTO = new TurmaResponseDTO(turma.get());
        return ResponseEntity.ok(turmaResponseDTO);
    }

    @PostMapping
    public ResponseEntity<?> criarTurma(@RequestBody TurmaCadastroDTO data){
        Turma turmaSalva = this.turmaService.criarTurma(data);

        if (turmaSalva == null){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Uma turma com o mesmo nome e ano letivo já existe");
        }

        TurmaResponseDTO responseDTO = new TurmaResponseDTO(turmaSalva);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TurmaResponseDTO> atualizarTurma(@PathVariable Long id, @RequestBody TurmaCadastroDTO data) {
        Turma turmaAtualizada = this.turmaService.updateTurma(id, data);
        if (turmaAtualizada == null){
            return ResponseEntity.notFound().build();
        }
        TurmaResponseDTO responseDTO = new TurmaResponseDTO(turmaAtualizada);

        return ResponseEntity.ok(responseDTO);
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
    public ResponseEntity<TurmaResponseDTO> associarProfessor(@PathVariable Long turmaId, @RequestBody AssociacaoProfessorDTO data) {
        try{
            Turma turmaAtualizada = turmaService.associarProfessor(turmaId, data.professorId());
            TurmaResponseDTO responseDTO = new TurmaResponseDTO(turmaAtualizada);
            return ResponseEntity.ok(responseDTO);
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{turmaId}/alunos")
    public ResponseEntity<List<AlunoResponseDTO>> getAlunosDaTurma(@PathVariable Long turmaId) {
        try{
            List<Aluno> alunos = turmaService.getAlunosPorTurma(turmaId);

            List<AlunoResponseDTO> dtos = alunos.stream()
                .map(AlunoResponseDTO::new)
                .collect(Collectors.toList());

            return ResponseEntity.ok().body(dtos);
        } catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/buscar")
    public ResponseEntity<List<TurmaResponseDTO>> buscarTurma(@RequestParam("termo") String termo) {
        List<Turma> turma = turmaService.buscarTurmas(termo);

        List<TurmaResponseDTO> dtos = turma.stream()
            .map(TurmaResponseDTO::new)
            .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }
    

}
