package com.educamais.app.security;

import java.util.List;

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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(req -> {
                    req.requestMatchers(HttpMethod.POST, "/auth/login").permitAll();
                    req.requestMatchers("/v3/api-docs/**").permitAll();
                    req.requestMatchers("/swagger-ui/**").permitAll();
                    req.requestMatchers("/swagger-ui.html").permitAll();

                    //PERMISSÕES DO GESTOR
                    req.requestMatchers(HttpMethod.GET, "/alunos").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.POST, "/alunos").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.PUT, "/alunos/**").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.DELETE, "/alunos/**").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.POST, "/turmas").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.PUT, "/turmas/**").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.DELETE, "/turmas/**").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.POST, "/turmas/{turmaId}/associar-professor").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.GET, "/professores").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.POST, "/professores").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.DELETE, "/professores/**").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.PUT, "/professores/**").hasRole("ADMIN");

                    //PROFESSOR E GESTOR
                    req.requestMatchers(HttpMethod.GET, "/alunos/{id}").hasAnyRole("ADMIN", "PROFESSOR");
                    req.requestMatchers(HttpMethod.GET, "/turmas").hasAnyRole("ADMIN", "PROFESSOR");
                    req.requestMatchers(HttpMethod.POST, "/avaliacoes").hasAnyRole("ADMIN", "PROFESSOR");
                    req.requestMatchers(HttpMethod.GET, "/alunos/{alunoId}/avaliacoes").hasAnyRole("ADMIN", "PROFESSOR");
                    req.requestMatchers(HttpMethod.GET, "/professores/minhas-turmas").hasAnyRole("ADMIN", "PROFESSOR");
                    
                    req.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();
                    req.anyRequest().authenticated();
                })
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
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
