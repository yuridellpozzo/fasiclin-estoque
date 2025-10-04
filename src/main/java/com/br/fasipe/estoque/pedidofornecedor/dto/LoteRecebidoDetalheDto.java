package com.br.fasipe.estoque.pedidofornecedor.dto;

import java.time.LocalDate;
import java.util.Objects;

/**
 * DTO para receber os detalhes de um item específico (lote) que está sendo recebido.
 */
public class LoteRecebidoDetalheDto {
    private Integer idProduto;
    private int quantidadeRecebida;
    private LocalDate dataVencimento;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoteRecebidoDetalheDto that = (LoteRecebidoDetalheDto) o;
        return quantidadeRecebida == that.quantidadeRecebida && Objects.equals(idProduto, that.idProduto) && Objects.equals(dataVencimento, that.dataVencimento);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProduto, quantidadeRecebida, dataVencimento);
    }

    @Override
    public String toString() {
        return "LoteRecebidoDetalheDto{" +
                "idProduto=" + idProduto +
                ", quantidadeRecebida=" + quantidadeRecebida +
                ", dataVencimento=" + dataVencimento +
                '}';
    }
}