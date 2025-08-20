package com.br.fasipe.estoque.repository;

import com.br.fasipe.estoque.model.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FornecedorRepository extends JpaRepository<Fornecedor, Integer> {
    
    Fornecedor findByIdPessoa(Integer idPessoa);
}