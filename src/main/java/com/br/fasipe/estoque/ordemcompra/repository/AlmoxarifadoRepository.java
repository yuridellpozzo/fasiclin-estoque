package com.br.fasipe.estoque.ordemcompra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.fasipe.estoque.ordemcompra.models.Almoxarifado;

import java.util.Optional;

/**
 * Repositório para a entidade Almoxarifado.
 * Fornece métodos para interagir com a tabela ALMOXARIFADO no banco de dados.
 */
@Repository
public interface AlmoxarifadoRepository extends JpaRepository<Almoxarifado, Integer> {
    
    /**
     * Busca um almoxarifado pelo nome.
     * 
     * @param nome O nome do almoxarifado a ser buscado.
     * @return Um Optional contendo o almoxarifado encontrado ou vazio se não existir.
     */
    Optional<Almoxarifado> findByNome(String nome);
    
    /**
     * Verifica se existe um almoxarifado com o nome especificado.
     * 
     * @param nome O nome do almoxarifado a ser verificado.
     * @return true se existir, false caso contrário.
     */
    boolean existsByNome(String nome);
}