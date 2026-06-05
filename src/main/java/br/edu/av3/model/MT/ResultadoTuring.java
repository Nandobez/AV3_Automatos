package br.edu.av3.model.MT;

import java.util.List;

/**
 * Modulo 5 - resultado da execucao da Maquina de Turing.
 *
 * aceita       - true se parou em um estado final
 * estadoFinal  - estado em que a maquina parou
 * fitaFinal    - conteudo da fita ao parar (ex.: resultado da soma)
 * motivoParada - por que parou (estado final / sem transicao / limite de passos)
 * passos       - passo a passo da execucao
 */
public record ResultadoTuring(
        boolean aceita,
        String estadoFinal,
        String fitaFinal,
        String motivoParada,
        List<PassoTuring> passos) {
}
