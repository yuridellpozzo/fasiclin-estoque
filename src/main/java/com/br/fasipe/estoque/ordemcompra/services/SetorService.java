package com.br.fasipe.estoque.ordemcompra.services;

import com.br.fasipe.estoque.ordemcompra.models.Setor;
import java.util.List;
import java.util.Optional;

public interface SetorService {
    Setor createSetor(Setor setor);
    Optional<Setor> getSetorById(Integer id);
    List<Setor> getAllSetores();
    Setor updateSetor(Integer id, Setor setor);
    void deleteSetor(Integer id);
}