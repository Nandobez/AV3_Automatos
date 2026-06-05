package br.edu.av3.dto.LF;

/** Corpo da requisicao de validacao: alfabeto e cadeia a validar. Ex.: {"alfabeto":"ab","cadeia":"abba"}. */
public record ValidarCadeiaRequest(String alfabeto, String cadeia) {
}
