package com.br.fasipe.estoque.pedidofornecedor.dto;

import java.time.LocalDate;

public class LoteRecebidoDetalheDto {
    private Integer idProduto;
    private int quantidadeRecebida;
    private LocalDate dataVencimento;
    
    // Campo para receber o nome do lote da tela
    private String numeroLote; 

    public LoteRecebidoDetalheDto() {
    }

    public Integer getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Integer idProduto) {
        this.idProduto = idProduto;
    }

    public int getQuantidadeRecebida() {
        return quantidadeRecebida;
    }

    public void setQuantidadeRecebida(int quantidadeRecebida) {
        this.quantidadeRecebida = quantidadeRecebida;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public String getNumeroLote() {
        return numeroLote;
    }

    public void setNumeroLote(String numeroLote) {
        this.numeroLote = numeroLote;
    }
}