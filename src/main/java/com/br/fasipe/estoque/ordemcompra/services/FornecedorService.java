package com.br.fasipe.estoque.ordemcompra.services;

import com.br.fasipe.estoque.ordemcompra.models.Fornecedor;
import java.util.List;
import java.util.Optional;

public interface FornecedorService {
    Fornecedor createFornecedor(Fornecedor fornecedor);
    Optional<Fornecedor> getFornecedorById(Integer id);
    List<Fornecedor> getAllFornecedores();
    Fornecedor updateFornecedor(Integer id, Fornecedor fornecedor);
    void deleteFornecedor(Integer id);
}