package br.edu.av3.model.AP;

import java.util.List;

/**
 * Modulo 4 - resultado da execucao do Automato com Pilha.
 *
 * aceita       - true se parou em estado final com a entrada toda consumida
 * motivoParada - por que parou (aceitou / sem transicao / limite de passos)
 * passos       - passo a passo (leitura, estado, pilha)
 */
public record ResultadoPilha(boolean aceita, String motivoParada, List<PassoPilha> passos) {
}
