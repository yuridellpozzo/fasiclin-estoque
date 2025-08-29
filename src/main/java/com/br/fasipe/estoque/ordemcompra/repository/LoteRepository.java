package com.br.fasipe.estoque.ordemcompra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.fasipe.estoque.ordemcompra.models.Lote;
import com.br.fasipe.estoque.ordemcompra.models.OrdemCompra;

import java.time.LocalDate;
import java.util.List;

/**
 * Repositório para a entidade Lote.
 * Fornece métodos para interagir com a tabela LOTE no banco de dados.
 */
@Repository
public interface LoteRepository extends JpaRepository<Lote, Integer> {
    
    /**
     * Busca todos os lotes de uma ordem de compra específica.
     * 
     * @param ordemCompra A ordem de compra cujos lotes serão buscados.
     * @return Uma lista de lotes da ordem de compra.
     */
    List<Lote> findByOrdemCompra(OrdemCompra ordemCompra);
    
    /**
     * Busca todos os lotes com data de vencimento anterior à data especificada.
     * 
     * @param data A data de referência para buscar lotes vencidos.
     * @return Uma lista de lotes com data de vencimento anterior à data especificada.
     */
    List<Lote> findByDataVencimentoBefore(LocalDate data);
    
    /**
     * Busca todos os lotes com data de vencimento entre as datas especificadas.
     * 
     * @param dataInicio A data de início do período.
     * @param dataFim A data de fim do período.
     * @return Uma lista de lotes com data de vencimento entre as datas especificadas.
     */
    List<Lote> findByDataVencimentoBetween(LocalDate dataInicio, LocalDate dataFim);
    
    /**
     * Conta o número de lotes de uma ordem de compra.
     * 
     * @param ordemCompra A ordem de compra cujos lotes serão contados.
     * @return O número de lotes na ordem de compra.
     */
    long countByOrdemCompra(OrdemCompra ordemCompra);
}