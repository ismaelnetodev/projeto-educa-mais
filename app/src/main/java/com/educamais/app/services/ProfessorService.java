package com.educamais.app.services;

import java.net.PasswordAuthentication;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.context.support.BeanDefinitionDsl.Role;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.educamais.app.dtos.AlunoCadastroDTO;
import com.educamais.app.dtos.ProfessorCadastroDTO;
import com.educamais.app.enums.Roles;
import com.educamais.app.model.Professor;
import com.educamais.app.repository.ProfessorRepository;

@Service
public class ProfessorService {

    private final ProfessorRepository professorRepository;
    private final PasswordEncoder passwordEncoder;

    public ProfessorService (ProfessorRepository professorRepository, PasswordEncoder passwordEncoder) {
        this.professorRepository = professorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Cadastrar professor
    public Professor cadastroProfessor (ProfessorCadastroDTO data) {
        String senhaCriptografada = passwordEncoder.encode(data.password());
        Professor professor = new Professor();
        professor.setNome(data.nome());
        professor.setLogin(data.login());
        professor.setPassword(senhaCriptografada);
        professor.setRole(Roles.PROFESSOR); 
        return professorRepository.save(professor);
    }

    // Retorna todos os professores
    public List<Professor> getProfessores() {
        List<Professor> professores = professorRepository.findAll();
        if (professores.isEmpty()) {
            return null;
        }
        return professores;
    } 

    // Retornar um professor por ID
    public Professor getProfessor(UUID id) {
        Optional<Professor> professor = professorRepository.findById(id);
        if (professor.isEmpty()) {
            return null;
        }
        return professor.get();
    }
    
    // Atualizar professor
    public Professor updateProfessor(UUID id, ProfessorCadastroDTO data) {
        Optional<Professor> professor = professorRepository.findById(id);
        if (professor.isEmpty()) {
            return null;
        }
        Professor professorAtualizado = professor.get();
        professorAtualizado.setNome(data.nome());
        professorAtualizado.setLogin(data.login());
        professorAtualizado.setPassword(data.password());
        return this.professorRepository.save(professorAtualizado);
    }

    // Deletar professor
    public Professor deleteProfessor(UUID id) {
        Optional<Professor> professor = professorRepository.findById(id);
        if (professor.isPresent()) {
            professorRepository.deleteById(id);
            return professor.get();
        }
        return null;
    }
}
