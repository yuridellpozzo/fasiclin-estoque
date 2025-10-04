// src/main/java/com/br/fasipe/estoque/pedidofornecedor/repository/ItemOrdemCompraRepository.java

package com.br.fasipe.estoque.pedidofornecedor.repository;

import com.br.fasipe.estoque.pedidofornecedor.models.ItemOrdemCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ItemOrdemCompraRepository extends JpaRepository<ItemOrdemCompra, Integer> {

    /**
     * Busca todos os itens (produtos) associados a uma Ordem de Compra específica.
     * @param idOrdemCompra ID da Ordem de Compra
     * @return Lista de ItemOrdemCompra
     */
    List<ItemOrdemCompra> findByOrdemCompraId(Integer idOrdemCompra);
}