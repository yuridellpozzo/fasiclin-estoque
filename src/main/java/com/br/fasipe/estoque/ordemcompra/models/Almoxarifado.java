package com.br.fasipe.estoque.ordemcompra.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "ALMOXARIFADO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Almoxarifado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDALMOX")
    private Integer id;

    // A tabela SETOR seria outra entidade a ser criada
    // @ManyToOne
    // @JoinColumn(name = "ID_SETOR", nullable = false)
    // private Setor setor;

    @Column(name = "NOMEALMO", nullable = false, unique = true, length = 100)
    private String nome;

}
