package com.educamais.app.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.educamais.app.dtos.ProfessorCadastroDTO;
import com.educamais.app.dtos.ProfessorResponseDTO;
import com.educamais.app.model.Professor;
import com.educamais.app.services.ProfessorService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/professores")
public class ProfessorController {
    private final ProfessorService professorService;
    public ProfessorController(ProfessorService professorService) {
        this.professorService = professorService;
    }

    @PostMapping()
    public ResponseEntity<?> criarProfessor(@RequestBody ProfessorCadastroDTO data) {
        try{
            Professor professor = professorService.cadastroProfessor(data);
            ProfessorResponseDTO professorResponse = new ProfessorResponseDTO(professor);
            return ResponseEntity.status(HttpStatus.CREATED).body(professorResponse);
        }
        catch (RuntimeException error) {
            return ResponseEntity.badRequest().body(error.getMessage());
        } 
    }

    @GetMapping()
    public ResponseEntity<List<ProfessorResponseDTO>> getProfessores() {
        List<Professor> professor = professorService.getProfessores();
        if (professor.isEmpty() || professor == null) {
            return ResponseEntity.notFound().build();
        }
        List<ProfessorResponseDTO> responseDTO = professor.stream()
            .map(ProfessorResponseDTO::new)
            .collect(Collectors.toList());
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("{id}")
    public ResponseEntity<ProfessorResponseDTO> getProfessorById(@PathVariable UUID id) {
        Professor professor = professorService.getProfessor(id);
        if (professor == null) {
            return ResponseEntity.notFound().build();
        }
        ProfessorResponseDTO response = new ProfessorResponseDTO(professor);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("{id}")
    public ResponseEntity<ProfessorResponseDTO> atualizarProfessor(@PathVariable UUID id, @RequestBody ProfessorCadastroDTO data) {
        Professor professor = professorService.updateProfessor(id, data);
        if (professor == null) {
            ResponseEntity.notFound().build();
        } 
        ProfessorResponseDTO response = new ProfessorResponseDTO(professor);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> detelarProfessor(@PathVariable UUID id) {
        Professor professor = professorService.deleteProfessor(id);
        if (professor == null) {
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
    
    
    


}
