package br.edu.av3.model.AP;

/**
 * Modulo 4 - transicao de um Automato com Pilha.
 * No 'estadoAtual', lendo 'simboloLido' (ou ε = nao consome entrada) e com
 * 'desempilha' no topo da pilha: troca o topo por 'empilha' (string; o caractere
 * mais a esquerda vira o novo topo) e vai para 'novoEstado'.
 *
 * Use "", "λ", "ε" ou "&" para epsilon em simboloLido/desempilha/empilha.
 * Simbolos de 1 caractere, no escopo do projeto.
 */
public record TransicaoPilha(
        String estadoAtual,
        String simboloLido,
        String desempilha,
        String novoEstado,
        String empilha) {
}
