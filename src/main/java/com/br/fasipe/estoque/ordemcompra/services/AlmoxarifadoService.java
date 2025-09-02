package com.br.fasipe.estoque.ordemcompra.services;

import com.br.fasipe.estoque.ordemcompra.models.Almoxarifado;
import java.util.List;
import java.util.Optional;

public interface AlmoxarifadoService {
    Almoxarifado createAlmoxarifado(Almoxarifado almoxarifado);
    Optional<Almoxarifado> getAlmoxarifadoById(Integer id);
    List<Almoxarifado> getAllAlmoxarifados();
    Almoxarifado updateAlmoxarifado(Integer id, Almoxarifado almoxarifado);
    void deleteAlmoxarifado(Integer id);
}