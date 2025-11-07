package com.educamais.app.dtos;

import java.util.List;

import com.educamais.app.enums.TipoQuestao;

public record QuestaoCadastroDTO(
    String enunciado,
    String disciplina,
    TipoQuestao tipo,
    double pontuacao,
    String imagemUrl,
    List<String> alternativas,
    String respostaCorreta
) {

}
