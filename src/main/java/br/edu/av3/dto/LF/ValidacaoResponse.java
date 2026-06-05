package br.edu.av3.dto.LF;

/** Resposta da validacao: se a cadeia e valida, seu tamanho e uma mensagem descritiva. */
public record ValidacaoResponse(boolean valida, int tamanho, String mensagem) {
}
