package com.br.fasipe.estoque.pedidofornecedor.services;

import com.br.fasipe.estoque.pedidofornecedor.models.Lote;
import com.br.fasipe.estoque.pedidofornecedor.repository.LoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class LoteService {

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(LoteService.class);

    @Autowired
    private LoteRepository loteRepository;

    /**
     * Exemplo de método findById (apenas para estruturação).
     */
    public Optional<Lote> findById(Integer id) {
        return loteRepository.findById(id);
    }

    public List<Lote> findAll() {
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }
    
    // Você pode adicionar outros métodos de CRUD (Create, Read, Update, Delete) aqui,
    // mas por agora, apenas o essencial para que a classe exista e compile.
}