package br.edu.av3.service.AF;

import br.edu.av3.exception.ValidacaoException;
import br.edu.av3.model.AF.AutomatoFinito;
import br.edu.av3.model.AF.PassoExecucao;
import br.edu.av3.model.AF.ResultadoExecucao;
import br.edu.av3.model.AF.Transicao;
import br.edu.av3.service.ModeloComputacionalService;
import br.edu.av3.service.AF.AutomatoFinitoFactory.AutomatoFinitoCompilado;
import br.edu.av3.util.Simbolos;
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
public class AutomatoFinitoService implements ModeloComputacionalService<AutomatoFinito, String, ResultadoExecucao> {

    @Override
    public ResultadoExecucao executar(AutomatoFinito af, String cadeia) {
        validar(af);
        String entrada = cadeia == null ? "" : cadeia;
        AutomatoFinitoCompilado automato = AutomatoFinitoFactory.compilar(af);

        List<PassoExecucao> passos = new ArrayList<>();
        Set<String> atuais = fechoEpsilon(automato, new TreeSet<>(Set.of(af.estadoInicial())));

        for (int i = 0; i < entrada.length(); i++) {
            String simbolo = String.valueOf(entrada.charAt(i));
            Set<String> antes = atuais;
            Set<String> depois = fechoEpsilon(automato, mover(automato, atuais, simbolo));
            passos.add(new PassoExecucao(simbolo, antes, depois));
            atuais = depois;
        }

        boolean aceita = atuais.stream().anyMatch(e -> automato.estadosFinais().contains(e));
        return new ResultadoExecucao(aceita, atuais, passos);
    }

    /** Estados alcancaveis a partir de 'atuais' lendo um simbolo (sem epsilon). */
    private Set<String> mover(AutomatoFinitoCompilado automato, Set<String> atuais, String simbolo) {
        Set<String> destino = new TreeSet<>();
        for (String estado : atuais) {
            destino.addAll(automato.destinos(estado, simbolo));
        }
        return destino;
    }

    /** Fecho-epsilon: tudo que se alcanca a partir do conjunto seguindo so transicoes vazias. */
    private Set<String> fechoEpsilon(AutomatoFinitoCompilado automato, Set<String> estados) {
        Set<String> fecho = new TreeSet<>(estados);
        Deque<String> pilha = new ArrayDeque<>(estados);
        while (!pilha.isEmpty()) {
            String e = pilha.pop();
            for (String destino : automato.destinosEpsilon(e)) {
                if (fecho.add(destino)) {
                    pilha.push(destino);
                }
            }
        }
        return fecho;
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
        if (af.transicoes() == null) {
            throw new ValidacaoException("O automato precisa de uma lista de transicoes");
        }
        for (String f : nuloParaVazio(af.estadosFinais())) {
            if (!af.estados().contains(f)) {
                throw new ValidacaoException("Estado final '" + f + "' nao pertence ao conjunto de estados");
            }
        }
        for (Transicao t : af.transicoes()) {
            if (t == null) {
                throw new ValidacaoException("Transicao invalida: valor nulo");
            }
            if (!af.estados().contains(t.origem())) {
                throw new ValidacaoException("Transicao com origem invalida: '" + t.origem() + "'");
            }
            if (!af.estados().contains(t.destino())) {
                throw new ValidacaoException("Transicao com destino invalido: '" + t.destino() + "'");
            }
            if (!Simbolos.ehEpsilon(t.simbolo()) && t.simbolo().length() != 1) {
                throw new ValidacaoException("Simbolo de transicao invalido (use 1 caractere ou epsilon)");
            }
        }
    }

    private <T> java.util.Collection<T> nuloParaVazio(java.util.Collection<T> c) {
        return c == null ? List.of() : c;
    }
}
