package com.br.fasipe.estoque.pedidofornecedor.services;

import com.br.fasipe.estoque.pedidofornecedor.dto.LoteRecebidoDetalheDto;
import com.br.fasipe.estoque.pedidofornecedor.dto.RecebimentoPedidoDto;
import com.br.fasipe.estoque.pedidofornecedor.models.*;
import com.br.fasipe.estoque.pedidofornecedor.models.OrdemCompra.StatusOrdem;
import com.br.fasipe.estoque.pedidofornecedor.repository.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;



@Service
public class OrdemCompraService {

    private static final Logger log = LoggerFactory.getLogger(OrdemCompraService.class);

    // Repositórios injetados...
    @Autowired private OrdemCompraRepository ordemCompraRepository;
    @Autowired private LoteRepository loteRepository;
    @Autowired private EstoqueRepository estoqueRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private ProfissionalRepository profissionalRepository;
    @Autowired private ProdutoRepository produtoRepository;
    @Autowired private ItemOrdemCompraRepository itemOrdemCompraRepository;


    @Transactional
    public String receberPedido(RecebimentoPedidoDto dto) {
        
        // --- 1. VALIDAÇÃO DE USUÁRIO E REGRA DE NEGÓCIO ---
        
        Usuario usuario = usuarioRepository.findByLoginAndSenha(dto.getLogin(), dto.getSenha())
                .orElseThrow(() -> new RuntimeException("Usuário ou senha inválidos."));

        Profissional profissional = profissionalRepository.findById(usuario.getProfissional().getId())
                .orElseThrow(() -> new RuntimeException("Profissional não encontrado para o usuário."));

        if (!profissional.getId().equals(1)) {
            throw new RuntimeException("Somente o profissional administrador (ID 1) pode realizar esta operação.");
        }
        
        // --- 2. VALIDAÇÃO DA ORDEM DE COMPRA ---
        OrdemCompra ordemCompra = ordemCompraRepository.findById(dto.getIdOrdemCompra())
                .orElseThrow(() -> new RuntimeException("Ordem de compra não encontrada."));

        if (ordemCompra.getStatus().equals(StatusOrdem.CONC)) {
            throw new RuntimeException("A ordem de compra já foi concluída.");
        }

        // --- 3. PROCESSAMENTO DE MÚLTIPLOS ITENS RECEBIDOS ---
        
        for (LoteRecebidoDetalheDto itemRecebido : dto.getItensRecebidos()) {
            
            Produto produto = produtoRepository.findById(itemRecebido.getIdProduto())
                    .orElseThrow(() -> new RuntimeException("Produto ID " + itemRecebido.getIdProduto() + " não encontrado."));

            // ATUALIZA A QUANTIDADE RECEBIDA NO ItemOrdemCompra
            ItemOrdemCompra itemDaOrdem = ordemCompra.getItens().stream()
                    .filter(item -> item.getProduto().getId().equals(produto.getId()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Item com produto ID " + itemRecebido.getIdProduto() + " não encontrado na Ordem de Compra."));
            
            int novaQuantidadeRecebida = itemDaOrdem.getQuantidade() + itemRecebido.getQuantidadeRecebida();
            itemDaOrdem.setQuantidade(novaQuantidadeRecebida);
            itemOrdemCompraRepository.save(itemDaOrdem);


            // CRIAÇÃO DO LOTE (O campo Produto é removido, pois não existe na tabela LOTE)
            Lote lote = new Lote();
            lote.setDataVencimento(itemRecebido.getDataVencimento());
            lote.setQuantidade(itemRecebido.getQuantidadeRecebida()); 
            lote.setOrdemCompra(ordemCompra);
            lote = loteRepository.save(lote); 

            // BUSCA/SOMA ESTOQUE
            Estoque estoque = estoqueRepository.findByProdutoId(produto.getId()) 
                    .orElse(new Estoque()); 

            if (estoque.getId() == null) { 
                estoque.setProduto(produto);
                estoque.setLote(lote); 
                estoque.setQuantidadeEstoque(itemRecebido.getQuantidadeRecebida()); 
            } else { 
                estoque.setProduto(produto); 
                estoque.setQuantidadeEstoque(estoque.getQuantidadeEstoque() + itemRecebido.getQuantidadeRecebida());
                estoque.setLote(lote); 
            }
            estoqueRepository.save(estoque);
        }
        
        // --- 4. ATUALIZA STATUS DA OC PARA 'EM ANDAMENTO' (RECEBIMENTO PARCIAL) ---
        if (ordemCompra.getStatus() == StatusOrdem.PEND) {
            ordemCompra.setStatus(StatusOrdem.ANDA);
            ordemCompraRepository.save(ordemCompra);
        }
        
        // O método findByStatus não é mais chamado para evitar a L.I.E.
        // O front-end recarrega os dados.
        return "Itens recebidos com sucesso! A Ordem de Compra está 'Em Andamento'.";
    }

    @Transactional
    public void concluirOrdemManualmente(Integer idOrdemCompra) {
        // 1. Busca a Ordem
        OrdemCompra ordemCompra = ordemCompraRepository.findById(idOrdemCompra)
                .orElseThrow(() -> new RuntimeException("Ordem de compra não encontrada."));

        // 2. Validação: Só pode ser concluída se estiver PENDENTE ou EM ANDAMENTO
        if (ordemCompra.getStatus() == StatusOrdem.CONC) {
            throw new RuntimeException("Esta ordem já está concluída.");
        }

        // 3. Conclusão Final
        ordemCompra.setStatus(StatusOrdem.CONC);
        ordemCompra.setDataEntrega(LocalDate.now());
        ordemCompraRepository.save(ordemCompra);
        log.info("Ordem de Compra #{} concluída manualmente.", idOrdemCompra);
    }


    // MÉTODOS DE BUSCA (Mantidos para funcionalidade do front-end)

    @Transactional(readOnly = true) // <-- Adicionei a transação que estava faltando
    public List<ItemOrdemCompra> getItensDaOrdem(Integer idOrdemCompra) {
        ordemCompraRepository.findById(idOrdemCompra)
            .orElseThrow(() -> new RuntimeException("Ordem de compra não encontrada."));

        return itemOrdemCompraRepository.findByOrdemCompraId(idOrdemCompra);
    }
    
    public Page<OrdemCompra> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ordemCompraRepository.findAll(pageable);
    }

    public Page<OrdemCompra> findByStatus(StatusOrdem status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ordemCompraRepository.findByStatus(status, pageable);
    }
}