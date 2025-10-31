package com.educamais.app.dtos;

public record AlunoCadastroDTO(
    String nome,
    String login,
    String password,
    String matricula,
    String fotoUrl,
    Long turmaId
) {

}
