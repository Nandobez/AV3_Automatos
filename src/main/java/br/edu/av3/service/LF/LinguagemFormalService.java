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
 * Como Sigma* e Sigma+ sao infinitos, a geracao e limitada por tamanho e por
 * quantidade maxima de cadeias retornadas.
 */
@Service
public class LinguagemFormalService {

    private static final int MAX_TAMANHO_GERACAO = 8;
    private static final int MAX_CADEIAS_GERADAS = 10_000;

    /**
     * Valida se todos os simbolos da cadeia pertencem ao alfabeto.
     * Lanca ValidacaoException no primeiro simbolo invalido, no estilo do exemplo.
     */
    public boolean validarCadeia(Alfabeto alfabeto, String cadeia) {
        validarAlfabeto(alfabeto);
        String entrada = cadeia == null ? "" : cadeia;

        for (int i = 0; i < entrada.length(); i++) {
            char simbolo = entrada.charAt(i);
            if (!alfabeto.contem(simbolo)) {
                throw new ValidacaoException(
                        "Simbolo '" + simbolo + "' na posicao " + i + " nao pertence ao alfabeto " + alfabeto);
            }
        }
        return true;
    }

    /** Tamanho da cadeia (numero de simbolos). */
    public int tamanho(String cadeia) {
        return cadeia == null ? 0 : cadeia.length();
    }

    /** Concatenacao de duas cadeias: w1 . w2. */
    public String concatenar(String w1, String w2) {
        return nuloParaVazio(w1) + nuloParaVazio(w2);
    }

    /**
     * Sigma* - todas as cadeias de tamanho 0 ate maxTamanho (inclui a cadeia vazia).
     */
    public List<String> sigmaEstrela(Alfabeto alfabeto, int maxTamanho) {
        validarGeracao(alfabeto, maxTamanho, true);
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
        validarGeracao(alfabeto, maxTamanho, false);
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

    private void validarGeracao(Alfabeto alfabeto, int maxTamanho, boolean incluiVazia) {
        validarAlfabeto(alfabeto);
        if (maxTamanho < 0) {
            throw new ValidacaoException("Tamanho maximo nao pode ser negativo");
        }
        if (maxTamanho > MAX_TAMANHO_GERACAO) {
            throw new ValidacaoException("Tamanho maximo muito alto (limite: " + MAX_TAMANHO_GERACAO + ")");
        }

        long total = incluiVazia ? 1 : 0;
        long potencia = 1;
        int inicio = incluiVazia ? 1 : 1;
        for (int tam = 1; tam <= maxTamanho; tam++) {
            potencia *= alfabeto.getSimbolos().size();
            if (tam >= inicio) {
                total += potencia;
            }
            if (total > MAX_CADEIAS_GERADAS) {
                throw new ValidacaoException("Geracao produziria cadeias demais (limite: " + MAX_CADEIAS_GERADAS + ")");
            }
        }
    }

    private void validarAlfabeto(Alfabeto alfabeto) {
        if (alfabeto == null || alfabeto.getSimbolos().isEmpty()) {
            throw new ValidacaoException("Alfabeto deve ser finito e nao vazio");
        }
    }

    private String nuloParaVazio(String valor) {
        return valor == null ? "" : valor;
    }
}
