package com.br.fasipe.estoque.pedidofornecedor.repository;

import com.br.fasipe.estoque.pedidofornecedor.models.OrdemCompra;
import com.br.fasipe.estoque.pedidofornecedor.models.OrdemCompra.StatusOrdem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;

@Repository
public interface OrdemCompraRepository extends JpaRepository<OrdemCompra, Integer> {

    // QUERY PROBLEMA FOI REMOVIDA
    // @Query("SELECT o FROM OrdemCompra o WHERE o.dataOrdem BETWEEN :dataInicio AND :dataFim")
    // Page<OrdemCompra> findByDataOrdemBetween(...) <--- REMOVIDO!
    
    // Spring Data JPA implementa esta busca automaticamente
    Page<OrdemCompra> findByStatus(StatusOrdem status, Pageable pageable);

    // MÉTODOS MANTIDOS:
    Page<OrdemCompra> findByValor(BigDecimal valor, Pageable pageable);

    Page<OrdemCompra> findByDataPrevisao(LocalDate dataPrevisao, Pageable pageable);

    Page<OrdemCompra> findByDataOrdem(LocalDate dataOrdem, Pageable pageable);

    Page<OrdemCompra> findByDataEntrega(LocalDate dataEntrega, Pageable pageable);

    long countByStatus(StatusOrdem status);
}