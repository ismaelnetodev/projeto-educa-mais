package com.educamais.app.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/alunos")
public class AlunoController {

    @GetMapping
    public ResponseEntity<String> listarAlunos(){
        return ResponseEntity.ok("Você é um administrdor/gestor e pode ver essa lista");
    }

}
