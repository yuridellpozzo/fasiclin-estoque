package com.br.fasipe.estoque.pedidofornecedor.dto;

import com.br.fasipe.estoque.pedidofornecedor.models.Produto;
import java.util.Objects;

/**
 * DTO para transportar o status de um item de uma Ordem de Compra para o frontend.
 * Inclui a quantidade pedida e a quantidade total já recebida.
 */
public class ItemStatusDto {
    private Produto produto;
    private int quantidadePedida;
    private int quantidadeRecebida;

    public ItemStatusDto() {
    }

    public ItemStatusDto(Produto produto, int quantidadePedida, int quantidadeRecebida) {
        this.produto = produto;
        this.quantidadePedida = quantidadePedida;
        this.quantidadeRecebida = quantidadeRecebida;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public int getQuantidadePedida() {
        return quantidadePedida;
    }

    public void setQuantidadePedida(int quantidadePedida) {
        this.quantidadePedida = quantidadePedida;
    }

    public int getQuantidadeRecebida() {
        return quantidadeRecebida;
    }

    public void setQuantidadeRecebida(int quantidadeRecebida) {
        this.quantidadeRecebida = quantidadeRecebida;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemStatusDto that = (ItemStatusDto) o;
        return quantidadePedida == that.quantidadePedida && quantidadeRecebida == that.quantidadeRecebida && Objects.equals(produto, that.produto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(produto, quantidadePedida, quantidadeRecebida);
    }
}