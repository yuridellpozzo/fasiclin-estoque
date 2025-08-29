package com.br.fasipe.estoque.ordemcompra.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.fasipe.estoque.ordemcompra.models.Produto;

import java.util.List;

/**
 * Repositório para a entidade Produto.
 * Fornece métodos para interagir com a tabela PRODUTO no banco de dados.
 */
@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {

    // O Spring Data JPA cria a query automaticamente a partir do nome do método.
    // Exemplo: Encontrar produtos por nome.
    List<Produto> findByNomeContainingIgnoreCase(String nome);
}
