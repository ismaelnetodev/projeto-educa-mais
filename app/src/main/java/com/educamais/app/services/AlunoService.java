package com.educamais.app.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.educamais.app.dtos.AlunoCadastroDTO;
import com.educamais.app.enums.Roles;
import com.educamais.app.model.Aluno;
import com.educamais.app.model.Turma;
import com.educamais.app.repository.AlunoRepository;
import com.educamais.app.repository.TurmaRepository;

@Service
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private final TurmaRepository turmaRepository;
    private final PasswordEncoder passwordEncoder;

    public AlunoService(AlunoRepository alunoRepository, TurmaRepository turmaRepository, PasswordEncoder passwordEncoder){
        this.alunoRepository = alunoRepository;
        this.turmaRepository = turmaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Aluno criarAluno(AlunoCadastroDTO data){
        Turma turma = turmaRepository.findById(data.turmaId()).orElseThrow(() -> new RuntimeException("Turma não encontrada com o id: " + data.turmaId()));

        String senhaCriptografada = passwordEncoder.encode(data.password());
        Aluno novoAluno = new Aluno();
        novoAluno.setNome(data.nome());
        novoAluno.setLogin(data.login());
        novoAluno.setPassword(senhaCriptografada);
        novoAluno.setMatricula(data.matricula());
        novoAluno.setRole(Roles.ALUNO);
        novoAluno.setEnabled(true);
        novoAluno.setTurma(turma);

        return alunoRepository.save(novoAluno);
    }

    public List<Aluno> getAlunos(){
        List<Aluno> alunos = alunoRepository.findAll();
        
        if (alunos.isEmpty()){
            return null;
        }

        return alunos;

    }

    public Aluno getAlunoById(UUID id){
        Optional<Aluno> aluno = alunoRepository.findById(id);

        if (aluno.isEmpty()){
            return null;
        }

        return aluno.get();
    }

    public Aluno updateAluno(UUID id, AlunoCadastroDTO data){
        Turma turma = turmaRepository.findById(data.turmaId()).orElseThrow(() -> new RuntimeException("Turma não encontrada com o id: " + data.turmaId()));
        Optional<Aluno> alunoOptional = alunoRepository.findById(id);

        if (alunoOptional.isEmpty()){
            return null;
        }

        String novaSenha = passwordEncoder.encode(data.password());

        Aluno alunoExistente = alunoOptional.get();
        alunoExistente.setNome(data.nome());
        alunoExistente.setLogin(data.login());
        alunoExistente.setMatricula(data.matricula());
        alunoExistente.setPassword(novaSenha);
        alunoExistente.setTurma(turma);
        return this.alunoRepository.save(alunoExistente);
    }

    public Aluno deleteAluno(UUID id){
        Optional<Aluno> aluno = this.alunoRepository.findById(id);
        if (aluno.isPresent()){
            this.alunoRepository.deleteById(id);
            return aluno.get();
        }

        return null;
    }

}
