package br.edu.av3.dto.GLC;

import br.edu.av3.model.GLC.Gramatica;

/** Corpo da requisicao: a gramatica e a cadeia a derivar. */
public record DerivarRequest(Gramatica gramatica, String cadeia) {
}
