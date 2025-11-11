package com.educamais.app.dtos;

public record AlunoCadastroDTO(
    String nome,
    String matricula,
    String fotoUrl,
    Long turmaId
) {

}
