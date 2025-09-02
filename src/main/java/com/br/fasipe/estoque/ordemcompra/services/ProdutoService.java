package com.br.fasipe.estoque.ordemcompra.services;

import com.br.fasipe.estoque.ordemcompra.models.Produto;
import java.util.List;
import java.util.Optional;

public interface ProdutoService {
    Produto createProduto(Produto produto);
    Optional<Produto> getProdutoById(Integer id);
    List<Produto> getAllProdutos();
    Produto updateProduto(Integer id, Produto produto);
    void deleteProduto(Integer id);
}