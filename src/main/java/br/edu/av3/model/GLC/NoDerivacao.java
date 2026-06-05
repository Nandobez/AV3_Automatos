package br.edu.av3.model.GLC;

import java.util.List;

/**
 * Modulo 3 - no da arvore de derivacao.
 * - variavel expandida: simbolo = a variavel, filhos = simbolos do lado direito aplicado;
 * - terminal: filhos vazio;
 * - producao vazia: filho unico com simbolo "λ".
 */
public record NoDerivacao(String simbolo, List<NoDerivacao> filhos) {
}
