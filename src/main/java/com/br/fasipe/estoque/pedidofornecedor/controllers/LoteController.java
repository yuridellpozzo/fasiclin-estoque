package com.br.fasipe.estoque.pedidofornecedor.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.br.fasipe.estoque.pedidofornecedor.models.Lote;
import com.br.fasipe.estoque.pedidofornecedor.services.LoteService;

import java.util.List;

@RestController
@RequestMapping("/api/lotes")
@CrossOrigin(origins = "*")
public class LoteController {
    
    private final LoteService loteService;

    public LoteController(LoteService loteService) {
        this.loteService = loteService;
    }

    @GetMapping
    public ResponseEntity<List<Lote>> listarLotes() {
        List<Lote> lotes = loteService.findAll();
        return ResponseEntity.ok(lotes);
    }
}
