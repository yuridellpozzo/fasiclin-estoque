package com.br.fasipe.estoque.ordemcompra.services.impl;

import com.br.fasipe.estoque.ordemcompra.models.Fornecedor;
import com.br.fasipe.estoque.ordemcompra.repository.FornecedorRepository;
import com.br.fasipe.estoque.ordemcompra.services.FornecedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class FornecedorServiceImpl implements FornecedorService {

    @Autowired
    private FornecedorRepository fornecedorRepository;

    @Override
    @Transactional
    public Fornecedor createFornecedor(Fornecedor fornecedor) {
        return fornecedorRepository.save(fornecedor);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Fornecedor> getFornecedorById(Integer id) {
        return fornecedorRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Fornecedor> getAllFornecedores() {
        return fornecedorRepository.findAll();
    }

    @Override
    @Transactional
    public Fornecedor updateFornecedor(Integer id, Fornecedor fornecedorDetails) {
        Fornecedor fornecedor = fornecedorRepository.findById(id).orElseThrow(() -> new RuntimeException("Fornecedor not found"));
        fornecedor.setPessoasJuridica(fornecedorDetails.getPessoasJuridica());
        fornecedor.setRepresentante(fornecedorDetails.getRepresentante());
        fornecedor.setContatoRepresentante(fornecedorDetails.getContatoRepresentante());
        fornecedor.setDescricao(fornecedorDetails.getDescricao());
        return fornecedorRepository.save(fornecedor);
    }

    @Override
    @Transactional
    public void deleteFornecedor(Integer id) {
        fornecedorRepository.deleteById(id);
    }
}