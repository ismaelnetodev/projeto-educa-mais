package com.educamais.app.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.educamais.app.enums.Roles;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String login;
    private String password;
    private Roles role;
    private boolean enabled;

    public User(){}

    public User(String login, String password, Roles role, boolean enabled){
        this.login = login;
        this.password = password;
        this.role = role;
        this.enabled = enabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        if (this.role == Roles.PROFESSOR || this.role == Roles.GESTOR){
            authorities.add(new SimpleGrantedAuthority("ROLE_PROFESSOR"));
        }

        if (this.role == Roles.GESTOR){
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        if (this.role == Roles.ALUNO){
            authorities.add(new SimpleGrantedAuthority("ROLE_ALUNO"));
        }

        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.login;
    }

    @Override
    public boolean isAccountNonExpired(){
        return true;
    }

    @Override
    public boolean isAccountNonLocked(){
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired(){
        return true;
    }

    @Override
    public boolean isEnabled(){
        return this.enabled;
    }

}
