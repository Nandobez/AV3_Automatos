package br.edu.av3.dto.AF;

import br.edu.av3.model.AF.AutomatoFinito;

/** Corpo da requisicao: o automato (carregado por JSON) e a cadeia a executar. */
public record ExecutarAutomatoRequest(AutomatoFinito automato, String cadeia) {
}
