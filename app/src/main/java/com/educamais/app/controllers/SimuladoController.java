package com.educamais.app.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.educamais.app.dtos.SimuladoAlunoResponseDTO;
import com.educamais.app.dtos.SimuladoGerarDTO;
import com.educamais.app.dtos.SimuladoResponseDTO;
import com.educamais.app.dtos.SimuladoSubmeterDTO;
import com.educamais.app.model.Simulado;
import com.educamais.app.services.SimuladoService;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/simulados")
public class SimuladoController {

    private final SimuladoService simuladoService;

    public SimuladoController(SimuladoService simuladoService){
        this.simuladoService = simuladoService;
    }

    @PostMapping("/gerar-aleatorio")
    public ResponseEntity<?> gerarSimulado(@RequestBody SimuladoGerarDTO data) {
        try {
            SimuladoResponseDTO novoSimulado = simuladoService.gerarSimuladoAleatorio(data);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoSimulado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/aluno/minhas-provas")
    public ResponseEntity<List<SimuladoResponseDTO>> getSimuladoParaAluno() {
        List<SimuladoResponseDTO> dtos = simuladoService.getSimuladoParaAluno();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{simuladoId}/fazer")
    public ResponseEntity<SimuladoResponseDTO> getSimuladoParaFazer(@PathVariable Long simuladoId) {
        try {
            SimuladoResponseDTO dto = simuladoService.getSimuladoParaFazer(simuladoId);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{simuladoId}/submeter")
    public ResponseEntity<?> submeterSimulado(@PathVariable Long simuladoId, @RequestBody SimuladoSubmeterDTO data) {
        try{
            SimuladoAlunoResponseDTO resultadoDTO = simuladoService.submeterSimulado(simuladoId, data);
            return ResponseEntity.ok(resultadoDTO);
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/professor/meus-simulados")
    public ResponseEntity<List<SimuladoResponseDTO>> getSimuladosDoProfessor() {
        List<SimuladoResponseDTO> dtos = simuladoService.getSimuladosDoProfessorLogado();
        
        return ResponseEntity.ok(dtos);
    }


    @GetMapping("/{simuladoId}/resultados")
    public ResponseEntity<?> getResultadoDoSimulado(@PathVariable Long simuladoId) {
        try{
            List<SimuladoAlunoResponseDTO> dtos = simuladoService.getResultadosDoSimulado(simuladoId);
            return ResponseEntity.ok(dtos);
        } catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }

}
