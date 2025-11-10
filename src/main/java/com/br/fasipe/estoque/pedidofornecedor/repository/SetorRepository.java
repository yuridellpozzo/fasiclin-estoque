package com.br.fasipe.estoque.pedidofornecedor.repository;

import com.br.fasipe.estoque.pedidofornecedor.models.Setor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Interface Repository para Setor com métodos mínimos necessários pelo SetorService
 */
public interface SetorRepository extends JpaRepository<Setor, Integer> {
    
    Optional<Setor> findByNome(String nome);
    
    boolean existsByNome(String nome);
}