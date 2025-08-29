package com.br.fasipe.estoque.ordemcompra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.fasipe.estoque.ordemcompra.models.PessoasJuridica;

import java.util.Optional;
import java.util.List;

/**
 * Repositório para a entidade PessoasJuridica.
 * Fornece métodos para interagir com a tabela PESSOAJUR no banco de dados.
 */
@Repository
public interface PessoasJuridicaRepository extends JpaRepository<PessoasJuridica, Integer> {
    
    /**
     * Busca uma pessoa jurídica pelo CNPJ.
     * 
     * @param cnpj O CNPJ da pessoa jurídica a ser buscada.
     * @return Um Optional contendo a pessoa jurídica encontrada ou vazio se não existir.
     */
    Optional<PessoasJuridica> findByCnpj(String cnpj);
    
    /**
     * Busca uma pessoa jurídica pelo ID da pessoa associada.
     * 
     * @param idPessoa O ID da pessoa associada à pessoa jurídica.
     * @return Um Optional contendo a pessoa jurídica encontrada ou vazio se não existir.
     */
    Optional<PessoasJuridica> findByIdPessoa(Integer idPessoa);
    
    /**
     * Busca pessoas jurídicas pela razão social.
     * 
     * @param razaoSocial A razão social a ser buscada.
     * @return Uma lista de pessoas jurídicas com a razão social especificada.
     */
    List<PessoasJuridica> findByRazaoSocialContainingIgnoreCase(String razaoSocial);
    
    /**
     * Busca pessoas jurídicas pelo nome fantasia.
     * 
     * @param nomeFantasia O nome fantasia a ser buscado.
     * @return Uma lista de pessoas jurídicas com o nome fantasia especificado.
     */
    List<PessoasJuridica> findByNomeFantasiaContainingIgnoreCase(String nomeFantasia);
    
    /**
     * Verifica se existe uma pessoa jurídica com o CNPJ especificado.
     * 
     * @param cnpj O CNPJ a ser verificado.
     * @return true se existir, false caso contrário.
     */
    boolean existsByCnpj(String cnpj);
    
    /**
     * Verifica se existe uma pessoa jurídica com o ID de pessoa especificado.
     * 
     * @param idPessoa O ID da pessoa a ser verificado.
     * @return true se existir, false caso contrário.
     */
    boolean existsByIdPessoa(Integer idPessoa);
}