package br.edu.av3.model.MT;

/**
 * Modulo 5 - transicao da Maquina de Turing (mesmo modelo do projeto do professor).
 * No estado 'estadoAtual', lendo 'simboloLido': escreve 'simboloEscrito',
 * move o cabecote ('L' esquerda, 'R' direita) e vai para 'novoEstado'.
 */
public record TransicaoTuring(
        String estadoAtual,
        char simboloLido,
        String novoEstado,
        char simboloEscrito,
        char movimento) {
}
