package com.br.fasipe.estoque.pedidofornecedor.repository;

import com.br.fasipe.estoque.pedidofornecedor.models.Fornecedor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FornecedorRepository extends JpaRepository<Fornecedor, Integer> {
    List<Fornecedor> findByRepresentante(String representante);
    List<Fornecedor> findByContatoRepresentante(String contatoRepresentante);
    Page<Fornecedor> findByPessoasJuridicaNomeFantasiaContainingIgnoreCase(String nomeFantasia, Pageable pageable);
}
