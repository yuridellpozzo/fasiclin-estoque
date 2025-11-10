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

import java.util.List;
import java.util.Optional; 

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
    
    @Autowired private EntityManager entityManager; // Para limpeza de cache/contexto JPA


    /**
     * SOLUÇÃO DE CONTORNO FUNCIONAL (Mínimo): O campo QNTD do ItemOrdemCompra é sobrescrito 
     * com o total recebido acumulado, perdendo a quantidade pedida original.
     */
    @Transactional
    public String receberPedido(RecebimentoPedidoDto dto) {
        
        // --- 1. VALIDAÇÃO DE USUÁRIO E REGRA DE NEGÓCIO ---
        
        Usuario usuario = usuarioRepository.findByLoginAndSenha(dto.getLogin(), dto.getSenha())
                .orElseThrow(() -> new RuntimeException("Usuário ou senha inválidos."));

        Profissional profissional = profissionalRepository.findById(usuario.getProfissional().getId())
                .orElseThrow(() -> new RuntimeException("Profissional não encontrado para o usuário."));

        // >>> VALIDAÇÃO CORRIGIDA: Checa se o TIPOPROFI é igual à string "1" <<<
        // Se tipoProfi é String, a comparação deve ser feita com String.
        if (profissional.getTipoProfi() == null || !profissional.getTipoProfi().equals("1")) {
            throw new RuntimeException("Somente o profissional administrador pode realizar esta operação.");
        }
        // <<< FIM DA VALIDAÇÃO CORRIGIDA >>>


        // --- 2. VALIDAÇÃO DA ORDEM DE COMPRA ---
        OrdemCompra ordemCompra = ordemCompraRepository.findById(dto.getIdOrdemCompra())
                .orElseThrow(() -> new RuntimeException("Ordem de compra não encontrada."));

        if (ordemCompra.getStatus().equals(StatusOrdemCompra.CONC)) {
            throw new RuntimeException("A ordem de compra já foi concluída.");
        }

        // --- 3. PROCESSAMENTO DE MÚLTIPLOS ITENS RECEBIDOS ---
        
        for (LoteRecebidoDetalheDto itemRecebido : dto.getItensRecebidos()) {
            
            Produto produto = produtoRepository.findById(itemRecebido.getIdProduto())
                    .orElseThrow(() -> new RuntimeException("Produto ID " + itemRecebido.getIdProduto() + " não encontrado."));

            // BUSCA O ItemOrdemCompra
            ItemOrdemCompra itemDaOrdem = ordemCompra.getItens().stream()
                    .filter(item -> item.getProduto().getId().equals(produto.getId()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Item com produto ID " + itemRecebido.getIdProduto() + " não encontrado na Ordem de Compra."));
            
            
            // LÓGICA DE CONTORNO: O campo 'quantidade' (QNTD) é tratado como QNTD Recebida Acumulada.
            int quantidadeRecebidaNoLote = itemRecebido.getQuantidadeRecebida();
            int quantidadeAcumuladaAnterior = itemDaOrdem.getQuantidade();
            int novaQuantidadeRecebidaAcumulada = quantidadeAcumuladaAnterior + quantidadeRecebidaNoLote; 
            
            // ATUALIZA A QNTD (SOBRESCRITA)
            itemDaOrdem.setQuantidade(novaQuantidadeRecebidaAcumulada); 
            itemOrdemCompraRepository.save(itemDaOrdem);
            
            // CRIAÇÃO DO LOTE
            Lote lote = new Lote();
            lote.setDataVencimento(itemRecebido.getDataVencimento());
            lote.setQuantidade(quantidadeRecebidaNoLote); 
            lote.setOrdemCompra(ordemCompra);
            lote = loteRepository.save(lote); 
            
            // Limpeza de Cache
            entityManager.flush();
            entityManager.clear();

            // 4. BUSCA/SOMA ESTOQUE POR PRODUTO E LOTE
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
            
            // 5. ATUALIZA O STATUS PARA EM ANDAMENTO
            if (ordemCompra.getStatus() == StatusOrdemCompra.PEND) {
                ordemCompra.setStatus(StatusOrdemCompra.ANDA);
            }
        }
        
        // --- 6. ATUALIZAÇÃO FINAL DO STATUS DA ORDEM DE COMPRA ---
        ordemCompraRepository.save(ordemCompra);
        
        String mensagem = ordemCompra.getStatus().equals(StatusOrdemCompra.CONC) 
                         ? "Itens recebidos com sucesso! Ordem de Compra CONCLUÍDA."
                         : "Itens recebidos com sucesso! A Ordem de Compra está 'Em Andamento'.";
                         
        return mensagem;
    }
    
    // MÉTODOS DE BUSCA (Corrigidos para Compilação)

    @Transactional
    public void concluirOrdemManualmente(Integer idOrdemCompra) {
        // 1. Busca a Ordem
        OrdemCompra ordemCompra = ordemCompraRepository.findById(idOrdemCompra)
                .orElseThrow(() -> new RuntimeException("Ordem de compra não encontrada."));

        // 2. Validação: Só pode ser concluída se estiver PENDENTE ou EM ANDAMENTO
        if (ordemCompra.getStatus().equals(StatusOrdemCompra.CONC)) {
            throw new RuntimeException("Esta ordem já está concluída.");
        }

        // 3. Conclusão Final
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
        Pageable pageable = PageRequest.of(page, size);
        return ordemCompraRepository.findAll(pageable);
    }

    public Page<OrdemCompra> findByStatus(StatusOrdemCompra status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ordemCompraRepository.findByStatus(status, pageable);
    }
}