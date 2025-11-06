package com.educamais.app.dtos;

import java.util.List;

public record QuestaoCadastroDTO(
    String enunciado,
    String disciplina,
    String tipo,
    List<String> alternativas,
    String respostaCorreta
) {

}
