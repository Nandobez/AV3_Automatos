package br.edu.av3.model.AF;

/**
 * Modulo 2 - uma transicao do automato: origem --simbolo--> destino.
 * O simbolo e String para permitir a transicao vazia (epsilon: "", "λ", "ε", "&").
 * Para AFN basta ter varias transicoes com mesma origem e simbolo.
 */
public record Transicao(String origem, String simbolo, String destino) {
}
