package com.br.fasipe.estoque.ordemcompra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.fasipe.estoque.ordemcompra.models.Fornecedor;
import com.br.fasipe.estoque.ordemcompra.models.PessoasJuridica;

import java.util.Optional;
import java.util.List;

/**
 * Repositório para a entidade Fornecedor.
 * Fornece métodos para interagir com a tabela FORNECEDOR no banco de dados.
 */
@Repository
public interface FornecedorRepository extends JpaRepository<Fornecedor, Integer> {
    
    /**
     * Busca um fornecedor pela pessoa jurídica associada.
     * 
     * @param pessoasJuridica A pessoa jurídica associada ao fornecedor.
     * @return Um Optional contendo o fornecedor encontrado ou vazio se não existir.
     */
    Optional<Fornecedor> findByPessoasJuridica(PessoasJuridica pessoasJuridica);
    
    /**
     * Busca fornecedores pelo nome do representante.
     * 
     * @param representante O nome do representante a ser buscado.
     * @return Uma lista de fornecedores com o representante especificado.
     */
    List<Fornecedor> findByRepresentanteContainingIgnoreCase(String representante);
    
    /**
     * Verifica se existe um fornecedor para a pessoa jurídica especificada.
     * 
     * @param pessoasJuridica A pessoa jurídica a ser verificada.
     * @return true se existir, false caso contrário.
     */
    boolean existsByPessoasJuridica(PessoasJuridica pessoasJuridica);
}