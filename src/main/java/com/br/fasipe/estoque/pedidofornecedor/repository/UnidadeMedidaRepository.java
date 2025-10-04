package com.br.fasipe.estoque.pedidofornecedor.repository;

import com.br.fasipe.estoque.pedidofornecedor.models.UnidadeMedida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UnidadeMedidaRepository extends JpaRepository<UnidadeMedida, Integer> {
    Optional<UnidadeMedida> findByDescricao(String descricao);
    Optional<UnidadeMedida> findByAbreviacao(String abreviacao);
}
