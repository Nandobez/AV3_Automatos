package br.edu.av3.model.MT;

/**
 * Modulo 5 - um passo da execucao: estado atual, simbolo lido, conteudo da fita
 * e posicao do cabecote. Espelha o que o projeto do professor imprimia no console.
 */
public record PassoTuring(int passo, String estado, String simboloLido, String fita, int cabecote) {
}
