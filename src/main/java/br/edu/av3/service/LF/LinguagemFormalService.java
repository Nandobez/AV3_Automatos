package br.edu.av3.service.LF;

import br.edu.av3.exception.ValidacaoException;
import br.edu.av3.model.LF.Alfabeto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Logica do Modulo 1 - Linguagens Formais.
 * Validacao de cadeia, tamanho, concatenacao, Sigma* e Sigma+.
 *
 * Como Sigma* e Sigma+ sao infinitos, a geracao e limitada por um tamanho maximo
 * de cadeia (parametro maxTamanho).
 */
@Service
public class LinguagemFormalService {

    /**
     * Valida se todos os simbolos da cadeia pertencem ao alfabeto.
     * Lanca ValidacaoException no primeiro simbolo invalido, no estilo do exemplo.
     */
    public boolean validarCadeia(Alfabeto alfabeto, String cadeia) {
        for (int i = 0; i < cadeia.length(); i++) {
            char simbolo = cadeia.charAt(i);
            if (!alfabeto.contem(simbolo)) {
                throw new ValidacaoException(
                        "Simbolo '" + simbolo + "' na posicao " + i + " nao pertence ao alfabeto " + alfabeto);
            }
        }
        return true;
    }

    /** Tamanho da cadeia (numero de simbolos). */
    public int tamanho(String cadeia) {
        return cadeia.length();
    }

    /** Concatenacao de duas cadeias: w1 . w2. */
    public String concatenar(String w1, String w2) {
        return w1 + w2;
    }

    /**
     * Sigma* - todas as cadeias de tamanho 0 ate maxTamanho (inclui a cadeia vazia).
     */
    public List<String> sigmaEstrela(Alfabeto alfabeto, int maxTamanho) {
        List<String> cadeias = new ArrayList<>();
        for (int tam = 0; tam <= maxTamanho; tam++) {
            gerarCadeias(alfabeto, tam, "", cadeias);
        }
        return cadeias;
    }

    /**
     * Sigma+ - igual a Sigma* mas sem a cadeia vazia (tamanho 1 ate maxTamanho).
     */
    public List<String> sigmaMais(Alfabeto alfabeto, int maxTamanho) {
        List<String> cadeias = new ArrayList<>();
        for (int tam = 1; tam <= maxTamanho; tam++) {
            gerarCadeias(alfabeto, tam, "", cadeias);
        }
        return cadeias;
    }

    /** Gera recursivamente todas as cadeias de um tamanho fixo sobre o alfabeto. */
    private void gerarCadeias(Alfabeto alfabeto, int tamanho, String prefixo, List<String> destino) {
        if (prefixo.length() == tamanho) {
            destino.add(prefixo);
            return;
        }
        for (char simbolo : alfabeto.getSimbolos()) {
            gerarCadeias(alfabeto, tamanho, prefixo + simbolo, destino);
        }
    }
}
