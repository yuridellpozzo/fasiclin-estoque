package com.br.fasipe.estoque.pedidofornecedor.repository;

import com.br.fasipe.estoque.pedidofornecedor.models.Almoxarifado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlmoxarifadoRepository extends JpaRepository<Almoxarifado, Integer> {
    Optional<Almoxarifado> findByNome(String nome);
    Page<Almoxarifado> findBySetorId(Integer idSetor, Pageable pageable);
}
