package br.edu.av3.exception;

/**
 * Lancada quando uma entrada nao respeita as regras de uma linguagem formal
 * (cadeia fora do alfabeto, simbolo invalido, etc.).
 * Mesmo padrao do ParseException do projeto de exemplo.
 */
public class ValidacaoException extends RuntimeException {

    public ValidacaoException(String mensagem) {
        super(mensagem);
    }
}
