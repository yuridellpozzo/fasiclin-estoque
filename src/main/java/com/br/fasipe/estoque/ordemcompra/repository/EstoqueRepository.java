package com.br.fasipe.estoque.ordemcompra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.br.fasipe.estoque.ordemcompra.models.Estoque;
import com.br.fasipe.estoque.ordemcompra.models.Produto;

import java.util.Optional;

@Repository
public interface EstoqueRepository extends JpaRepository<Estoque, Integer> {

    
    @Query("SELECT SUM(e.quantidadeEstoque) FROM Estoque e WHERE e.produto = :produto")
    Optional<Integer> findTotalQuantidadeByProduto(Produto produto);
    
    Optional<Estoque> findByProdutoAndLoteId(Produto produto, Integer loteId);
}
