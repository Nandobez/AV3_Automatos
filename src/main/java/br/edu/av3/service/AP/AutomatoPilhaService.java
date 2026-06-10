package br.edu.av3.service.AP;

import br.edu.av3.exception.ValidacaoException;
import br.edu.av3.model.AP.AutomatoPilha;
import br.edu.av3.model.AP.PassoPilha;
import br.edu.av3.model.AP.ResultadoPilha;
import br.edu.av3.model.AP.TransicaoPilha;
import br.edu.av3.service.ModeloComputacionalService;
import br.edu.av3.service.AP.AutomatoPilhaFactory.AutomatoPilhaCompilado;
import br.edu.av3.util.Simbolos;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Logica do Modulo 4 - Automato com Pilha (PDA), deterministico.
 *
 * A cada passo casa (estado, simbolo lido, topo da pilha) com uma transicao:
 * desempilha o topo, empilha a string da transicao (caractere mais a esquerda = novo topo)
 * e troca de estado. Aceita por estado final com a entrada toda consumida.
 *
 * Cobre a linguagem minima L = { a^n b^n | n >= 1 }.
 */
@Service
public class AutomatoPilhaService implements ModeloComputacionalService<AutomatoPilha, String, ResultadoPilha> {

    private static final int LIMITE_PASSOS = 100_000;

    @Override
    public ResultadoPilha executar(AutomatoPilha ap, String cadeia) {
        validar(ap);
        String entrada = cadeia == null ? "" : cadeia;
        AutomatoPilhaCompilado automato = AutomatoPilhaFactory.compilar(ap);

        Deque<Character> pilha = new ArrayDeque<>();
        pilha.push(automato.simboloInicialPilha());

        String estado = automato.estadoInicial();
        int pos = 0;
        List<PassoPilha> passos = new ArrayList<>();

        for (int n = 0; ; n++) {
            if (pos == entrada.length() && automato.estadosFinais().contains(estado)) {
                passos.add(passo(estado, entrada, pos, pilha, "Estado final com entrada consumida -> ACEITA"));
                return new ResultadoPilha(true, "Aceita por estado final", passos);
            }

            TransicaoPilha t = encontrarTransicao(automato, estado, entrada, pos, pilha);
            if (t == null) {
                passos.add(passo(estado, entrada, pos, pilha, "Sem transicao valida -> PARA"));
                return new ResultadoPilha(false, "Parou sem transicao valida", passos);
            }

            passos.add(passo(estado, entrada, pos, pilha, descrever(t)));
            aplicar(t, pilha);
            if (!Simbolos.ehEpsilon(t.simboloLido())) {
                pos++;
            }
            estado = t.novoEstado();

            if (n > LIMITE_PASSOS) {
                passos.add(passo(estado, entrada, pos, pilha, "Limite de passos atingido (possivel loop)"));
                return new ResultadoPilha(false, "Limite de passos atingido", passos);
            }
        }
    }

    /** Primeira transicao que casa estado + topo da pilha + simbolo lido (ou epsilon). */
    private TransicaoPilha encontrarTransicao(AutomatoPilhaCompilado automato, String estado, String entrada, int pos, Deque<Character> pilha) {
        for (TransicaoPilha t : automato.transicoesDoEstado(estado)) {
            if (!Simbolos.ehEpsilon(t.desempilha())) {
                if (pilha.isEmpty() || pilha.peek() != t.desempilha().charAt(0)) {
                    continue;
                }
            }
            if (!Simbolos.ehEpsilon(t.simboloLido())) {
                if (pos >= entrada.length() || entrada.charAt(pos) != t.simboloLido().charAt(0)) {
                    continue;
                }
            }
            return t;
        }
        return null;
    }

    private void aplicar(TransicaoPilha t, Deque<Character> pilha) {
        if (!Simbolos.ehEpsilon(t.desempilha())) {
            pilha.pop();
        }
        if (!Simbolos.ehEpsilon(t.empilha())) {
            String empilha = t.empilha();
            for (int i = empilha.length() - 1; i >= 0; i--) {
                pilha.push(empilha.charAt(i));
            }
        }
    }

    private PassoPilha passo(String estado, String entrada, int pos, Deque<Character> pilha, String acao) {
        String restante = pos >= entrada.length() ? Simbolos.LAMBDA : entrada.substring(pos);
        return new PassoPilha(estado, restante, conteudoPilha(pilha), acao);
    }

    /** Conteudo da pilha do topo para a base. */
    private String conteudoPilha(Deque<Character> pilha) {
        if (pilha.isEmpty()) {
            return "(vazia)";
        }
        StringBuilder sb = new StringBuilder();
        for (char c : pilha) {
            sb.append(c);
        }
        return sb.toString();
    }

    private String descrever(TransicaoPilha t) {
        String le = Simbolos.ehEpsilon(t.simboloLido()) ? Simbolos.EPSILON : t.simboloLido();
        String pop = Simbolos.ehEpsilon(t.desempilha()) ? Simbolos.EPSILON : t.desempilha();
        String push = Simbolos.ehEpsilon(t.empilha()) ? Simbolos.EPSILON : t.empilha();
        return "le '" + le + "', desempilha " + pop + ", empilha " + push + " -> " + t.novoEstado();
    }

    private void validar(AutomatoPilha ap) {
        if (ap == null) {
            throw new ValidacaoException("Automato com pilha nao informado");
        }
        if (ap.estados() == null || ap.estados().isEmpty()) {
            throw new ValidacaoException("O automato precisa de pelo menos um estado");
        }
        if (ap.estadoInicial() == null || !ap.estados().contains(ap.estadoInicial())) {
            throw new ValidacaoException("Estado inicial '" + ap.estadoInicial() + "' nao pertence ao conjunto de estados");
        }
        if (ap.simboloInicialPilha() == null || ap.simboloInicialPilha().length() != 1) {
            throw new ValidacaoException("Simbolo inicial da pilha invalido (use 1 caractere)");
        }
        for (String f : nuloParaVazio(ap.estadosFinais())) {
            if (!ap.estados().contains(f)) {
                throw new ValidacaoException("Estado final '" + f + "' nao pertence ao conjunto de estados");
            }
        }
        if (ap.transicoes() == null) {
            throw new ValidacaoException("A maquina precisa de uma lista de transicoes");
        }
        for (TransicaoPilha t : ap.transicoes()) {
            if (t == null) {
                throw new ValidacaoException("Transicao invalida: valor nulo");
            }
            if (!ap.estados().contains(t.estadoAtual()) || !ap.estados().contains(t.novoEstado())) {
                throw new ValidacaoException("Transicao usa estado inexistente: " + t.estadoAtual() + " -> " + t.novoEstado());
            }
            validarSimboloOpcional(t.simboloLido(), "Simbolo lido");
            validarSimboloOpcional(t.desempilha(), "Simbolo desempilhado");
        }
    }

    private void validarSimboloOpcional(String simbolo, String nome) {
        if (!Simbolos.ehEpsilon(simbolo) && simbolo.length() != 1) {
            throw new ValidacaoException(nome + " invalido (use 1 caractere ou epsilon)");
        }
    }

    private <T> java.util.Collection<T> nuloParaVazio(java.util.Collection<T> c) {
        return c == null ? List.of() : c;
    }
}
