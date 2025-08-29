package com.br.fasipe.estoque.ordemcompra.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.br.fasipe.estoque.ordemcompra.models.OrdemCompra;
import java.time.LocalDate;
import java.util.List;


@Repository
public interface OrdemCompraRepository extends JpaRepository<OrdemCompra, Integer> {
    List<OrdemCompra> findByDataOrdemBetween(LocalDate inicio, LocalDate fim);

     List<OrdemCompra> findByStatus(OrdemCompra.StatusOrdem status);

}

