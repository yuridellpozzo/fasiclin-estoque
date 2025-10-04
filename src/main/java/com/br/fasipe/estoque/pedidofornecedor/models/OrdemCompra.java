package com.br.fasipe.estoque.pedidofornecedor.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "ORDEMCOMPRA")
public class OrdemCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDORDCOMP")
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUSORD", nullable = false)
    private StatusOrdem status;

    @Column(name = "DATAPREV")
    private LocalDate dataPrevisao;
    
    @Column(name = "DATAORDEM") // <--- CAMPO CRUCIAL ADICIONADO AQUI
    private LocalDate dataOrdem;

    @Column(name = "DATAENTRE")
    private LocalDate dataEntrega;

    @Column(name = "VALOR")
    private BigDecimal valor;

    @OneToMany(mappedBy = "ordemCompra", fetch = FetchType.EAGER) // CORRIGIDO: de LAZY para EAGER
    @JsonManagedReference
    private List<ItemOrdemCompra> itens;

    public enum StatusOrdem {
        PEND, ANDA, CONC // Mapeia para 'PEND', 'ANDA', 'CONC' no banco
    }

    public OrdemCompra() {
    }

    // Adicione o dataOrdem no construtor se ele for usado.
    public OrdemCompra(Integer id, StatusOrdem status, LocalDate dataPrevisao, LocalDate dataOrdem, LocalDate dataEntrega, BigDecimal valor, List<ItemOrdemCompra> itens) {
        this.id = id;
        this.status = status;
        this.dataPrevisao = dataPrevisao;
        this.dataOrdem = dataOrdem; // Adicionado ao construtor
        this.dataEntrega = dataEntrega;
        this.valor = valor;
        this.itens = itens;
    }

    // GETTERS e SETTERS adicionados/corrigidos:
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public StatusOrdem getStatus() { return status; }
    public void setStatus(StatusOrdem status) { this.status = status; }

    public LocalDate getDataPrevisao() { return dataPrevisao; }
    public void setDataPrevisao(LocalDate dataPrevisao) { this.dataPrevisao = dataPrevisao; }

    public LocalDate getDataOrdem() { return dataOrdem; } // <--- NOVO GETTER
    public void setDataOrdem(LocalDate dataOrdem) { this.dataOrdem = dataOrdem; } // <--- NOVO SETTER

    public LocalDate getDataEntrega() { return dataEntrega; }
    public void setDataEntrega(LocalDate dataEntrega) { this.dataEntrega = dataEntrega; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public List<ItemOrdemCompra> getItens() { return itens; }
    public void setItens(List<ItemOrdemCompra> itens) { this.itens = itens; }
    
    // Mantenha equals, hashcode e toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrdemCompra that = (OrdemCompra) o;
        return Objects.equals(id, that.id);
    }

    @Override public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() {
        return "OrdemCompra{" + "id=" + id + ", status=" + status + ", dataOrdem=" + dataOrdem + ", valor=" + valor + '}';
    }
}