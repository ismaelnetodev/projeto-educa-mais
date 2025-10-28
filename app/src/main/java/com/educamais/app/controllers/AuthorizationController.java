package com.educamais.app.controllers;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import com.educamais.app.dtos.LoginDTO;
import com.educamais.app.dtos.LoginResponseDTO;
import com.educamais.app.model.User;
import com.educamais.app.security.TokenService;

@RestController
@RequestMapping("/auth")
public class AuthorizationController {
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;


    public AuthorizationController(AuthenticationManager authenticationManager, TokenService tokenService){
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginDTO data){
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var user = (User) auth.getPrincipal();
        var token = tokenService.generateToken(user);
        var role = user.getRole().toString();
        var responseDTO = new LoginResponseDTO(token, role);
        return ResponseEntity.ok(responseDTO);
    }
}
