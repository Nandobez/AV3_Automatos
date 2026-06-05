package br.edu.av3.model.AF;

import java.util.List;
import java.util.Set;

/**
 * Modulo 2 - definicao de um Automato Finito (serve para AFD e AFN).
 *
 * estados        - conjunto Q de estados
 * estadoInicial  - estado q0
 * estadosFinais  - conjunto F de estados de aceitacao
 * transicoes     - funcao de transicao (lista de origem--simbolo-->destino)
 *
 * E desserializado direto do JSON enviado pelo cliente (Jackson suporta records).
 */
public record AutomatoFinito(
        Set<String> estados,
        String estadoInicial,
        Set<String> estadosFinais,
        List<Transicao> transicoes) {
}
