package com.br.fasipe.estoque.pedidofornecedor.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "PESSOAFIS") // Nome exato da tabela conforme sua imagem
public class PessoaFis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDPESSOAFIS")
    private Integer id;

    // --- CAMPOS REMOVIDOS (Causavam o erro) ---
    // private String nomePessoa; 
    // private String cpfPessoa;
    // ------------------------------------------

    // --- NOVOS MAPEAMENTOS (Conforme sua imagem) ---

    @Column(name = "ID_PESSOA")
    private Integer idPessoa; // FK para a tabela PESSOA (onde provavelmente está o nome agora)

    @Column(name = "SEXOPESSOA")
    private String sexoPessoa;

    @Column(name = "DATACRIACAO")
    private LocalDateTime dataCriacao; // ou LocalDate, dependendo do DB

    @Column(name = "ID_DOCUMENTO")
    private String idDocumento; // FK para a tabela DOCUMENTO

    // --- GETTERS E SETTERS ---

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdPessoa() {
        return idPessoa;
    }

    public void setIdPessoa(Integer idPessoa) {
        this.idPessoa = idPessoa;
    }

    public String getSexoPessoa() {
        return sexoPessoa;
    }

    public void setSexoPessoa(String sexoPessoa) {
        this.sexoPessoa = sexoPessoa;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(String idDocumento) {
        this.idDocumento = idDocumento;
    }
}