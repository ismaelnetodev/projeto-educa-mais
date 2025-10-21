package com.educamais.app.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.educamais.app.enums.Roles;
import com.educamais.app.model.Gestor;
import com.educamais.app.repository.GestorRepository;

@Component
public class DataSeeder implements CommandLineRunner {

    private final GestorRepository gestorRepository;
    private final PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(DataSeeder.class);

    public DataSeeder(GestorRepository gestorRepository, PasswordEncoder passwordEncoder) {
        this.gestorRepository = gestorRepository;
        this.passwordEncoder = passwordEncoder;
    }

@Override
    public void run(String... args) throws Exception {

        if (gestorRepository.count() == 0) {
            logger.info("Nenhum gestor encontrado. Criando usuário admin padrão...");

            String senhaCriptografada = passwordEncoder.encode("admin123");

            Gestor admin = new Gestor();
            admin.setNome("Admin Padrão");
            admin.setLogin("admin");
            admin.setPassword(senhaCriptografada);
            admin.setRole(Roles.GESTOR);
            admin.setEnabled(true);
            
            gestorRepository.save(admin);
            
            logger.info("Usuário admin criado com sucesso (login: admin, senha: admin123)");
        } else {
            logger.info("Já existem gestores cadastrados. Nenhum usuário foi criado.");
        }
    }
}