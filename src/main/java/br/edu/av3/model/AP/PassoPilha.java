package br.edu.av3.model.AP;

/**
 * Modulo 4 - um passo da execucao. Mostra o que o PDF pede:
 * leitura (entrada que ainda falta), estado atual e conteudo da pilha (topo a esquerda),
 * mais a acao tomada nesse passo.
 */
public record PassoPilha(String estado, String entradaRestante, String pilha, String acao) {
}
