package br.edu.av3.model.LF;

import br.edu.av3.exception.ValidacaoException;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Modulo 1 - representa um alfabeto Sigma: conjunto finito e nao vazio de simbolos.
 * Ex.: Sigma = {a, b}.
 */
public class Alfabeto {

    private final Set<Character> simbolos = new LinkedHashSet<>();

    /** Cria o alfabeto a partir dos simbolos informados (ex.: 'a', 'b'). */
    public Alfabeto(char... simbolos) {
        for (char c : simbolos) {
            this.simbolos.add(c);
        }
    }

    /** Cria o alfabeto a partir de uma String, um simbolo por caractere (ex.: "ab"). */
    public Alfabeto(String simbolos) {
        if (simbolos == null || simbolos.isEmpty()) {
            throw new ValidacaoException("Alfabeto deve ser finito e nao vazio");
        }
        for (char c : simbolos.toCharArray()) {
            this.simbolos.add(c);
        }
    }

    /** Verifica se um simbolo pertence ao alfabeto. */
    public boolean contem(char simbolo) {
        return simbolos.contains(simbolo);
    }

    public Set<Character> getSimbolos() {
        return simbolos;
    }

    @Override
    public String toString() {
        return simbolos.toString();
    }
}
