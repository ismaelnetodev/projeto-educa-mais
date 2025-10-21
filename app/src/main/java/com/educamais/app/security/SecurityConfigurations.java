package com.educamais.app.security;

import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties.Http;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    private final SecurityFilter securityFilter;

    public SecurityConfigurations(SecurityFilter securityFilter){
        this.securityFilter = securityFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(req -> {
                    req.requestMatchers(HttpMethod.POST, "/auth/login").permitAll();

                    //PERMISSÕES DO GESTOR
                    req.requestMatchers(HttpMethod.GET, "/alunos").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.POST, "/alunos").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.PUT, "/alunos/**").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.DELETE, "/alunos/**").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.POST, "/turmas").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.PUT, "/turmas/**").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.DELETE, "/turmas/**").hasRole("ADMIN");

                    req.requestMatchers(HttpMethod.GET, "/turmas").hasAnyRole("ADMIN", "PROFESSOR");

                    req.anyRequest().authenticated();
                })
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

}
