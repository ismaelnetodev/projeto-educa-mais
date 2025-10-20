package com.educamais.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.educamais.app.repository.AlunoRepository;
import com.educamais.app.repository.GestorRepository;
import com.educamais.app.repository.ProfessorRepository;

@Service
public class AuthenticationService implements UserDetailsService{

    private final AlunoRepository alunoRepository;
    private final ProfessorRepository professorRepository;
    private final GestorRepository gestorRepository;

    public AuthenticationService(AlunoRepository alunoRepository, ProfessorRepository professorRepository, GestorRepository gestorRepository){
        this.alunoRepository = alunoRepository;
        this.professorRepository = professorRepository;
        this.gestorRepository = gestorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = this.alunoRepository.findByLogin(username);

        if (user == null){
            user = this.professorRepository.findByLogin(username);
        }

        if (user == null){
            user = this.gestorRepository.findByLogin(username);
        }

        if (user == null){
            throw new UsernameNotFoundException(username);
        }

        return user;
    }

}
