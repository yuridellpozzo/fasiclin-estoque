package com.br.fasipe.estoque.pedidofornecedor.repository;

import com.br.fasipe.estoque.pedidofornecedor.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByLoginAndSenha(String login, String senha);
}