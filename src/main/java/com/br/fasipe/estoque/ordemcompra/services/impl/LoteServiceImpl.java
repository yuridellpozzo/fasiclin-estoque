package com.br.fasipe.estoque.ordemcompra.services.impl;

import com.br.fasipe.estoque.ordemcompra.models.Lote;
import com.br.fasipe.estoque.ordemcompra.repository.LoteRepository;
import com.br.fasipe.estoque.ordemcompra.services.LoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LoteServiceImpl implements LoteService {

    @Autowired
    private LoteRepository loteRepository;

    @Override
    @Transactional
    public Lote createLote(Lote lote) {
        return loteRepository.save(lote);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Lote> getLoteById(Integer id) {
        return loteRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Lote> getAllLotes() {
        return loteRepository.findAll();
    }

    @Override
    @Transactional
    public Lote updateLote(Integer id, Lote loteDetails) {
        Lote lote = loteRepository.findById(id).orElseThrow(() -> new RuntimeException("Lote not found"));
        lote.setOrdemCompra(loteDetails.getOrdemCompra());
        lote.setDataVencimento(loteDetails.getDataVencimento());
        lote.setQuantidade(loteDetails.getQuantidade());
        return loteRepository.save(lote);
    }

    @Override
    @Transactional
    public void deleteLote(Integer id) {
        loteRepository.deleteById(id);
    }
}