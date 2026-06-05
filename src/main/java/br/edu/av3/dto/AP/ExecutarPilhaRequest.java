package br.edu.av3.dto.AP;

import br.edu.av3.model.AP.AutomatoPilha;

/** Corpo da requisicao: o automato com pilha e a cadeia a executar. */
public record ExecutarPilhaRequest(AutomatoPilha automato, String cadeia) {
}
