package com.br.fasipe.estoque.pedidofornecedor.repository;

import com.br.fasipe.estoque.pedidofornecedor.models.Estoque;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EstoqueRepository extends JpaRepository<Estoque, Integer> {
    Page<Estoque> findByProdutoId(Integer idProduto, Pageable pageable);
    Page<Estoque> findByLoteId(Integer idLote, Pageable pageable);
    Page<Estoque> findByProdutoAlmoxarifadoId(Integer idAlmoxarifado, Pageable pageable);

    @Query("SELECT e FROM Estoque e WHERE e.quantidadeEstoque < :quantidade")
    Page<Estoque> findByQuantidadeEstoqueLessThan(Integer quantidade, Pageable pageable);

    @Query("SELECT count(e) FROM Estoque e WHERE e.quantidadeEstoque < :quantidade")
    long countByQuantidadeEstoqueLessThan(Integer quantidade);
    Optional<Estoque> findByProdutoIdAndLoteId(Integer idProduto, Integer idLote);

    // MÉTODO NECESSÁRIO PARA BUSCAR O REGISTRO DE ESTOQUE TOTAL PELO PRODUTO
    Optional<Estoque> findByProdutoId(Integer idProduto); 
}
