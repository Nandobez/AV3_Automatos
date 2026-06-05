package br.edu.av3.model.MT;

import java.util.List;
import java.util.Set;

/**
 * Modulo 5 - definicao de uma Maquina de Turing (1 fita, deterministica).
 * Generica: as transicoes sao informadas pelo cliente, entao serve tanto para
 * reconhecer L = { w#w } (opcao A) quanto para somar em unario (opcao B).
 *
 * estadoInicial - estado de partida (q0)
 * estadosFinais - estados de aceitacao (ao alcancar um deles, a maquina aceita e para)
 * transicoes    - funcao de transicao
 * branco        - simbolo branco da fita (default 'B' se omitido)
 */
public record MaquinaTuring(
        String estadoInicial,
        Set<String> estadosFinais,
        List<TransicaoTuring> transicoes,
        char branco) {
}
