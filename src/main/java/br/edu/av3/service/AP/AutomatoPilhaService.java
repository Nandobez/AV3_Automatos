package br.edu.av3.service.AP;

import br.edu.av3.exception.ValidacaoException;
import br.edu.av3.model.AP.AutomatoPilha;
import br.edu.av3.model.AP.PassoPilha;
import br.edu.av3.model.AP.ResultadoPilha;
import br.edu.av3.model.AP.TransicaoPilha;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Set;

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
public class AutomatoPilhaService {

    private static final Set<String> EPSILON = Set.of("", "λ", "ε", "&");
    private static final int LIMITE_PASSOS = 100_000;

    public ResultadoPilha executar(AutomatoPilha ap, String cadeia) {
        validar(ap);
        String entrada = cadeia == null ? "" : cadeia;

        Deque<Character> pilha = new ArrayDeque<>();
        pilha.push(ap.simboloInicialPilha().charAt(0));

        String estado = ap.estadoInicial();
        int pos = 0;
        List<PassoPilha> passos = new ArrayList<>();

        for (int n = 0; ; n++) {
            // Aceitacao: entrada consumida e estado final.
            if (pos == entrada.length() && ap.estadosFinais().contains(estado)) {
                passos.add(passo(estado, entrada, pos, pilha, "Estado final com entrada consumida -> ACEITA"));
                return new ResultadoPilha(true, "Aceita por estado final", passos);
            }

            TransicaoPilha t = encontrarTransicao(ap, estado, entrada, pos, pilha);
            if (t == null) {
                passos.add(passo(estado, entrada, pos, pilha, "Sem transicao valida -> PARA"));
                return new ResultadoPilha(false, "Parou sem transicao valida", passos);
            }

            passos.add(passo(estado, entrada, pos, pilha, descrever(t)));
            aplicar(t, pilha);
            if (!ehEpsilon(t.simboloLido())) {
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
    private TransicaoPilha encontrarTransicao(AutomatoPilha ap, String estado, String entrada, int pos, Deque<Character> pilha) {
        for (TransicaoPilha t : ap.transicoes()) {
            if (!t.estadoAtual().equals(estado)) {
                continue;
            }
            // topo da pilha
            if (!ehEpsilon(t.desempilha())) {
                if (pilha.isEmpty() || pilha.peek() != t.desempilha().charAt(0)) {
                    continue;
                }
            }
            // simbolo de entrada
            if (!ehEpsilon(t.simboloLido())) {
                if (pos >= entrada.length() || entrada.charAt(pos) != t.simboloLido().charAt(0)) {
                    continue;
                }
            }
            return t;
        }
        return null;
    }

    private void aplicar(TransicaoPilha t, Deque<Character> pilha) {
        if (!ehEpsilon(t.desempilha())) {
            pilha.pop();
        }
        if (!ehEpsilon(t.empilha())) {
            String empilha = t.empilha();
            // empilha da direita para a esquerda -> o caractere mais a esquerda fica no topo
            for (int i = empilha.length() - 1; i >= 0; i--) {
                pilha.push(empilha.charAt(i));
            }
        }
    }

    private PassoPilha passo(String estado, String entrada, int pos, Deque<Character> pilha, String acao) {
        String restante = pos >= entrada.length() ? "λ" : entrada.substring(pos);
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
        String le = ehEpsilon(t.simboloLido()) ? "ε" : t.simboloLido();
        String pop = ehEpsilon(t.desempilha()) ? "ε" : t.desempilha();
        String push = ehEpsilon(t.empilha()) ? "ε" : t.empilha();
        return "le '" + le + "', desempilha " + pop + ", empilha " + push + " -> " + t.novoEstado();
    }

    private boolean ehEpsilon(String s) {
        return s == null || EPSILON.contains(s);
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
        for (String f : ap.estadosFinais() == null ? Set.<String>of() : ap.estadosFinais()) {
            if (!ap.estados().contains(f)) {
                throw new ValidacaoException("Estado final '" + f + "' nao pertence ao conjunto de estados");
            }
        }
        if (ap.transicoes() == null) {
            throw new ValidacaoException("A maquina precisa de uma lista de transicoes");
        }
        for (TransicaoPilha t : ap.transicoes()) {
            if (!ap.estados().contains(t.estadoAtual()) || !ap.estados().contains(t.novoEstado())) {
                throw new ValidacaoException("Transicao usa estado inexistente: " + t.estadoAtual() + " -> " + t.novoEstado());
            }
        }
    }
}
