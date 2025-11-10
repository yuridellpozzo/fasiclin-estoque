package com.br.fasipe.estoque.pedidofornecedor.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonBackReference;
@Entity
@Table(name = "ITEM_ORDCOMP")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ItemOrdemCompra {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDITEMORD")
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_ORDCOMP", nullable = false)
    @JsonBackReference
    private OrdemCompra ordemCompra;

    @NotNull(message = "O produto deve ser informado.")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_PRODUTO", nullable = false)
    private Produto produto;

    @Positive(message = "A quantidade deve ser maior que zero.")
    @Column(name = "QNTD", nullable = false)
    private int quantidade;

    @NotNull(message = "O valor do item é obrigatório.")
    @Positive(message = "O valor do item deve ser positivo.")
    @Column(name = "VALOR", nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @NotNull(message = "A data de vencimento é obrigatória.")
    @Column(name = "DATAVENC", nullable = false)
    private LocalDate dataVencimento;

    public ItemOrdemCompra() {
    }

    public ItemOrdemCompra(Integer id, OrdemCompra ordemCompra, Produto produto, int quantidade, BigDecimal valor, LocalDate dataVencimento) {
        this.id = id;
        this.ordemCompra = ordemCompra;
        this.produto = produto;
        this.quantidade = quantidade;
        this.valor = valor;
        this.dataVencimento = dataVencimento;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public OrdemCompra getOrdemCompra() {
        return ordemCompra;
    }

    public void setOrdemCompra(OrdemCompra ordemCompra) {
        this.ordemCompra = ordemCompra;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
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
        ItemOrdemCompra that = (ItemOrdemCompra) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ItemOrdemCompra{" +
                "id=" + id +
                ", ordemCompraId=" + (ordemCompra != null ? ordemCompra.getId() : null) +
                ", produtoId=" + (produto != null ? produto.getId() : null) +
                ", quantidade=" + quantidade +
                ", valor=" + valor +
                ", dataVencimento=" + dataVencimento +
                '}';
    }
}
