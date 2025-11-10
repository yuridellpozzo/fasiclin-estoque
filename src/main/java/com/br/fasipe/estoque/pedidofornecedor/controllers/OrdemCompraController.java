package com.br.fasipe.estoque.pedidofornecedor.controllers;

import com.br.fasipe.estoque.pedidofornecedor.models.StatusOrdemCompra;
import com.br.fasipe.estoque.pedidofornecedor.dto.RecebimentoPedidoDto;
import com.br.fasipe.estoque.pedidofornecedor.models.ItemOrdemCompra;
import com.br.fasipe.estoque.pedidofornecedor.models.OrdemCompra;
import com.br.fasipe.estoque.pedidofornecedor.services.OrdemCompraService;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@RestController
@RequestMapping("/api/ordens-de-compra")
@CrossOrigin(origins = "*") // Permite requisições de outras origens

public class OrdemCompraController {

    private static final Logger log = LoggerFactory.getLogger(OrdemCompraController.class);

    @Autowired
    
    private OrdemCompraService ordemCompraService;

    /**
     * Endpoint GET para buscar todos os itens de uma Ordem de Compra específica.
     * URL: GET /api/ordens-de-compra/{id}/itens
     */
    @Transactional(readOnly = true)
    @GetMapping("/{id}/itens")
    public ResponseEntity<List<ItemOrdemCompra>> listarItensDaOrdem(@PathVariable Integer id) {
        log.info("Buscando itens para a ordem de compra ID: {}", id);
        // A exceção para "não encontrado" já é lançada pelo service.
        // É uma boa prática criar um @ControllerAdvice para capturar exceções
        // e retornar o status HTTP apropriado (ex: 404 Not Found).
        List<ItemOrdemCompra> itens = ordemCompraService.getItensDaOrdem(id);
        return itens.isEmpty() 
            ? ResponseEntity.noContent().build() 
            : ResponseEntity.ok(itens);
    }

    /**
     * Novo endpoint para receber múltiplos itens de uma OC e dar baixa.
     */
    @PostMapping("/receber-itens")
    public ResponseEntity<String> receberItens(@RequestBody RecebimentoPedidoDto dto) {
        try {
            log.info("Recebendo itens para a ordem de compra ID: {}", dto.getIdOrdemCompra());
            String mensagemDeRetorno = ordemCompraService.receberPedido(dto);
            return ResponseEntity.ok(mensagemDeRetorno);
        } catch (RuntimeException e) {
            // Captura a exceção de validação (ADM, OC concluída, etc.)
            log.error("Erro ao receber itens: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}/concluir")
    public ResponseEntity<String> concluirOrdem(@PathVariable Integer id) {
        try {
            log.info("Tentando concluir manualmente a Ordem de Compra #{}", id);
            ordemCompraService.concluirOrdemManualmente(id);
            return ResponseEntity.ok("Ordem de Compra #" + id + " concluída com sucesso!");
        } catch (RuntimeException e) {
            log.error("Erro ao concluir OC #{}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<Object> listarOrdensPorStatus(
        @RequestParam(required = false) String status,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
    
        try {
            log.info("Listando ordens de compra com status: {}, página: {}, tamanho: {}", status, page, size);
            // Se o status for nulo, vazio ou "all", busca todas as ordens (implementar no service)
            if (status == null || status.trim().isEmpty() || "all".equalsIgnoreCase(status)) {
                return ResponseEntity.ok(ordemCompraService.findAll(page, size));
            }

            // Converte a String (ex: "CONC") para o Enum StatusOrdem
            StatusOrdemCompra statusEnum = StatusOrdemCompra.valueOf(status.toUpperCase());
            Page<OrdemCompra> ordens = ordemCompraService.findByStatus(statusEnum, page, size);
            
            // Valida se a página não está vazia.
            if (ordens.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(ordens);

        } catch (IllegalArgumentException e) {
            // Captura se o status enviado na URL for inválido
            log.warn("Status de ordem de compra inválido fornecido: {}", status);
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            // Captura outros erros
            log.error("Erro interno ao listar ordens de compra", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}