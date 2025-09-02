package com.br.fasipe.estoque.ordemcompra.services.impl;

import com.br.fasipe.estoque.ordemcompra.models.Setor;
import com.br.fasipe.estoque.ordemcompra.repository.SetorRepository;
import com.br.fasipe.estoque.ordemcompra.services.SetorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SetorServiceImpl implements SetorService {

    @Autowired
    private SetorRepository setorRepository;

    @Override
    @Transactional
    public Setor createSetor(Setor setor) {
        return setorRepository.save(setor);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Setor> getSetorById(Integer id) {
        return setorRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Setor> getAllSetores() {
        return setorRepository.findAll();
    }

    @Override
    @Transactional
    public Setor updateSetor(Integer id, Setor setorDetails) {
        Setor setor = setorRepository.findById(id).orElseThrow(() -> new RuntimeException("Setor not found"));
        setor.setNome(setorDetails.getNome());
        return setorRepository.save(setor);
    }

    @Override
    @Transactional
    public void deleteSetor(Integer id) {
        setorRepository.deleteById(id);
    }
}