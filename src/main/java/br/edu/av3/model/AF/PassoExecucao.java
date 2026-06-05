package br.edu.av3.model.AF;

import java.util.Set;

/**
 * Modulo 2 - um passo da execucao: ao ler 'simbolo', o conjunto de estados
 * passa de 'estadosAntes' para 'estadosDepois' (ja com fecho-epsilon aplicado).
 */
public record PassoExecucao(String simbolo, Set<String> estadosAntes, Set<String> estadosDepois) {
}
