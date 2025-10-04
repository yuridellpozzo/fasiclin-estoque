package com.br.fasipe.estoque.pedidofornecedor.dto;

import java.util.List;
import java.util.Objects;

/**
 * DTO para o payload principal do recebimento de itens.
 */
public class RecebimentoPedidoDto {
    private Integer idOrdemCompra;
    private Integer idResponsavel;
    private String login;
    private String senha;
    private List<LoteRecebidoDetalheDto> itensRecebidos;

    public RecebimentoPedidoDto() {
    }

    public RecebimentoPedidoDto(Integer idOrdemCompra, Integer idResponsavel, String login, String senha, List<LoteRecebidoDetalheDto> itensRecebidos) {
        this.idOrdemCompra = idOrdemCompra;
        this.idResponsavel = idResponsavel;
        this.login = login;
        this.senha = senha;
        this.itensRecebidos = itensRecebidos;
    }

    public Integer getIdOrdemCompra() {
        return idOrdemCompra;
    }

    public void setIdOrdemCompra(Integer idOrdemCompra) {
        this.idOrdemCompra = idOrdemCompra;
    }

    public Integer getIdResponsavel() {
        return idResponsavel;
    }

    public void setIdResponsavel(Integer idResponsavel) {
        this.idResponsavel = idResponsavel;
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

    public List<LoteRecebidoDetalheDto> getItensRecebidos() {
        return itensRecebidos;
    }

    public void setItensRecebidos(List<LoteRecebidoDetalheDto> itensRecebidos) {
        this.itensRecebidos = itensRecebidos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecebimentoPedidoDto that = (RecebimentoPedidoDto) o;
        return Objects.equals(idOrdemCompra, that.idOrdemCompra) && Objects.equals(idResponsavel, that.idResponsavel) && Objects.equals(login, that.login) && Objects.equals(senha, that.senha) && Objects.equals(itensRecebidos, that.itensRecebidos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idOrdemCompra, idResponsavel, login, senha, itensRecebidos);
    }
}