package br.edu.av3.model.GLC;

/**
 * Modulo 3 - uma producao da gramatica: variavel -> producao (lado direito).
 * Ex.: Producao("S", "aSb") ou Producao("S", "λ") para a producao vazia.
 * Os simbolos sao de 1 caractere (ex.: S, a, b), no escopo do projeto.
 */
public record Producao(String variavel, String producao) {
}
