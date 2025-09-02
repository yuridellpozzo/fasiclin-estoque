package com.br.fasipe.estoque.ordemcompra.services.impl;

import com.br.fasipe.estoque.ordemcompra.models.ItemOrdemCompra;
import com.br.fasipe.estoque.ordemcompra.repository.ItemOrdemCompraRepository;
import com.br.fasipe.estoque.ordemcompra.services.ItemOrdemCompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ItemOrdemCompraServiceImpl implements ItemOrdemCompraService {

    @Autowired
    private ItemOrdemCompraRepository itemOrdemCompraRepository;

    @Override
    @Transactional
    public ItemOrdemCompra createItemOrdemCompra(ItemOrdemCompra itemOrdemCompra) {
        return itemOrdemCompraRepository.save(itemOrdemCompra);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ItemOrdemCompra> getItemOrdemCompraById(Integer id) {
        return itemOrdemCompraRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemOrdemCompra> getAllItensOrdemCompra() {
        return itemOrdemCompraRepository.findAll();
    }

    @Override
    @Transactional
    public ItemOrdemCompra updateItemOrdemCompra(Integer id, ItemOrdemCompra itemOrdemCompraDetails) {
        ItemOrdemCompra itemOrdemCompra = itemOrdemCompraRepository.findById(id).orElseThrow(() -> new RuntimeException("ItemOrdemCompra not found"));
        itemOrdemCompra.setOrdemCompra(itemOrdemCompraDetails.getOrdemCompra());
        itemOrdemCompra.setProduto(itemOrdemCompraDetails.getProduto());
        itemOrdemCompra.setQuantidade(itemOrdemCompraDetails.getQuantidade());
        itemOrdemCompra.setValor(itemOrdemCompraDetails.getValor());
        itemOrdemCompra.setDataVencimento(itemOrdemCompraDetails.getDataVencimento());
        return itemOrdemCompraRepository.save(itemOrdemCompra);
    }

    @Override
    @Transactional
    public void deleteItemOrdemCompra(Integer id) {
        itemOrdemCompraRepository.deleteById(id);
    }
}