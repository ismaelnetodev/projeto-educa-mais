package com.educamais.app.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.educamais.app.dtos.AlunoCadastroDTO;
import com.educamais.app.dtos.ProfessorCadastroDTO;
import com.educamais.app.dtos.TurmaCadastroDTO;
import com.educamais.app.enums.Roles;
import com.educamais.app.model.Gestor;
import com.educamais.app.model.Professor;
import com.educamais.app.model.Turma;
import com.educamais.app.repository.GestorRepository;
import com.educamais.app.services.AlunoService;
import com.educamais.app.services.ProfessorService;
import com.educamais.app.services.TurmaService;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataSeeder.class);

    @Value("${api.security.admin.password}")
    private String adminPassword;

    private final GestorRepository gestorRepository;
    private final PasswordEncoder passwordEncoder;
    private final TurmaService turmaService;
    private final ProfessorService professorService;
    private final AlunoService alunoService;

    public DataSeeder(GestorRepository gestorRepository, PasswordEncoder passwordEncoder, TurmaService turmaService, ProfessorService professorService, AlunoService alunoService) {
        this.gestorRepository = gestorRepository;
        this.passwordEncoder = passwordEncoder;
        this.turmaService = turmaService;
        this.professorService = professorService;
        this.alunoService = alunoService;
    }

@Override
    public void run(String... args) throws Exception {

        if (gestorRepository.count() == 0) {
            logger.info("Banco de dados vazio, semeando...");

            String senhaCriptografada = passwordEncoder.encode(adminPassword);
            
            //Criação do primeiro gestor/admin
            Gestor admin = new Gestor();
            admin.setNome("Admin Padrão");
            admin.setLogin("admin");
            admin.setPassword(senhaCriptografada);
            admin.setRole(Roles.GESTOR);
            admin.setEnabled(true);
            
            gestorRepository.save(admin);
            
            logger.info("Usuário Gestor 'admin' criado com sucesso.");

            //Criação da primeira turma (se não houver)
            TurmaCadastroDTO turmaDTO = new TurmaCadastroDTO("1° Ano A - Matutino", 2025);
            Turma turmaA = turmaService.criarTurma(turmaDTO);
            logger.info("Turma de exemplo criada");

            //Criação do professor (se não houver)
            ProfessorCadastroDTO profDTO = new ProfessorCadastroDTO("Prof. Ismael", "ismael", "1234");
            Professor professor = professorService.cadastroProfessor(profDTO);
            logger.info("Usuário PROFESSOR criado.");

            //Associar professor a turma
            turmaService.associarProfessor(turmaA.getId(), professor.getId());
            logger.info("Professor associado a turma existente");

            //Criar aluno e associar a turma
            AlunoCadastroDTO aluno1DTO = new AlunoCadastroDTO("João Pedro", "joao.pedro", "1234", "002", turmaA.getId());
            alunoService.criarAluno(aluno1DTO);

            AlunoCadastroDTO aluno2DTO = new AlunoCadastroDTO("Mariana Stephany", "marii", "1234", "003", turmaA.getId());
            alunoService.criarAluno(aluno2DTO);

            logger.info("Alunos incluidos!");
            logger.info("Semeamento completo");


        } else {
            logger.info("Já existem usuarios cadastrados. Nenhum usuário foi criado.");
        }
    }
}