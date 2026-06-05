package br.edu.av3.model.AP;

import java.util.List;
import java.util.Set;

/**
 * Modulo 4 - definicao de um Automato com Pilha (PDA), deterministico.
 *
 * estados              - conjunto de estados
 * estadoInicial        - estado de partida
 * estadosFinais        - aceita por estado final (com a entrada toda consumida)
 * simboloInicialPilha  - simbolo que ja comeca na pilha (ex.: "Z")
 * transicoes           - funcao de transicao
 *
 * Linguagem minima exigida: L = { a^n b^n | n >= 1 }.
 */
public record AutomatoPilha(
        Set<String> estados,
        String estadoInicial,
        Set<String> estadosFinais,
        String simboloInicialPilha,
        List<TransicaoPilha> transicoes) {
}
