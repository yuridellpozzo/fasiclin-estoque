package com.br.fasipe.estoque.ordemcompra.services.impl;

import com.br.fasipe.estoque.ordemcompra.models.Almoxarifado;
import com.br.fasipe.estoque.ordemcompra.repository.AlmoxarifadoRepository;
import com.br.fasipe.estoque.ordemcompra.services.AlmoxarifadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AlmoxarifadoServiceImpl implements AlmoxarifadoService {

    @Autowired
    private AlmoxarifadoRepository almoxarifadoRepository;

    @Override
    @Transactional
    public Almoxarifado createAlmoxarifado(Almoxarifado almoxarifado) {
        return almoxarifadoRepository.save(almoxarifado);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Almoxarifado> getAlmoxarifadoById(Integer id) {
        return almoxarifadoRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Almoxarifado> getAllAlmoxarifados() {
        return almoxarifadoRepository.findAll();
    }

    @Override
    @Transactional
    public Almoxarifado updateAlmoxarifado(Integer id, Almoxarifado almoxarifadoDetails) {
        Almoxarifado almoxarifado = almoxarifadoRepository.findById(id).orElseThrow(() -> new RuntimeException("Almoxarifado not found"));
        almoxarifado.setNome(almoxarifadoDetails.getNome());
        almoxarifado.setSetor(almoxarifadoDetails.getSetor());
        return almoxarifadoRepository.save(almoxarifado);
    }

    @Override
    @Transactional
    public void deleteAlmoxarifado(Integer id) {
        almoxarifadoRepository.deleteById(id);
    }
}