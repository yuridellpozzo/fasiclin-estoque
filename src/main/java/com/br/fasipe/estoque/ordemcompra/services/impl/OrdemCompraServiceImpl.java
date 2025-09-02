package com.br.fasipe.estoque.ordemcompra.services.impl;

import com.br.fasipe.estoque.ordemcompra.models.OrdemCompra;
import com.br.fasipe.estoque.ordemcompra.repository.OrdemCompraRepository;
import com.br.fasipe.estoque.ordemcompra.services.OrdemCompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OrdemCompraServiceImpl implements OrdemCompraService {

    @Autowired
    private OrdemCompraRepository ordemCompraRepository;

    @Override
    @Transactional
    public OrdemCompra createOrdemCompra(OrdemCompra ordemCompra) {
        return ordemCompraRepository.save(ordemCompra);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrdemCompra> getOrdemCompraById(Integer id) {
        return ordemCompraRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdemCompra> getAllOrdensCompra() {
        return ordemCompraRepository.findAll();
    }

    @Override
    @Transactional
    public OrdemCompra updateOrdemCompra(Integer id, OrdemCompra ordemCompraDetails) {
        OrdemCompra ordemCompra = ordemCompraRepository.findById(id).orElseThrow(() -> new RuntimeException("OrdemCompra not found"));
        ordemCompra.setStatus(ordemCompraDetails.getStatus());
        ordemCompra.setValor(ordemCompraDetails.getValor());
        ordemCompra.setDataPrevisao(ordemCompraDetails.getDataPrevisao());
        ordemCompra.setDataOrdem(ordemCompraDetails.getDataOrdem());
        ordemCompra.setDataEntrega(ordemCompraDetails.getDataEntrega());
        return ordemCompraRepository.save(ordemCompra);
    }

    @Override
    @Transactional
    public void deleteOrdemCompra(Integer id) {
        ordemCompraRepository.deleteById(id);
    }
}