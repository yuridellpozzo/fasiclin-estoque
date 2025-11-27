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

    // Coluna obrigatória conforme o print
    @Column(name = "DATA_VALIDADE")
    private LocalDate dataValidade;

    // Coluna obrigatória conforme o print
    @Column(name = "DATA_FABRICACAO")
    private LocalDate dataFabricacao;

    @Column(name = "QNTD")
    private Integer quantidade;

    @ManyToOne
    @JoinColumn(name = "ID_ORDCOMP")
    private OrdemCompra ordemCompra;

    @Column(name = "IDITEM") 
    private Integer idItem;

    @Column(name = "NOME_LOTE")
    private String nomeLote;

    // Coluna opcional (não marcada como Not Null no print), mas mapeada
    @Column(name = "OBSERVACAO")
    private String observacao;

    public Lote() {
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public LocalDate getDataVencimento() { return dataVencimento; }
    public void setDataVencimento(LocalDate dataVencimento) { this.dataVencimento = dataVencimento; }

    public LocalDate getDataValidade() { return dataValidade; }
    public void setDataValidade(LocalDate dataValidade) { this.dataValidade = dataValidade; }

    public LocalDate getDataFabricacao() { return dataFabricacao; }
    public void setDataFabricacao(LocalDate dataFabricacao) { this.dataFabricacao = dataFabricacao; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

    public OrdemCompra getOrdemCompra() { return ordemCompra; }
    public void setOrdemCompra(OrdemCompra ordemCompra) { this.ordemCompra = ordemCompra; }

    public Integer getIdItem() { return idItem; }
    public void setIdItem(Integer idItem) { this.idItem = idItem; }

    public String getNomeLote() { return nomeLote; }
    public void setNomeLote(String nomeLote) { this.nomeLote = nomeLote; }

    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }
}