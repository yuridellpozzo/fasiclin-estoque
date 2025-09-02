package com.br.fasipe.estoque.ordemcompra.services;

import com.br.fasipe.estoque.ordemcompra.models.Lote;
import java.util.List;
import java.util.Optional;

public interface LoteService {
    Lote createLote(Lote lote);
    Optional<Lote> getLoteById(Integer id);
    List<Lote> getAllLotes();
    Lote updateLote(Integer id, Lote lote);
    void deleteLote(Integer id);
}