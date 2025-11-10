package com.br.fasipe.estoque.pedidofornecedor.repository;

import com.br.fasipe.estoque.pedidofornecedor.models.Almoxarifado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AlmoxarifadoRepository extends JpaRepository<Almoxarifado, Integer> {
    
    // Método necessário para o AlmoxarifadoService.findByNome(String nome)
    Optional<Almoxarifado> findByNome(String nome);

    // Método necessário para o AlmoxarifadoService.findBySetor(...)
    Page<Almoxarifado> findBySetorId(Integer setorId, Pageable pageable);
}