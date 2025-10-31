package com.educamais.app.controllers;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.educamais.app.dtos.AlunoCadastroDTO;
import com.educamais.app.dtos.AlunoResponseDTO;
import com.educamais.app.dtos.AvaliacaoResponseDTO;
import com.educamais.app.model.Aluno;
import com.educamais.app.model.AvaliacaoQualitativa;
import com.educamais.app.services.AlunoService;
import com.educamais.app.services.AvaliacaoQualitativaService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;




@RestController
@RequestMapping("/alunos")
public class AlunoController {

    private final AlunoService alunoService;
    private final AvaliacaoQualitativaService avaliacaoQualitativaService;

    public AlunoController(AlunoService alunoService, AvaliacaoQualitativaService avaliacaoQualitativaService){
        this.alunoService = alunoService;
        this.avaliacaoQualitativaService = avaliacaoQualitativaService;
    }

    @PostMapping()
    public ResponseEntity<?> criarALuno(@RequestBody AlunoCadastroDTO data) {
        try{
            Aluno alunoSalvo = this.alunoService.criarAluno(data);
            AlunoResponseDTO responseDTO = new AlunoResponseDTO(alunoSalvo);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    

    @GetMapping
    public ResponseEntity<List<AlunoResponseDTO>> listarAlunos(){
        List<Aluno> alunos = alunoService.getAlunos();

        if (alunos == null || alunos.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        List<AlunoResponseDTO> responseDTOs = alunos.stream()
            .map(AlunoResponseDTO::new)
            .collect(Collectors.toList());

        return ResponseEntity.ok().body(responseDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlunoResponseDTO> getAlunoById(@PathVariable UUID id) {
        Aluno aluno = alunoService.getAlunoById(id);

        if (aluno == null){
            return ResponseEntity.notFound().build();
        }

        AlunoResponseDTO responseDTO = new AlunoResponseDTO(aluno);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlunoResponseDTO> updateAluno(@PathVariable UUID id, @RequestBody AlunoCadastroDTO data) {
        Aluno alunoAtualizado = this.alunoService.updateAluno(id, data);

        if (alunoAtualizado == null){
            return ResponseEntity.notFound().build();
        }
        AlunoResponseDTO responseDTO = new AlunoResponseDTO(alunoAtualizado);
        return ResponseEntity.ok().body(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAluno(@PathVariable UUID id){
        Aluno alunoDeletado = alunoService.deleteAluno(id);

        if (alunoDeletado == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{alunoId}/avaliacoes")
    public ResponseEntity<List<AvaliacaoResponseDTO>> getHistoricoDoAluno(@PathVariable UUID alunoId) {
        try {
            List<AvaliacaoQualitativa> historico = avaliacaoQualitativaService.getHistoricoDoAluno(alunoId);

            List<AvaliacaoResponseDTO> dtos = historico.stream()
                .map(AvaliacaoResponseDTO::new)
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(dtos);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    

}
