package br.edu.av3.service.AF;

import br.edu.av3.exception.ValidacaoException;
import br.edu.av3.model.AF.AutomatoFinito;
import br.edu.av3.model.AF.PassoExecucao;
import br.edu.av3.model.AF.ResultadoExecucao;
import br.edu.av3.model.AF.Transicao;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Logica do Modulo 2 - Automato Finito (AFD/AFN).
 *
 * A simulacao trabalha sempre com um CONJUNTO de estados atuais. Isso cobre:
 *  - AFD: o conjunto sempre tem 1 estado;
 *  - AFN: o conjunto pode ter varios (nao determinismo) e usa fecho-epsilon.
 */
@Service
public class AutomatoFinitoService {

    /** Simbolos tratados como transicao vazia (epsilon). */
    private static final Set<String> EPSILON = Set.of("", "λ", "ε", "&");

    public ResultadoExecucao executar(AutomatoFinito af, String cadeia) {
        validar(af);

        List<PassoExecucao> passos = new ArrayList<>();
        Set<String> atuais = fechoEpsilon(af, new TreeSet<>(Set.of(af.estadoInicial())));

        for (int i = 0; i < cadeia.length(); i++) {
            String simbolo = String.valueOf(cadeia.charAt(i));
            Set<String> antes = atuais;
            Set<String> depois = fechoEpsilon(af, mover(af, atuais, simbolo));
            passos.add(new PassoExecucao(simbolo, antes, depois));
            atuais = depois;
        }

        boolean aceita = atuais.stream().anyMatch(e -> af.estadosFinais().contains(e));
        return new ResultadoExecucao(aceita, atuais, passos);
    }

    /** Estados alcancaveis a partir de 'atuais' lendo um simbolo (sem epsilon). */
    private Set<String> mover(AutomatoFinito af, Set<String> atuais, String simbolo) {
        Set<String> destino = new TreeSet<>();
        for (Transicao t : af.transicoes()) {
            if (!ehEpsilon(t.simbolo()) && atuais.contains(t.origem()) && t.simbolo().equals(simbolo)) {
                destino.add(t.destino());
            }
        }
        return destino;
    }

    /** Fecho-epsilon: tudo que se alcanca a partir do conjunto seguindo so transicoes vazias. */
    private Set<String> fechoEpsilon(AutomatoFinito af, Set<String> estados) {
        Set<String> fecho = new TreeSet<>(estados);
        Deque<String> pilha = new ArrayDeque<>(estados);
        while (!pilha.isEmpty()) {
            String e = pilha.pop();
            for (Transicao t : af.transicoes()) {
                if (ehEpsilon(t.simbolo()) && t.origem().equals(e) && fecho.add(t.destino())) {
                    pilha.push(t.destino());
                }
            }
        }
        return fecho;
    }

    private boolean ehEpsilon(String simbolo) {
        return simbolo == null || EPSILON.contains(simbolo);
    }

    /** Validacao da definicao do automato, no estilo do projeto de exemplo. */
    private void validar(AutomatoFinito af) {
        if (af == null) {
            throw new ValidacaoException("Automato nao informado");
        }
        if (af.estados() == null || af.estados().isEmpty()) {
            throw new ValidacaoException("O automato precisa de pelo menos um estado");
        }
        if (af.estadoInicial() == null || !af.estados().contains(af.estadoInicial())) {
            throw new ValidacaoException("Estado inicial '" + af.estadoInicial() + "' nao pertence ao conjunto de estados");
        }
        for (String f : nuloParaVazio(af.estadosFinais())) {
            if (!af.estados().contains(f)) {
                throw new ValidacaoException("Estado final '" + f + "' nao pertence ao conjunto de estados");
            }
        }
        for (Transicao t : nuloParaVazio(af.transicoes())) {
            if (!af.estados().contains(t.origem())) {
                throw new ValidacaoException("Transicao com origem invalida: '" + t.origem() + "'");
            }
            if (!ehEpsilon(t.simbolo()) && !af.estados().contains(t.destino())) {
                throw new ValidacaoException("Transicao com destino invalido: '" + t.destino() + "'");
            }
        }
    }

    private <T> java.util.Collection<T> nuloParaVazio(java.util.Collection<T> c) {
        return c == null ? List.of() : c;
    }
}
