package com.educamais.app.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.educamais.app.dtos.AvaliacaoCadastroDTO;
import com.educamais.app.dtos.AvaliacaoResponseDTO;
import com.educamais.app.model.AvaliacaoQualitativa;
import com.educamais.app.services.AvaliacaoQualitativaService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;




@RestController
@RequestMapping("/avaliacoes")
public class AvaliacaoQualitativaController {
    private final AvaliacaoQualitativaService avaliacaoQualitativaService;

    public AvaliacaoQualitativaController(AvaliacaoQualitativaService avaliacaoQualitativaService){
        this.avaliacaoQualitativaService = avaliacaoQualitativaService;
    }

    @PostMapping()
    public ResponseEntity<?> registrarAvaliacao(@Valid @RequestBody AvaliacaoCadastroDTO data) {
        try{
            AvaliacaoQualitativa avaliacaoSalva = avaliacaoQualitativaService.registrarAvaliacao(data);

            AvaliacaoResponseDTO avaliacaoResponseDTO = new AvaliacaoResponseDTO(avaliacaoSalva);

            return ResponseEntity.status(HttpStatus.CREATED).body(avaliacaoResponseDTO);
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
}
