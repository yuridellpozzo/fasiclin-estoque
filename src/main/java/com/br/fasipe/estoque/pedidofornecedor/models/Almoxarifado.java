package com.br.fasipe.estoque.pedidofornecedor.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.GenerationType;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.Objects;

@Entity
@Table(name = "ALMOXARIFADO")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Almoxarifado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDALMOX")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_SETOR")
    private Setor setor;

    @Column(name = "NOMEALMO", nullable = false, unique = true, length = 100)
    private String nome;

    public Almoxarifado() {
    }

    public Almoxarifado(Integer id, Setor setor, String nome) {
        this.id = id;
        this.setor = setor;
        this.nome = nome;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Setor getSetor() {
        return setor;
    }

    public void setSetor(Setor setor) {
        this.setor = setor;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Almoxarifado that = (Almoxarifado) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Almoxarifado{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                '}';
    }
}
