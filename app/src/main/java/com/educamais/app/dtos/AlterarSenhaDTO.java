package com.educamais.app.dtos;

import jakarta.validation.constraints.NotBlank;

public record AlterarSenhaDTO(@NotBlank String senhaAntiga, @NotBlank String novaSenha) {

}
