package com.educamais.app.dtos;

import java.util.UUID;

public record AvaliacaoCadastroDTO(
    UUID alunoId,
    double assiduidade,
    double participacao,
    double responsabilidade,
    double sociabilidade,
    String observacao
) {

}
