package com.br.fasipe.estoque.pedidofornecedor.models;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "PROFISSIONAL")
public class Profissional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDPROFISSIO")
    private Integer id;

    // --- CAMPO ADICIONADO: ID_DOCUMENTO agora existe na tabela ---
    // Mapeia para a entidade Documento através da coluna ID_DOCUMENTO
    @OneToOne
    @JoinColumn(name = "ID_DOCUMENTO")
    private Documento documento;
    // -------------------------------------------------------------

    @Column(name = "TIPOPROFI") // Mapeia para a coluna ENUM/VARCHAR do BD
    private String tipoProfi; // Usamos String para mapear '1', '2', etc.

    public Profissional() {
    }

    public Profissional(Integer id, String tipoProfi) {
        this.id = id;
        this.tipoProfi = tipoProfi;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Documento getDocumento() {
        return documento;
    }

    public void setDocumento(Documento documento) {
        this.documento = documento;
    }

    public String getTipoProfi() {
        return tipoProfi;
    }

    public void setTipoProfi(String tipoProfi) {
        this.tipoProfi = tipoProfi;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Profissional that = (Profissional) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Profissional{" +
                "id=" + id +
                ", documento=" + (documento != null ? documento.getIdDocumento() : "null") +
                ", tipoProfi='" + tipoProfi + '\'' +
                '}';
    }
}