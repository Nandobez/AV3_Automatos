package br.edu.av3.model.GLC;

import java.util.List;

/**
 * Modulo 3 - resultado da tentativa de derivar a cadeia.
 *
 * aceita - true se a cadeia pertence a linguagem da gramatica
 * passos - derivacao passo a passo (formas sentenciais, ex.: S, aSb, aaSbb, ...)
 * arvore - arvore de derivacao simplificada (null se rejeitada)
 */
public record ResultadoDerivacao(boolean aceita, List<String> passos, NoDerivacao arvore) {
}
