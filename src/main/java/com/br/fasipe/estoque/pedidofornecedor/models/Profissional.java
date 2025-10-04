package com.br.fasipe.estoque.pedidofornecedor.models;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "PROFISSIONAL")
public class Profissional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDPROFISSIO")
    private Integer id;

    @OneToOne
    @JoinColumn(name = "ID_PESSOAFIS")
    private PessoaFis pessoaFis;

    public Profissional() {
    }

    public Profissional(Integer id, PessoaFis pessoaFis) {
        this.id = id;
        this.pessoaFis = pessoaFis;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public PessoaFis getPessoaFis() {
        return pessoaFis;
    }

    public void setPessoaFis(PessoaFis pessoaFis) {
        this.pessoaFis = pessoaFis;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Profissional that = (Profissional) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Profissional{" +
                "id=" + id +
                ", pessoaFisId=" + (pessoaFis != null ? pessoaFis.getId() : "null") +
                '}';
    }
}