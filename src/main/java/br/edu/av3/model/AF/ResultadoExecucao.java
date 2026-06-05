package br.edu.av3.model.AF;

import java.util.List;
import java.util.Set;

/**
 * Modulo 2 - resultado da execucao de uma cadeia no automato.
 *
 * aceita         - true se algum estado final foi alcancado ao fim da cadeia
 * estadosFinais  - conjunto de estados em que o automato parou
 * passos         - passo a passo das transicoes (para exibir na interface)
 */
public record ResultadoExecucao(boolean aceita, Set<String> estadosFinais, List<PassoExecucao> passos) {
}
