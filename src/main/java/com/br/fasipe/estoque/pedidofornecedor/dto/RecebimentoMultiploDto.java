package com.br.fasipe.estoque.pedidofornecedor.dto;

import java.util.List;
import java.util.Objects;

/**
 * DTO principal para o recebimento de múltiplos itens de uma Ordem de Compra.
 */
public class RecebimentoMultiploDto {
    private Integer idOrdemCompra;
    private String login;
    private String senha;
    private List<ItemRecebidoDto> itens;

    public RecebimentoMultiploDto() {
    }

    public RecebimentoMultiploDto(Integer idOrdemCompra, String login, String senha, List<ItemRecebidoDto> itens) {
        this.idOrdemCompra = idOrdemCompra;
        this.login = login;
        this.senha = senha;
        this.itens = itens;
    }

    public Integer getIdOrdemCompra() {
        return idOrdemCompra;
    }

    public void setIdOrdemCompra(Integer idOrdemCompra) {
        this.idOrdemCompra = idOrdemCompra;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public List<ItemRecebidoDto> getItens() {
        return itens;
    }

    public void setItens(List<ItemRecebidoDto> itens) {
        this.itens = itens;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecebimentoMultiploDto that = (RecebimentoMultiploDto) o;
        return Objects.equals(idOrdemCompra, that.idOrdemCompra) && Objects.equals(login, that.login) && Objects.equals(senha, that.senha) && Objects.equals(itens, that.itens);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idOrdemCompra, login, senha, itens);
    }
}