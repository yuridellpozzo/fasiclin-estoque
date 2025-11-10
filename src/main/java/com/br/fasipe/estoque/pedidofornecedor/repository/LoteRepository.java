package com.br.fasipe.estoque.pedidofornecedor.repository;

import com.br.fasipe.estoque.pedidofornecedor.models.Lote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LoteRepository extends JpaRepository<Lote, Integer> {
    // No LoteRepository.java
    @Query("SELECT SUM(l.quantidade) FROM Lote l WHERE l.ordemCompra.id = :idOrdemCompra")
    Optional<Integer> sumQuantitiesByOrdemCompraId(Integer idOrdemCompra);
}