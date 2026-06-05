package br.edu.av3.model.GLC;

import java.util.List;
import java.util.Set;

/**
 * Modulo 3 - definicao de uma Gramatica Livre de Contexto (GLC).
 *
 * variaveis      - simbolos nao terminais (V)
 * terminais      - simbolos terminais (T)
 * producoes      - regras variavel -> cadeia de simbolos
 * simboloInicial - variavel de partida (S)
 *
 * Desserializada direto do JSON enviado pelo cliente.
 */
public record Gramatica(
        Set<String> variaveis,
        Set<String> terminais,
        List<Producao> producoes,
        String simboloInicial) {
}
