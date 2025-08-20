package com.br.fasipe.estoque.repository;

import com.br.fasipe.estoque.model.Lote;
import com.br.fasipe.estoque.model.OrdemCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LoteRepository extends JpaRepository<Lote, Integer> {
    
    List<Lote> findByOrdemCompra(OrdemCompra ordemCompra);
    
    List<Lote> findByDataVencimentoBefore(LocalDate data);
}