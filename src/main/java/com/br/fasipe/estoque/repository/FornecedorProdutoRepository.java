package com.br.fasipe.estoque.repository;

import com.br.fasipe.estoque.model.Fornecedor;
import com.br.fasipe.estoque.model.FornecedorProduto;
import com.br.fasipe.estoque.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FornecedorProdutoRepository extends JpaRepository<FornecedorProduto, Integer> {
    
    List<FornecedorProduto> findByFornecedor(Fornecedor fornecedor);
    
    List<FornecedorProduto> findByProduto(Produto produto);
}