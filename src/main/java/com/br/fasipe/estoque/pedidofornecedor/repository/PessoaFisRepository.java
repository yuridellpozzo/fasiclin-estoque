package com.br.fasipe.estoque.pedidofornecedor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.br.fasipe.estoque.pedidofornecedor.models.PessoaFis;

@Repository
public interface PessoaFisRepository extends JpaRepository<PessoaFis, Integer> {
}
