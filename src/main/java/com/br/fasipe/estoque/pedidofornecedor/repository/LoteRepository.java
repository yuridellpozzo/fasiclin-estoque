package com.br.fasipe.estoque.pedidofornecedor.repository;

import com.br.fasipe.estoque.pedidofornecedor.models.Lote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoteRepository extends JpaRepository<Lote, Integer> {

    // REMOVIDO: O método sumQuantidadeByOrdemCompraIdAndProdutoId
    // Ele causa um erro de mapeamento pois a coluna ID_PRODUTO não existe na tabela LOTE.
}