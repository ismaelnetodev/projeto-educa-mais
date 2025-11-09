package com.educamais.app.dtos;

import java.time.LocalDateTime;

import com.educamais.app.model.SimuladoAluno;

public record SimuladoAlunoResponseDTO(
    Long id,
    String alunoNome,
    String simuladoTitulo,
    double notaFinal,
    LocalDateTime dataSubmissao
) {

    public SimuladoAlunoResponseDTO(SimuladoAluno simuladoAluno){
        this(
            simuladoAluno.getId(),
            simuladoAluno.getAluno().getNome(),
            simuladoAluno.getSimulado().getTitulo(),
            simuladoAluno.getNotaFinal(),
            simuladoAluno.getDataSubmissao()
        );
    }

}
