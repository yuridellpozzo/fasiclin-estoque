package com.br.fasipe.estoque.pedidofornecedor.models;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "LOTE")
public class Lote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDLOTE")
    private Integer id;

    @Column(name = "DATAVENC")
    private LocalDate dataVencimento;

    @Column(name = "QNTD")
    private Integer quantidade;

    @ManyToOne
    @JoinColumn(name = "ID_ORDCOMP")
    private OrdemCompra ordemCompra;

    // Campo obrigatório para vincular ao Item
    @Column(name = "IDITEM") 
    private Integer idItem;

    // --- CORREÇÃO: Campo Obrigatório NOME_LOTE ---
    @Column(name = "NOME_LOTE")
    private String nomeLote;
    // ---------------------------------------------

    public Lote() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public OrdemCompra getOrdemCompra() {
        return ordemCompra;
    }

    public void setOrdemCompra(OrdemCompra ordemCompra) {
        this.ordemCompra = ordemCompra;
    }

    public Integer getIdItem() {
        return idItem;
    }

    public void setIdItem(Integer idItem) {
        this.idItem = idItem;
    }

    public String getNomeLote() {
        return nomeLote;
    }

    public void setNomeLote(String nomeLote) {
        this.nomeLote = nomeLote;
    }
}