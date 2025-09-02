package com.br.fasipe.estoque.ordemcompra.services.impl;

import com.br.fasipe.estoque.ordemcompra.models.Estoque;
import com.br.fasipe.estoque.ordemcompra.repository.EstoqueRepository;
import com.br.fasipe.estoque.ordemcompra.services.EstoqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EstoqueServiceImpl implements EstoqueService {

    @Autowired
    private EstoqueRepository estoqueRepository;

    @Override
    @Transactional
    public Estoque createEstoque(Estoque estoque) {
        return estoqueRepository.save(estoque);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Estoque> getEstoqueById(Integer id) {
        return estoqueRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Estoque> getAllEstoques() {
        return estoqueRepository.findAll();
    }

    @Override
    @Transactional
    public Estoque updateEstoque(Integer id, Estoque estoqueDetails) {
        Estoque estoque = estoqueRepository.findById(id).orElseThrow(() -> new RuntimeException("Estoque not found"));
        estoque.setProduto(estoqueDetails.getProduto());
        estoque.setLote(estoqueDetails.getLote());
        estoque.setQuantidadeEstoque(estoqueDetails.getQuantidadeEstoque());
        return estoqueRepository.save(estoque);
    }

    @Override
    @Transactional
    public void deleteEstoque(Integer id) {
        estoqueRepository.deleteById(id);
    }

    @Override
    @Transactional
    public int updateQuantidadeEstoque(Integer quantidadeEstoque, Integer id) {
        return estoqueRepository.updateQuantidadeEstoque(quantidadeEstoque, id);
    }
}