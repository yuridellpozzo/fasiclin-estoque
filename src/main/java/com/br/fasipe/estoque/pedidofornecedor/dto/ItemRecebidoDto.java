package com.br.fasipe.estoque.pedidofornecedor.dto;

import java.time.LocalDate;
import java.util.Objects;

/**
 * DTO para representar um único item recebido do formulário do frontend.
 */
public class ItemRecebidoDto {
    private Integer idProduto;
    private int quantidadeRecebida;
    private LocalDate dataVencimento;
    private String codigoLote; // NOVO CAMPO para rastreio

    public ItemRecebidoDto() {
    }

    public ItemRecebidoDto(Integer idProduto, int quantidadeRecebida, LocalDate dataVencimento, String codigoLote) {
        this.idProduto = idProduto;
        this.quantidadeRecebida = quantidadeRecebida;
        this.dataVencimento = dataVencimento;
        this.codigoLote = codigoLote;
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

    public String getCodigoLote() {
        return codigoLote;
    }

    public void setCodigoLote(String codigoLote) {
        this.codigoLote = codigoLote;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemRecebidoDto that = (ItemRecebidoDto) o;
        return quantidadeRecebida == that.quantidadeRecebida && Objects.equals(idProduto, that.idProduto) && Objects.equals(dataVencimento, that.dataVencimento) && Objects.equals(codigoLote, that.codigoLote);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProduto, quantidadeRecebida, dataVencimento, codigoLote);
    }

    @Override
    public String toString() {
        return "ItemRecebidoDto{" +
                "idProduto=" + idProduto +
                ", quantidadeRecebida=" + quantidadeRecebida +
                ", dataVencimento=" + dataVencimento +
                ", codigoLote='" + codigoLote + '\'' +
                '}';
    }
}