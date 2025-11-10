package com.educamais.app.dtos;

import java.time.LocalDateTime;

public record SimuladoGerarDTO(
    String titulo,
    Long turmaId,
    Long disciplinaId,
    int numeroQuestoes,
    LocalDateTime inicioDisponivel,
    LocalDateTime fimDisponivel
) {

}
