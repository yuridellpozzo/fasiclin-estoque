package com.br.fasipe.estoque.pedidofornecedor.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller para administração de conexões do banco de dados.
 * Permite visualizar e encerrar processos do MySQL.
 * Use com cuidado, pois pode afetar a estabilidade da aplicação.
 */
// ALERTA DE SEGURANÇA: Este controller expõe funcionalidades de administração
// do banco de dados diretamente via API. Ele DEVE ser protegido com autenticação
// e autorização rigorosas (ex: Spring Security com role 'ROLE_ADMIN').
@RestController
@RequestMapping("/api/admin/db")
@CrossOrigin(origins = "*")
public class DatabaseAdminController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Lista todas as conexões ativas no banco de dados.
     * Executa o comando 'SHOW FULL PROCESSLIST'.
     * @return Uma lista de mapas, onde cada mapa representa uma conexão ativa.
     */
    @GetMapping("/connections")
    public ResponseEntity<List<Map<String, Object>>> getActiveConnections() {
        List<Map<String, Object>> connections = jdbcTemplate.queryForList("SHOW FULL PROCESSLIST");
        return ResponseEntity.ok(connections);
    }

    /**
     * Encerra (mata) uma conexão específica pelo seu ID de processo.
     * @param id O ID do processo da conexão a ser encerrada.
     * @return Uma resposta de sucesso ou erro.
     */
    @PostMapping("/kill/{id}")
    public ResponseEntity<String> killConnection(@PathVariable Long id) {
        try {
            jdbcTemplate.execute("KILL " + id);
            return ResponseEntity.ok("Conexão " + id + " encerrada com sucesso.");
        } catch (Exception e) {
            
            return ResponseEntity.badRequest().body("Falha ao encerrar a conexão " + id + ": " + e.getMessage());
        }
    }
}
