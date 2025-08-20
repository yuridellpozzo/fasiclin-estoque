package com.br.fasipe.estoque.repository;

import com.br.fasipe.estoque.model.ItemOrdemCompra;
import com.br.fasipe.estoque.model.OrdemCompra;
import com.br.fasipe.estoque.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemOrdemCompraRepository extends JpaRepository<ItemOrdemCompra, Integer> {
    
    List<ItemOrdemCompra> findByOrdemCompra(OrdemCompra ordemCompra);
    
    List<ItemOrdemCompra> findByProduto(Produto produto);
}