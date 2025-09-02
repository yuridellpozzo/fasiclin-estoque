package com.br.fasipe.estoque.ordemcompra.services;

import com.br.fasipe.estoque.ordemcompra.models.Estoque;
import java.util.List;
import java.util.Optional;

public interface EstoqueService {
    Estoque createEstoque(Estoque estoque);
    Optional<Estoque> getEstoqueById(Integer id);
    List<Estoque> getAllEstoques();
    Estoque updateEstoque(Integer id, Estoque estoque);
    void deleteEstoque(Integer id);
    int updateQuantidadeEstoque(Integer quantidadeEstoque, Integer id);
}