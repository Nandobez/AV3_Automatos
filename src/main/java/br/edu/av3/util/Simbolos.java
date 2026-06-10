package br.edu.av3.util;

import java.util.Set;

/** Utilitario central para simbolos especiais usados nos modelos formais. */
public final class Simbolos {

    public static final String LAMBDA = "\u03BB";
    public static final String EPSILON = "\u03B5";

    private static final Set<String> EPSILON_EQUIVALENTES = Set.of("", LAMBDA, EPSILON, "&");

    private Simbolos() {
    }

    public static boolean ehEpsilon(String simbolo) {
        return simbolo == null || EPSILON_EQUIVALENTES.contains(simbolo);
    }
}
