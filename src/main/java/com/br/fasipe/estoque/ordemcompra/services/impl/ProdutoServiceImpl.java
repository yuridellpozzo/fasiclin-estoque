package com.br.fasipe.estoque.ordemcompra.services.impl;

import com.br.fasipe.estoque.ordemcompra.models.Produto;
import com.br.fasipe.estoque.ordemcompra.repository.ProdutoRepository;
import com.br.fasipe.estoque.ordemcompra.services.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoServiceImpl implements ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Override
    @Transactional
    public Produto createProduto(Produto produto) {
        return produtoRepository.save(produto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Produto> getProdutoById(Integer id) {
        return produtoRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Produto> getAllProdutos() {
        return produtoRepository.findAll();
    }

    @Override
    @Transactional
    public Produto updateProduto(Integer id, Produto produtoDetails) {
        Produto produto = produtoRepository.findById(id).orElseThrow(() -> new RuntimeException("Produto not found"));
        produto.setNome(produtoDetails.getNome());
        produto.setDescricao(produtoDetails.getDescricao());
        produto.setUnidadeMedida(produtoDetails.getUnidadeMedida());
        return produtoRepository.save(produto);
    }

    @Override
    @Transactional
    public void deleteProduto(Integer id) {
        produtoRepository.deleteById(id);
    }
}