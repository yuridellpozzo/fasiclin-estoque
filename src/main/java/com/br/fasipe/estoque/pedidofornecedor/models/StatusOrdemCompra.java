package com.br.fasipe.estoque.pedidofornecedor.models;

/**
 * Define os possíveis status de uma Ordem de Compra.
 */
public enum StatusOrdemCompra {
    PEND, // Pendente (Aberto)
    ANDA, // Em Andamento (Recebimento Parcial)
    CONC, // Concluída (Recebimento Total ou Conclusão Manual)
    CANC  // Cancelada
}