package com.br.fasipe.estoque.pedidofornecedor.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Objects;

@Entity
@Table(name = "UNIMEDIDA")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UnidadeMedida {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDUNMEDI")
    private Integer id;

    @NotNull(message = "Descrição é obrigatória")
    @Size(max = 50, message = "Descrição deve ter no máximo 50 caracteres")
    @Column(name = "DESCRICAO", nullable = false, length = 50)
    private String descricao;

    @NotNull(message = "Abreviação é obrigatória")
    @Size(max = 3, message = "Abreviação deve ter no máximo 3 caracteres")
    @Column(name = "UNIABREV", nullable = false, unique = true, length = 3)
    private String abreviacao;

    public UnidadeMedida() {
    }

    public UnidadeMedida(Integer id, String descricao, String abreviacao) {
        this.id = id;
        this.descricao = descricao;
        this.abreviacao = abreviacao;
    }

    public UnidadeMedida(String descricao, String abreviacao) {
        this.descricao = descricao;
        this.abreviacao = abreviacao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getAbreviacao() {
        return abreviacao;
    }

    public void setAbreviacao(String abreviacao) {
        this.abreviacao = abreviacao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnidadeMedida that = (UnidadeMedida) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "UnidadeMedida{" +
                "id=" + id +
                ", descricao='" + descricao + '\'' +
                ", abreviacao='" + abreviacao + '\'' +
                '}';
    }
}
