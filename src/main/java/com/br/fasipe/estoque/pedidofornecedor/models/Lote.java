package com.br.fasipe.estoque.pedidofornecedor.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "LOTE")
public class Lote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDLOTE")
    private Integer id;

    @Column(name = "DATAVENC")
    private LocalDate dataVencimento;

    @Column(name = "QNTD")
    private int quantidade;

    @ManyToOne
    @JoinColumn(name = "ID_ORDCOMP")
    private OrdemCompra ordemCompra;

    public Lote() {
    }

    public Lote(Integer id, LocalDate dataVencimento, int quantidade, OrdemCompra ordemCompra) {
        this.id = id;
        this.dataVencimento = dataVencimento;
        this.quantidade = quantidade;
        this.ordemCompra = ordemCompra;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public OrdemCompra getOrdemCompra() {
        return ordemCompra;
    }

    public void setOrdemCompra(OrdemCompra ordemCompra) {
        this.ordemCompra = ordemCompra;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lote lote = (Lote) o;
        return Objects.equals(id, lote.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Lote{" +
                "id=" + id +
                ", dataVencimento=" + dataVencimento +
                ", quantidade=" + quantidade +
                ", ordemCompraId=" + (ordemCompra != null ? ordemCompra.getId() : "null") +
                '}';
    }
}