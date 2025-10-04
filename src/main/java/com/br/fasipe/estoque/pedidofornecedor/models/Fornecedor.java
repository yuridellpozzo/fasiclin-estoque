package com.br.fasipe.estoque.pedidofornecedor.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import jakarta.persistence.FetchType;
import jakarta.persistence.GenerationType;

import jakarta.validation.constraints.Size;
import java.util.Objects;


@Entity
@Table(name = "FORNECEDOR")
public class Fornecedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDFORNECEDOR")
    private Integer id;

    @NotNull(message = "A pessoa jurídica deve ser informada.")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PESSOA", nullable = false, unique = true)
    private PessoaJuridica pessoasJuridica;

     @Size(max = 100)
    @Column(name = "REPRESENT", length = 100)
    private String representante;

    @Size(max = 15)
    @Column(name = "CONTREPRE", length = 15)
    private String contatoRepresentante;

    @Size(max = 250)
    @Column(name = "DESCRICAO", length = 250) // Corrigido de DECRICAO para DESCRICAO
    private String descricao; // Nome do campo estava incorreto no SQL (DECRICAO)

    public Fornecedor() {
    }

    public Fornecedor(Integer id, PessoaJuridica pessoasJuridica, String representante, String contatoRepresentante, String descricao) {
        this.id = id;
        this.pessoasJuridica = pessoasJuridica;
        this.representante = representante;
        this.contatoRepresentante = contatoRepresentante;
        this.descricao = descricao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public PessoaJuridica getPessoasJuridica() {
        return pessoasJuridica;
    }

    public void setPessoasJuridica(PessoaJuridica pessoasJuridica) {
        this.pessoasJuridica = pessoasJuridica;
    }

    public String getRepresentante() {
        return representante;
    }

    public void setRepresentante(String representante) {
        this.representante = representante;
    }

    public String getContatoRepresentante() {
        return contatoRepresentante;
    }

    public void setContatoRepresentante(String contatoRepresentante) {
        this.contatoRepresentante = contatoRepresentante;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fornecedor that = (Fornecedor) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Fornecedor{" +
                "id=" + id +
                ", pessoasJuridica=" + (pessoasJuridica != null ? pessoasJuridica.getId() : null) +
                ", representante='" + representante + '\'' +
                ", contatoRepresentante='" + contatoRepresentante + '\'' +
                ", descricao='" + descricao + '\'' +
                '}';
    }
    
}
