package com.educamais.app.dtos;

import java.time.LocalDateTime;
import java.util.List;

public record SimuladoCriarDTO(
    String titulo,
    Long turmaId,
    Long disciplinaId,
    List<Long> questoesId,
    LocalDateTime inicioDisponivel,
    LocalDateTime fimDisponivel
) {

}
