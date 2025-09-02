package com.br.fasipe.estoque.ordemcompra.services;

import com.br.fasipe.estoque.ordemcompra.models.ItemOrdemCompra;
import java.util.List;
import java.util.Optional;

public interface ItemOrdemCompraService {
    ItemOrdemCompra createItemOrdemCompra(ItemOrdemCompra itemOrdemCompra);
    Optional<ItemOrdemCompra> getItemOrdemCompraById(Integer id);
    List<ItemOrdemCompra> getAllItensOrdemCompra();
    ItemOrdemCompra updateItemOrdemCompra(Integer id, ItemOrdemCompra itemOrdemCompra);
    void deleteItemOrdemCompra(Integer id);
}