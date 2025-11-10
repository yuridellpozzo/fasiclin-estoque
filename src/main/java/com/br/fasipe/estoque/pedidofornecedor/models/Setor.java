package com.br.fasipe.estoque.pedidofornecedor.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

/**
 * Declaração mínima da Entidade Setor para evitar erros de compilação
 */
@Entity
public class Setor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "NOMESETOR")
    private String nome;

    // Getters e Setters mínimos para o SetorService funcionar
    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }
    
    // Setters (simplificados)
    public void setId(Integer id) {
        this.id = id;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
}