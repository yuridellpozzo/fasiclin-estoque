package com.br.fasipe.estoque.pedidofornecedor.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.GenerationType;
import java.util.Objects;

@Entity
@Table(name = "ALMOXARIFADO")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Almoxarifado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDALMOX")
    private Integer id;


    @Column(name = "NOMEALMO", nullable = false, unique = true, length = 100)
    private String nome;

    @Column(name = "ID_SETOR") // Use o nome real da coluna FK no DB (ex: ID_SETOR, IDSETOR)
    private Integer setorId;

    public Almoxarifado() {
    }

    public Almoxarifado(Integer id, String nome) {
        this.id = id;

        this.nome = nome;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getSetorId() {
        return setorId;
    }

    public void setSetorId(Integer setorId) {
        this.setorId = setorId;
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
