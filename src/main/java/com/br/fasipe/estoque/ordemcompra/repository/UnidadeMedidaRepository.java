package com.br.fasipe.estoque.ordemcompra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.fasipe.estoque.ordemcompra.models.UnidadeMedida;

import java.util.Optional;

/**
 * Repositório para a entidade UnidadeMedida.
 * Fornece métodos para interagir com a tabela UNIMEDIDA no banco de dados.
 */
@Repository
public interface UnidadeMedidaRepository extends JpaRepository<UnidadeMedida, Integer> {
    
    /**
     * Busca uma unidade de medida pela descrição.
     * 
     * @param descricao A descrição da unidade de medida a ser buscada.
     * @return Um Optional contendo a unidade de medida encontrada ou vazio se não existir.
     */
    Optional<UnidadeMedida> findByDescricao(String descricao);
    
    /**
     * Busca uma unidade de medida pela abreviação.
     * 
     * @param abreviacao A abreviação da unidade de medida a ser buscada.
     * @return Um Optional contendo a unidade de medida encontrada ou vazio se não existir.
     */
    Optional<UnidadeMedida> findByAbreviacao(String abreviacao);
    
    /**
     * Verifica se existe uma unidade de medida com a abreviação especificada.
     * 
     * @param abreviacao A abreviação da unidade de medida a ser verificada.
     * @return true se existir, false caso contrário.
     */
    boolean existsByAbreviacao(String abreviacao);
}