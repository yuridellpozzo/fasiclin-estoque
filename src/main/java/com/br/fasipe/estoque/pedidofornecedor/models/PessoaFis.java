package com.br.fasipe.estoque.pedidofornecedor.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "PESSOAFIS")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PessoaFis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDPESSOAFIS")
    private Integer id;

    @Column(name = "ID_PESSOA", unique = true)
    private Integer idPessoa;

    @Column(name = "CPFPESSOA", nullable = false, unique = true, length = 11)
    private String cpf;

    @Column(name = "NOMEPESSOA", nullable = false, length = 100)
    private String nome;

    // Outros campos como DATANASCPES, SEXOPESSOA, etc. podem ser adicionados aqui.

    public PessoaFis() {
    }

    public PessoaFis(Integer id, Integer idPessoa, String cpf, String nome) { // Corrigido o construtor
        this.id = id;
        this.idPessoa = idPessoa;
        this.cpf = cpf;
        this.nome = nome;
    }

    public Integer getId() { // Renomeado de getIdPessoaFis para getId para consistência
        return id;
    }

    public void setId(Integer id) { // Renomeado de setIdPessoaFis para setId
        this.id = id;
    }

    public Integer getIdPessoa() {
        return idPessoa;
    }

    public void setIdPessoa(Integer idPessoa) {
        this.idPessoa = idPessoa;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
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
        PessoaFis that = (PessoaFis) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "PessoaFis{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                '}';
    }
}
