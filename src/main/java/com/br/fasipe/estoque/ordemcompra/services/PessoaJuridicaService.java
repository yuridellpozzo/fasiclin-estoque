package com.br.fasipe.estoque.ordemcompra.services;

import com.br.fasipe.estoque.ordemcompra.models.PessoaJuridica;
import java.util.List;
import java.util.Optional;

public interface PessoaJuridicaService {
    PessoaJuridica createPessoaJuridica(PessoaJuridica pessoaJuridica);
    Optional<PessoaJuridica> getPessoaJuridicaById(Integer id);
    List<PessoaJuridica> getAllPessoasJuridicas();
    PessoaJuridica updatePessoaJuridica(Integer id, PessoaJuridica pessoaJuridica);
    void deletePessoaJuridica(Integer id);
}