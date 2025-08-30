package com.br.fasipe.estoque.ordemcompra.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.br.fasipe.estoque.ordemcompra.models.Almoxarifado;

@Repository
public interface AlmoxarifadoRepository extends JpaRepository<Almoxarifado, Integer> {
  

}
