package com.br.fasipe.estoque.ordemcompra.services;

import com.br.fasipe.estoque.ordemcompra.models.OrdemCompra;
import java.util.List;
import java.util.Optional;

public interface OrdemCompraService {
    OrdemCompra createOrdemCompra(OrdemCompra ordemCompra);
    Optional<OrdemCompra> getOrdemCompraById(Integer id);
    List<OrdemCompra> getAllOrdensCompra();
    OrdemCompra updateOrdemCompra(Integer id, OrdemCompra ordemCompra);
    void deleteOrdemCompra(Integer id);
}