package com.br.fasipe.estoque.pedidofornecedor.controllers;

import com.br.fasipe.estoque.pedidofornecedor.dto.ProfissionalDto; // Mantido
import com.br.fasipe.estoque.pedidofornecedor.services.ProfissionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/profissionais")
@CrossOrigin(origins = "*") 
public class ProfissionalController {

    private static final Logger log = LoggerFactory.getLogger(ProfissionalController.class);

    @Autowired
    private ProfissionalService profissionalService;

    /**
     * Endpoint para listar o Profissional Administrador (ID 1).
     * URL: GET /api/profissionais/estoque
     */
    @GetMapping("/estoque")
    public List<ProfissionalDto> getProfissionaisEstoque() {
        log.info("Buscando profissional de estoque (administrador)");
        // Chama o novo método do serviço que busca apenas o ADM (ID 1)
        return profissionalService.findAdminProfissional(); 
    }
}