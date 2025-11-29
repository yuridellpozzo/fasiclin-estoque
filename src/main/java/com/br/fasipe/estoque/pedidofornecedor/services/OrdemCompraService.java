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
    
    // Repositório da nova tabela ITEM para validação
    @Autowired private ItemRepository itemRepository;
    
    @Autowired private EntityManager entityManager;

    @Transactional
    public String receberPedido(RecebimentoPedidoDto dto) {
        
        // 1. Validação de Usuário
        Usuario usuario = usuarioRepository.findByLoginAndSenha(dto.getLogin(), dto.getSenha())
                .orElseThrow(() -> new RuntimeException("Usuário ou senha inválidos."));

        Profissional profissional = profissionalRepository.findById(usuario.getProfissional().getId())
                .orElseThrow(() -> new RuntimeException("Profissional não encontrado."));

        // Validação de Administrador (Tipo "1")
        if (profissional.getTipoProfi() == null || !profissional.getTipoProfi().equals("1")) {
            throw new RuntimeException("Somente o profissional administrador pode realizar esta operação.");
        }

        // 2. Validação da Ordem
        OrdemCompra ordemCompra = ordemCompraRepository.findById(dto.getIdOrdemCompra())
                .orElseThrow(() -> new RuntimeException("Ordem de compra não encontrada."));

        if (ordemCompra.getStatus().equals(StatusOrdemCompra.CONC)) {
            throw new RuntimeException("A ordem de compra já foi concluída.");
        }

        // 3. Processamento dos Itens
        for (LoteRecebidoDetalheDto itemRecebido : dto.getItensRecebidos()) {
            
            Produto produto = produtoRepository.findById(itemRecebido.getIdProduto())
                    .orElseThrow(() -> new RuntimeException("Produto ID " + itemRecebido.getIdProduto() + " não encontrado."));

            // Validação de existência na tabela ITEM
            if (!itemRepository.existsById(produto.getId())) {
                throw new RuntimeException("ERRO: O produto '" + produto.getNome() + "' (ID " + produto.getId() + ") não existe na tabela ITEM. Cadastre-o antes.");
            }

            ItemOrdemCompra itemDaOrdem = ordemCompra.getItens().stream()
                    .filter(item -> item.getProduto().getId().equals(produto.getId()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Item não encontrado na Ordem de Compra."));
            
            // Abater Quantidade
            int qtdRecebida = itemRecebido.getQuantidadeRecebida();
            int qtdPendente = itemDaOrdem.getQuantidade(); 

            if (qtdRecebida > qtdPendente) {
                throw new RuntimeException("A quantidade recebida (" + qtdRecebida + ") é maior que a pendente (" + qtdPendente + ") para o produto " + produto.getNome());
            }

            int novaQuantidadePendente = qtdPendente - qtdRecebida; 
            itemDaOrdem.setQuantidade(novaQuantidadePendente); 
            itemOrdemCompraRepository.save(itemDaOrdem);
            
            // Criação do Lote
            Lote lote = new Lote();
            lote.setDataVencimento(itemRecebido.getDataVencimento());
            lote.setQuantidade(qtdRecebida); 
            lote.setOrdemCompra(ordemCompra);
            
            // Campos Obrigatórios
            lote.setIdItem(produto.getId()); 
            lote.setDataValidade(itemRecebido.getDataVencimento());   

            if (itemRecebido.getDataFabricacao() != null) {
                lote.setDataFabricacao(itemRecebido.getDataFabricacao());
            } else {
                lote.setDataFabricacao(LocalDate.now()); 
            }

            if (itemRecebido.getNumeroLote() != null && !itemRecebido.getNumeroLote().isEmpty()) {
                lote.setNomeLote(itemRecebido.getNumeroLote());
            } else {
                lote.setNomeLote("AUTO-" + System.currentTimeMillis());
            }
            
            lote.setObservacao("Recebido por ID: " + usuario.getId());
            
            lote = loteRepository.save(lote); 
            
            entityManager.flush();
            entityManager.clear();

            // Atualização de Estoque
            Optional<Estoque> estoqueOpt = estoqueRepository.findByProdutoIdAndLoteId(produto.getId(), lote.getId()); 

            if (estoqueOpt.isEmpty()) { 
                Estoque novoEstoque = new Estoque();
                novoEstoque.setProduto(produto);
                novoEstoque.setLote(lote); 
                novoEstoque.setQuantidadeEstoque(qtdRecebida); 
                estoqueRepository.save(novoEstoque);
            } else { 
                Estoque estoqueExistente = estoqueOpt.get();
                estoqueExistente.setQuantidadeEstoque(estoqueExistente.getQuantidadeEstoque() + qtdRecebida);
                estoqueRepository.save(estoqueExistente);
            }
            
            // Atualiza Status para ANDAMENTO (apenas sai de PENDENTE)
            if (ordemCompra.getStatus() == StatusOrdemCompra.PEND) {
                ordemCompra.setStatus(StatusOrdemCompra.ANDA);
            }
        }
        
        // --- REMOVIDA LÓGICA DE CONCLUSÃO AUTOMÁTICA ---
        // A ordem ficará em 'ANDA' até que você clique em "Concluir Ordem" manualmente.
        
        ordemCompraRepository.save(ordemCompra);
        
        return "Itens recebidos com sucesso! Saldo atualizado.";
    }

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

    @Transactional(readOnly = true) 
    public List<ItemOrdemCompra> getItensDaOrdem(Integer idOrdemCompra) {
        ordemCompraRepository.findById(idOrdemCompra)
            .orElseThrow(() -> new EntityNotFoundException("Ordem de compra não encontrada."));
        return itemOrdemCompraRepository.findByOrdemCompraId(idOrdemCompra); 
    }
    
    public Page<OrdemCompra> findAll(int page, int size) {
        return ordemCompraRepository.findAll(PageRequest.of(page, size));
    }

    public Page<OrdemCompra> findByStatus(StatusOrdemCompra status, int page, int size) {
        return ordemCompraRepository.findByStatus(status, PageRequest.of(page, size));
    }
}