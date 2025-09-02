package com.br.fasipe.estoque.ordemcompra.services.impl;

import com.br.fasipe.estoque.ordemcompra.models.UnidadeMedida;
import com.br.fasipe.estoque.ordemcompra.repository.UnidadeMedidaRepository;
import com.br.fasipe.estoque.ordemcompra.services.UnidadeMedidaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UnidadeMedidaServiceImpl implements UnidadeMedidaService {

    @Autowired
    private UnidadeMedidaRepository unidadeMedidaRepository;

    @Override
    @Transactional
    public UnidadeMedida createUnidadeMedida(UnidadeMedida unidadeMedida) {
        return unidadeMedidaRepository.save(unidadeMedida);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UnidadeMedida> getUnidadeMedidaById(Integer id) {
        return unidadeMedidaRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UnidadeMedida> getAllUnidadesMedida() {
        return unidadeMedidaRepository.findAll();
    }

    @Override
    @Transactional
    public UnidadeMedida updateUnidadeMedida(Integer id, UnidadeMedida unidadeMedidaDetails) {
        UnidadeMedida unidadeMedida = unidadeMedidaRepository.findById(id).orElseThrow(() -> new RuntimeException("UnidadeMedida not found"));
        unidadeMedida.setDescricao(unidadeMedidaDetails.getDescricao());
        unidadeMedida.setAbreviacao(unidadeMedidaDetails.getAbreviacao());
        return unidadeMedidaRepository.save(unidadeMedida);
    }

    @Override
    @Transactional
    public void deleteUnidadeMedida(Integer id) {
        unidadeMedidaRepository.deleteById(id);
    }
}