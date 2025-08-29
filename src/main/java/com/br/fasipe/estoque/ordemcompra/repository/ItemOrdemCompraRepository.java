package com.br.fasipe.estoque.ordemcompra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.br.fasipe.estoque.ordemcompra.models.ItemOrdemCompra;
import com.br.fasipe.estoque.ordemcompra.models.OrdemCompra;
import com.br.fasipe.estoque.ordemcompra.models.Produto;

import java.util.List;
import java.math.BigDecimal;

/**
 * Repositório para a entidade ItemOrdemCompra.
 * Fornece métodos para interagir com a tabela ITEM_ORDCOMP no banco de dados.
 */
@Repository
public interface ItemOrdemCompraRepository extends JpaRepository<ItemOrdemCompra, Integer> {
    
    /**
     * Busca todos os itens de uma ordem de compra específica.
     * 
     * @param ordemCompra A ordem de compra cujos itens serão buscados.
     * @return Uma lista de itens da ordem de compra.
     */
    List<ItemOrdemCompra> findByOrdemCompra(OrdemCompra ordemCompra);
    
    /**
     * Busca todos os itens que contêm um produto específico.
     * 
     * @param produto O produto a ser buscado nos itens.
     * @return Uma lista de itens que contêm o produto especificado.
     */
    List<ItemOrdemCompra> findByProduto(Produto produto);
    
    /**
     * Calcula o valor total de uma ordem de compra somando o valor de todos os seus itens.
     * 
     * @param ordemCompra A ordem de compra cujo valor total será calculado.
     * @return O valor total da ordem de compra.
     */
    @Query("SELECT SUM(i.valor) FROM ItemOrdemCompra i WHERE i.ordemCompra = :ordemCompra")
    BigDecimal calcularValorTotalOrdemCompra(OrdemCompra ordemCompra);
    
    /**
     * Conta o número de itens em uma ordem de compra.
     * 
     * @param ordemCompra A ordem de compra cujos itens serão contados.
     * @return O número de itens na ordem de compra.
     */
    long countByOrdemCompra(OrdemCompra ordemCompra);
}