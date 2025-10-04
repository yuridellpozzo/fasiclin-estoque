package com.br.fasipe.estoque.pedidofornecedor.models;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "USUARIO")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDUSUARIO")
    private Integer id;

    @Column(name = "LOGUSUARIO", nullable = false, unique = true)
    private String login;

    @Column(name = "SENHAUSUA", nullable = false)
    private String senha;

    @OneToOne
    @JoinColumn(name = "ID_PROFISSIO")
    private Profissional profissional;

    public Usuario() {
    }

    public Usuario(Integer id, String login, String senha, Profissional profissional) {
        this.id = id;
        this.login = login;
        this.senha = senha;
        this.profissional = profissional;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Profissional getProfissional() {
        return profissional;
    }

    public void setProfissional(Profissional profissional) {
        this.profissional = profissional;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}