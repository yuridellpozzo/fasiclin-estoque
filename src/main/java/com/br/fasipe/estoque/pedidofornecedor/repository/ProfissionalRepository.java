package com.br.fasipe.estoque.pedidofornecedor.repository;

import com.br.fasipe.estoque.pedidofornecedor.models.Profissional;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

// Removemos a importação de java.util.Optional pois o método não é mais declarado aqui.

public interface ProfissionalRepository extends JpaRepository<Profissional, Integer> {

    List<Profissional> findByTipoProfi(String tipoProfi);
    // Opcional: Se quiser um método customizado, crie-o aqui.
}