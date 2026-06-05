package br.edu.av3.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * Tratamento global de erros. Converte ValidacaoException em HTTP 400 (Bad
 * Request)
 * com um corpo simples {valida:false, erro:...}, em vez de estourar 500.
 * Vale para todos os modulos.
 *
 * //TODO : Falta remover os try catchs dos controllers, ja que fiz o handler,
 * provavelmente vou esquecerKKKKK
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidacaoException.class)
    public ResponseEntity<Map<String, Object>> tratarValidacao(ValidacaoException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("valida", false, "erro", e.getMessage()));
    }
}
