package com.br.fasipe.estoque.ordemcompra.services.impl;

import com.br.fasipe.estoque.ordemcompra.models.PessoaJuridica;
import com.br.fasipe.estoque.ordemcompra.repository.PessoaJuridicaRepository;
import com.br.fasipe.estoque.ordemcompra.services.PessoaJuridicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PessoaJuridicaServiceImpl implements PessoaJuridicaService {

    @Autowired
    private PessoaJuridicaRepository pessoaJuridicaRepository;

    @Override
    @Transactional
    public PessoaJuridica createPessoaJuridica(PessoaJuridica pessoaJuridica) {
        return pessoaJuridicaRepository.save(pessoaJuridica);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PessoaJuridica> getPessoaJuridicaById(Integer id) {
        return pessoaJuridicaRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PessoaJuridica> getAllPessoasJuridicas() {
        return pessoaJuridicaRepository.findAll();
    }

    @Override
    @Transactional
    public PessoaJuridica updatePessoaJuridica(Integer id, PessoaJuridica pessoaJuridicaDetails) {
        PessoaJuridica pessoaJuridica = pessoaJuridicaRepository.findById(id).orElseThrow(() -> new RuntimeException("PessoaJuridica not found"));
        pessoaJuridica.setRazaoSocial(pessoaJuridicaDetails.getRazaoSocial());
        pessoaJuridica.setNomeFantasia(pessoaJuridicaDetails.getNomeFantasia());
        pessoaJuridica.setCnpj(pessoaJuridicaDetails.getCnpj());
        pessoaJuridica.setCnae(pessoaJuridicaDetails.getCnae());
        return pessoaJuridicaRepository.save(pessoaJuridica);
    }

    @Override
    @Transactional
    public void deletePessoaJuridica(Integer id) {
        pessoaJuridicaRepository.deleteById(id);
    }
}