package com.br.fasipe.estoque.pedidofornecedor.models;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "ESTOQUE")
public class Estoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDESTOQUE")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ID_PRODUTO", nullable = false)
    private Produto produto;

    @ManyToOne
    @JoinColumn(name = "ID_LOTE", nullable = false)
    private Lote lote;

    @Column(name = "QTDESTOQUE")
    private int quantidadeEstoque;

    public Estoque() {
    }

    public Estoque(Integer id, Produto produto, Lote lote, int quantidadeEstoque) {
        this.id = id;
        this.produto = produto;
        this.lote = lote;
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Lote getLote() {
        return lote;
    }

    public void setLote(Lote lote) {
        this.lote = lote;
    }

    public int getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(int quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Estoque estoque = (Estoque) o;
        return Objects.equals(id, estoque.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Estoque{" +
                "id=" + id +
                ", produto=" + (produto != null ? produto.getId() : null) +
                ", lote=" + (lote != null ? lote.getId() : null) +
                ", quantidadeEstoque=" + quantidadeEstoque +
                '}';
    }
}