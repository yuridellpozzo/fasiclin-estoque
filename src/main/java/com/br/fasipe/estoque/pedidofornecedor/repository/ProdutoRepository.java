package com.br.fasipe.estoque.pedidofornecedor.repository;

import com.br.fasipe.estoque.pedidofornecedor.models.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
    Optional<Produto> findByNome(String nome);
    Page<Produto> findByAlmoxarifadoId(Integer idAlmoxarifado, Pageable pageable);
    Optional<Produto> findByCodBarras(String codBarras);
    Page<Produto> findByTempIdeal(BigDecimal tempIdeal, Pageable pageable);

}
