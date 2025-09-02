package com.br.fasipe.estoque.ordemcompra.services;

import com.br.fasipe.estoque.ordemcompra.models.UnidadeMedida;
import java.util.List;
import java.util.Optional;

public interface UnidadeMedidaService {
    UnidadeMedida createUnidadeMedida(UnidadeMedida unidadeMedida);
    Optional<UnidadeMedida> getUnidadeMedidaById(Integer id);
    List<UnidadeMedida> getAllUnidadesMedida();
    UnidadeMedida updateUnidadeMedida(Integer id, UnidadeMedida unidadeMedida);
    void deleteUnidadeMedida(Integer id);
}