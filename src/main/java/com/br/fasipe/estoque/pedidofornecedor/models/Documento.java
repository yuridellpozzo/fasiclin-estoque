package com.br.fasipe.estoque.pedidofornecedor.models;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "DOCUMENTO")
public class Documento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DOCUMENTO") 
    private String iddocumento;

    // Adicione aqui outros campos da tabela DOCUMENTO se houver (ex: numero, tipo, etc.)
    // @Column(name = "NUMERO")
    // private String numero;

    public Documento() {
    }

    public Documento(String iddocumento) {
        this.iddocumento = iddocumento; 
    }

    public String getIdDocumento() {
        return iddocumento;
    }

    public void setIdDocumento(String iddocumento) {
        this.iddocumento = iddocumento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Documento documento = (Documento) o;
        return Objects.equals(iddocumento, documento.iddocumento);
    }

    @Override
    public int hashCode() {
        return Objects.hash(iddocumento);
    }
}