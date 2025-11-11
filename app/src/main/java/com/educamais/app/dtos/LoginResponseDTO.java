package com.educamais.app.dtos;

public record LoginResponseDTO(String token, String role, boolean senhaTemporaria) {

}
