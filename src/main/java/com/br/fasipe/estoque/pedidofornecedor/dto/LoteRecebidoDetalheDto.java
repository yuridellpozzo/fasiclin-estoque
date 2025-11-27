package com.br.fasipe.estoque.pedidofornecedor.dto;

import java.time.LocalDate;
import java.util.Objects;

public class LoteRecebidoDetalheDto {
    private Integer idProduto;
    private int quantidadeRecebida;
    private LocalDate dataVencimento;
    private LocalDate dataFabricacao; // Campo obrigatório
    private String numeroLote; 

    public LoteRecebidoDetalheDto() {
    }

    public LoteRecebidoDetalheDto(Integer idProduto, int quantidadeRecebida, LocalDate dataVencimento) {
        this.idProduto = idProduto;
        this.quantidadeRecebida = quantidadeRecebida;
        this.dataVencimento = dataVencimento;
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

    public LocalDate getDataFabricacao() {
        return dataFabricacao; // CORRIGIDO: Agora retorna a variável certa
    }

    public void setDataFabricacao(LocalDate dataFabricacao) {
        this.dataFabricacao = dataFabricacao;
    }

    public String getNumeroLote() {
        return numeroLote;
    }

    public void setNumeroLote(String numeroLote) {
        this.numeroLote = numeroLote;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoteRecebidoDetalheDto that = (LoteRecebidoDetalheDto) o;
        return quantidadeRecebida == that.quantidadeRecebida && 
               Objects.equals(idProduto, that.idProduto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProduto, quantidadeRecebida);
    }
}