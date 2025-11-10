package com.br.fasipe.estoque.pedidofornecedor.models;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ORDEMCOMPRA")
public class OrdemCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDORDCOMP")
    private Integer id;

    @Column(name = "DATAORDEM", nullable = false)
    private LocalDate dataOrdem;

    @Column(name = "VALOR") // Mapeia para a coluna VALOR do seu DB
    private BigDecimal valor;

    @Column(name = "STATUSORD") // Coluna no DB: STATUSORD
    @Enumerated(EnumType.STRING)
    private StatusOrdemCompra status;

    @Column(name = "DATAPREV") // Mapeia para a coluna DATAPREV do seu DB
    private LocalDate dataprev;

    @Column(name = "DATAENTRE") // Mapeia para a coluna DATAENTRE do seu DB
    private LocalDate dataEntre;

    // --- Mapeamento para ItemOrdemCompra ---
    @OneToMany(mappedBy = "ordemCompra", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<ItemOrdemCompra> itens = new HashSet<>();
    
    // --- Mapeamento CRUCIAL para Lote (FetchType.EAGER é necessário para a soma em memória) ---
    @OneToMany(mappedBy = "ordemCompra", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Lote> lotes = new HashSet<>(); 

    // Construtores
    public OrdemCompra() {
    }

    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDataOrdem() {
        return dataOrdem;
    }

    public void setDataOrdem(LocalDate dataOrdem) {
        this.dataOrdem = dataOrdem;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public StatusOrdemCompra getStatus() {
        return status;
    }

    public void setStatus(StatusOrdemCompra status) {
        this.status = status;
    }

    public Set<ItemOrdemCompra> getItens() {
        return itens;
    }

    public void setItens(Set<ItemOrdemCompra> itens) {
        this.itens = itens;
    }
    
    public Set<Lote> getLotes() { 
        return lotes;
    }

    public void setLotes(Set<Lote> lotes) {
        this.lotes = lotes;
    }

    public LocalDate getDataprev() {
        return dataprev;
    }

    public void setDataprev(LocalDate dataprev) {
        this.dataprev = dataprev;
    }

    public LocalDate getDataEntre() {
        return dataEntre;
    }

    public void setDataEntre(LocalDate dataEntre) {
        this.dataEntre = dataEntre;
    }

    // Métodos utilitários
    public void adicionarItem(ItemOrdemCompra item) {
        this.itens.add(item);
        item.setOrdemCompra(this);
    }
}