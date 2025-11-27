package com.br.fasipe.estoque.pedidofornecedor.services;

import com.br.fasipe.estoque.pedidofornecedor.dto.LoteRecebidoDetalheDto;
import com.br.fasipe.estoque.pedidofornecedor.dto.RecebimentoPedidoDto;
import com.br.fasipe.estoque.pedidofornecedor.models.*;
import com.br.fasipe.estoque.pedidofornecedor.repository.*;
import com.br.fasipe.estoque.pedidofornecedor.models.ItemOrdemCompra;
import com.br.fasipe.estoque.pedidofornecedor.models.OrdemCompra;
import com.br.fasipe.estoque.pedidofornecedor.models.StatusOrdemCompra;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional; 

@Service
public class OrdemCompraService {

    private static final Logger log = LoggerFactory.getLogger(OrdemCompraService.class);

    @Autowired private OrdemCompraRepository ordemCompraRepository;
    @Autowired private LoteRepository loteRepository;
    @Autowired private EstoqueRepository estoqueRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private ProfissionalRepository profissionalRepository;
    @Autowired private ProdutoRepository produtoRepository;
    @Autowired private ItemOrdemCompraRepository itemOrdemCompraRepository;
    
    @Autowired private EntityManager entityManager;

    /**
     * Processa o recebimento de itens de uma Ordem de Compra.
     * Cria lotes, atualiza estoque e muda o status da ordem.
     */
    @Transactional
    public String receberPedido(RecebimentoPedidoDto dto) {
        
        // --- 1. Validação de Usuário e Permissão ---
        Usuario usuario = usuarioRepository.findByLoginAndSenha(dto.getLogin(), dto.getSenha())
                .orElseThrow(() -> new RuntimeException("Usuário ou senha inválidos."));

        Profissional profissional = profissionalRepository.findById(usuario.getProfissional().getId())
                .orElseThrow(() -> new RuntimeException("Profissional não encontrado para o usuário."));

        // Validação: Verifica se o tipo do profissional é "1" (Administrador)
        // Importante: Compara como String
        if (profissional.getTipoProfi() == null || !profissional.getTipoProfi().equals("1")) {
            throw new RuntimeException("Somente o profissional administrador pode realizar esta operação.");
        }

        // --- 2. Validação da Ordem de Compra ---
        OrdemCompra ordemCompra = ordemCompraRepository.findById(dto.getIdOrdemCompra())
                .orElseThrow(() -> new RuntimeException("Ordem de compra não encontrada."));

        if (ordemCompra.getStatus().equals(StatusOrdemCompra.CONC)) {
            throw new RuntimeException("A ordem de compra já foi concluída.");
        }

        // --- 3. Processamento dos Itens Recebidos ---
        for (LoteRecebidoDetalheDto itemRecebido : dto.getItensRecebidos()) {
            
            Produto produto = produtoRepository.findById(itemRecebido.getIdProduto())
                    .orElseThrow(() -> new RuntimeException("Produto ID " + itemRecebido.getIdProduto() + " não encontrado."));

            // Busca o item correspondente na OC
            ItemOrdemCompra itemDaOrdem = ordemCompra.getItens().stream()
                    .filter(item -> item.getProduto().getId().equals(produto.getId()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Item com produto ID " + itemRecebido.getIdProduto() + " não encontrado na Ordem de Compra."));
            
            // Lógica de Acúmulo: Atualiza a quantidade recebida diretamente no item
            int quantidadeRecebidaNoLote = itemRecebido.getQuantidadeRecebida();
            int quantidadeAcumuladaAnterior = itemDaOrdem.getQuantidade(); // Valor atual no banco
            int novaQuantidadeRecebidaAcumulada = quantidadeAcumuladaAnterior + quantidadeRecebidaNoLote; 
            
            itemDaOrdem.setQuantidade(novaQuantidadeRecebidaAcumulada); 
            itemOrdemCompraRepository.save(itemDaOrdem);
            
            // Criação do LOTE
            Lote lote = new Lote();
            lote.setDataVencimento(itemRecebido.getDataVencimento());
            lote.setQuantidade(quantidadeRecebidaNoLote); 
            lote.setOrdemCompra(ordemCompra);
            
            // --- PREENCHIMENTO DE CAMPOS OBRIGATÓRIOS DO BANCO ---
            
            // 1. IDITEM (Vínculo com o item da ordem)
            lote.setIdItem(itemDaOrdem.getId()); 
            
            // 2. DATA_VALIDADE (Cópia do vencimento, pois o banco exige)
            lote.setDataValidade(itemRecebido.getDataVencimento());

            // 3. DATA_FABRICACAO
            if (itemRecebido.getDataFabricacao() != null) {
                lote.setDataFabricacao(itemRecebido.getDataFabricacao());
            } else {
                lote.setDataFabricacao(LocalDate.now()); // Fallback se o front não enviar
            }

            // 4. NOME_LOTE
            if (itemRecebido.getNumeroLote() != null && !itemRecebido.getNumeroLote().isEmpty()) {
                lote.setNomeLote(itemRecebido.getNumeroLote());
            } else {
                lote.setNomeLote("AUTO-" + System.currentTimeMillis());
            }
            
            // Opcional: Observação
            lote.setObservacao("Recebido por ID: " + usuario.getId());
            
            lote = loteRepository.save(lote); 
            
            // Limpa cache para garantir que o estoque seja buscado atualizado
            entityManager.flush();
            entityManager.clear();

            // 4. Atualização de Estoque (Soma ou Cria)
            Optional<Estoque> estoqueOpt = estoqueRepository.findByProdutoIdAndLoteId(produto.getId(), lote.getId()); 

            if (estoqueOpt.isEmpty()) { 
                Estoque novoEstoque = new Estoque();
                novoEstoque.setProduto(produto);
                novoEstoque.setLote(lote); 
                novoEstoque.setQuantidadeEstoque(quantidadeRecebidaNoLote); 
                estoqueRepository.save(novoEstoque);
            } else { 
                Estoque estoqueExistente = estoqueOpt.get();
                estoqueExistente.setQuantidadeEstoque(estoqueExistente.getQuantidadeEstoque() + quantidadeRecebidaNoLote);
                estoqueRepository.save(estoqueExistente);
            }
            
            // 5. Atualiza Status da Ordem para "Em Andamento" se for o primeiro recebimento
            if (ordemCompra.getStatus() == StatusOrdemCompra.PEND) {
                ordemCompra.setStatus(StatusOrdemCompra.ANDA);
            }
        }
        
        // Salva o status atualizado da ordem
        ordemCompraRepository.save(ordemCompra);
        
        String mensagem = ordemCompra.getStatus().equals(StatusOrdemCompra.CONC) 
                         ? "Itens recebidos com sucesso! Ordem de Compra CONCLUÍDA."
                         : "Itens recebidos com sucesso! A Ordem de Compra está 'Em Andamento'.";
                         
        return mensagem;
    }

    /**
     * Conclui manualmente uma ordem de compra.
     */
    @Transactional
    public void concluirOrdemManualmente(Integer idOrdemCompra) {
        OrdemCompra ordemCompra = ordemCompraRepository.findById(idOrdemCompra)
                .orElseThrow(() -> new RuntimeException("Ordem de compra não encontrada."));

        if (ordemCompra.getStatus().equals(StatusOrdemCompra.CONC)) {
            throw new RuntimeException("Esta ordem já está concluída.");
        }

        ordemCompra.setStatus(StatusOrdemCompra.CONC);
        ordemCompraRepository.save(ordemCompra);
        log.info("Ordem de Compra #{} concluída manualmente.", idOrdemCompra);
    }

    // --- Métodos de Busca ---

    @Transactional(readOnly = true) 
    public List<ItemOrdemCompra> getItensDaOrdem(Integer idOrdemCompra) {
        ordemCompraRepository.findById(idOrdemCompra)
            .orElseThrow(() -> new EntityNotFoundException("Ordem de compra não encontrada."));

        return itemOrdemCompraRepository.findByOrdemCompraId(idOrdemCompra); 
    }
    
    public Page<OrdemCompra> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ordemCompraRepository.findAll(pageable);
    }

    public Page<OrdemCompra> findByStatus(StatusOrdemCompra status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ordemCompraRepository.findByStatus(status, pageable);
    }
}