package com.br.fasipe.estoque.ordemcompra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.br.fasipe.estoque.ordemcompra.models.Estoque;

@Repository
public interface EstoqueRepository extends JpaRepository<Estoque, Integer> {
    
}
