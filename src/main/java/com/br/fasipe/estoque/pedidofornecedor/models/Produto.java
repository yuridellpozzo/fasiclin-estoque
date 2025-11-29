package com.br.fasipe.estoque.pedidofornecedor.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "PRODUTO")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDPRODUTO")
    private Integer id;

    @Column(name = "NOME")
    private String nome;
    
    @Column(name = "DESCRICAO")
    private String descricao;
    
    @ManyToOne
    @JoinColumn(name = "ID_UNMEDI")
    private UnidadeMedida unidadeMedida;

    @Column(name = "CODBARRAS")
    private String codBarras;

    @Column(name = "TEMPIDEAL")
    private BigDecimal tempIdeal;
    
    @Column(name = "STQMAX")
    private Integer stqMax; 

    @Column(name = "STQMIN")
    private Integer stqMin;

    @Column(name = "PNTPEDIDO")
    private Integer ptnPedido;

    @ManyToOne
    @JoinColumn(name = "ID_ALMOX")
    private Almoxarifado almoxarifado;

    public Produto() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public Integer getStqMax() {
        return stqMax;
    }

    public void setStqMax(Integer stqMax) {
        this.stqMax = stqMax;
    }

    public Integer getStqMin() {
        return stqMin;
    }

    public void setStqMin(Integer stqMin) {
        this.stqMin = stqMin;
    }

    public Integer getPtnPedido() {
        return ptnPedido;
    }

    public void setPtnPedido(Integer ptnPedido) {
        this.ptnPedido = ptnPedido;
    }

    public UnidadeMedida getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(UnidadeMedida unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }

    public String getCodBarras() {
        return codBarras;
    }

    public void setCodBarras(String codBarras) {
        this.codBarras = codBarras;
    }

    public BigDecimal getTempIdeal() {
        return tempIdeal;
    }

    public void setTempIdeal(BigDecimal tempIdeal) {
        this.tempIdeal = tempIdeal;
    }

    public Almoxarifado getAlmoxarifado() {
        return almoxarifado;
    }

    public void setAlmoxarifado(Almoxarifado almoxarifado) {
        this.almoxarifado = almoxarifado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Produto produto = (Produto) o;
        return Objects.equals(id, produto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}