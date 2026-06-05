package br.edu.av3.service.GLC;

import br.edu.av3.exception.ValidacaoException;
import br.edu.av3.model.GLC.Gramatica;
import br.edu.av3.model.GLC.NoDerivacao;
import br.edu.av3.model.GLC.Producao;
import br.edu.av3.model.GLC.ResultadoDerivacao;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Logica do Modulo 3 - Gramatica Livre de Contexto.
 *
 * Reconhecimento por parser recursivo com backtracking (DFS):
 * cada variavel precisa derivar um trecho contiguo da cadeia; para cada producao
 * testamos as quebras possiveis do trecho entre os simbolos do lado direito.
 * O proprio encadeamento das chamadas constroi a arvore de derivacao, e dela
 * geramos o passo a passo (derivacao mais a esquerda).
 *
 * Complexidade: O(b^d) no pior caso (busca em arvore de derivacoes). Suficiente
 * para as entradas do escopo. Alternativa polinomial: CYK O(n^3) - ver documento.
 */
@Service
public class GramaticaService {

    /** Representacoes aceitas para a producao vazia (epsilon). */
    private static final Set<String> EPSILON = Set.of("", "λ", "ε", "&");

    public ResultadoDerivacao derivar(Gramatica g, String cadeia) {
        validar(g);

        char inicial = g.simboloInicial().charAt(0);
        NoDerivacao arvore = parse(g, inicial, 0, cadeia.length(), cadeia, new HashSet<>());

        if (arvore == null) {
            return new ResultadoDerivacao(false, List.of(), null);
        }
        return new ResultadoDerivacao(true, gerarPassos(arvore), arvore);
    }

    /**
     * Tenta derivar exatamente o trecho [ini, fim) de 'alvo' a partir de 'simbolo'.
     * Retorna o no da arvore se conseguir, ou null (backtrack).
     */
    private NoDerivacao parse(Gramatica g, char simbolo, int ini, int fim, String alvo, Set<String> caminho) {
        // Terminal: casa exatamente 1 caractere.
        if (ehTerminal(g, simbolo)) {
            if (fim - ini == 1 && alvo.charAt(ini) == simbolo) {
                return folha(String.valueOf(simbolo));
            }
            return null;
        }

        // Variavel: tenta cada producao. Guarda contra ciclos (mesmo simbolo no mesmo trecho).
        String chave = simbolo + "@" + ini + ":" + fim;
        if (caminho.contains(chave)) {
            return null;
        }
        Set<String> novoCaminho = new HashSet<>(caminho);
        novoCaminho.add(chave);

        for (Producao p : g.producoes()) {
            if (p.variavel() == null || p.variavel().length() != 1 || p.variavel().charAt(0) != simbolo) {
                continue;
            }
            List<Character> rhs = simbolosDoLadoDireito(p);

            if (rhs.isEmpty()) {
                // Producao vazia: so casa se o trecho tambem for vazio.
                if (ini == fim) {
                    return new NoDerivacao(String.valueOf(simbolo), List.of(folha("λ")));
                }
                continue;
            }

            List<NoDerivacao> filhos = casarSequencia(g, rhs, 0, ini, fim, alvo, novoCaminho);
            if (filhos != null) {
                return new NoDerivacao(String.valueOf(simbolo), filhos);
            }
        }
        return null;
    }

    /**
     * Casa rhs[idx..] contra o trecho [pos, fim), testando todas as quebras.
     * Retorna a lista de filhos (em ordem) ou null.
     */
    private List<NoDerivacao> casarSequencia(Gramatica g, List<Character> rhs, int idx,
                                             int pos, int fim, String alvo, Set<String> caminho) {
        if (idx == rhs.size()) {
            return pos == fim ? new ArrayList<>() : null;
        }

        char simbolo = rhs.get(idx);

        // Terminal consome exatamente 1 caractere -> poda forte.
        if (ehTerminal(g, simbolo)) {
            if (pos < fim && alvo.charAt(pos) == simbolo) {
                List<NoDerivacao> resto = casarSequencia(g, rhs, idx + 1, pos + 1, fim, alvo, caminho);
                if (resto != null) {
                    resto.add(0, folha(String.valueOf(simbolo)));
                    return resto;
                }
            }
            return null;
        }

        // Variavel: tenta cada quebra [pos, corte).
        for (int corte = pos; corte <= fim; corte++) {
            NoDerivacao filho = parse(g, simbolo, pos, corte, alvo, caminho);
            if (filho == null) {
                continue;
            }
            List<NoDerivacao> resto = casarSequencia(g, rhs, idx + 1, corte, fim, alvo, caminho);
            if (resto != null) {
                resto.add(0, filho);
                return resto;
            }
        }
        return null;
    }

    /** Gera a derivacao mais a esquerda (lista de formas sentenciais) a partir da arvore. */
    private List<String> gerarPassos(NoDerivacao raiz) {
        List<String> passos = new ArrayList<>();
        List<NoDerivacao> fronteira = new ArrayList<>();
        fronteira.add(raiz);
        passos.add(formaSentencial(fronteira));

        while (true) {
            int idx = -1;
            for (int i = 0; i < fronteira.size(); i++) {
                if (!fronteira.get(i).filhos().isEmpty()) {
                    idx = i;
                    break;
                }
            }
            if (idx < 0) {
                break;
            }
            NoDerivacao no = fronteira.remove(idx);
            fronteira.addAll(idx, no.filhos());
            passos.add(formaSentencial(fronteira));
        }
        return passos;
    }

    /** Concatena os simbolos da fronteira, ignorando λ. Forma vazia vira "λ". */
    private String formaSentencial(List<NoDerivacao> fronteira) {
        StringBuilder sb = new StringBuilder();
        for (NoDerivacao no : fronteira) {
            if (!no.simbolo().equals("λ")) {
                sb.append(no.simbolo());
            }
        }
        return sb.length() == 0 ? "λ" : sb.toString();
    }

    private List<Character> simbolosDoLadoDireito(Producao p) {
        List<Character> simbolos = new ArrayList<>();
        if (p.producao() == null || EPSILON.contains(p.producao())) {
            return simbolos;
        }
        for (char c : p.producao().toCharArray()) {
            simbolos.add(c);
        }
        return simbolos;
    }

    private NoDerivacao folha(String simbolo) {
        return new NoDerivacao(simbolo, List.of());
    }

    private boolean ehTerminal(Gramatica g, char simbolo) {
        return g.terminais() != null && g.terminais().contains(String.valueOf(simbolo));
    }

    /** Validacao da definicao da gramatica, no estilo do projeto de exemplo. */
    private void validar(Gramatica g) {
        if (g == null) {
            throw new ValidacaoException("Gramatica nao informada");
        }
        if (g.variaveis() == null || g.variaveis().isEmpty()) {
            throw new ValidacaoException("A gramatica precisa de pelo menos uma variavel");
        }
        for (String v : g.variaveis()) {
            if (v == null || v.length() != 1) {
                throw new ValidacaoException("Variavel invalida (use 1 caractere): '" + v + "'");
            }
        }
        for (String t : nuloParaVazio(g.terminais())) {
            if (t == null || t.length() != 1) {
                throw new ValidacaoException("Terminal invalido (use 1 caractere): '" + t + "'");
            }
            if (g.variaveis().contains(t)) {
                throw new ValidacaoException("Simbolo '" + t + "' esta em variaveis e terminais ao mesmo tempo");
            }
        }
        if (g.simboloInicial() == null || g.simboloInicial().length() != 1
                || !g.variaveis().contains(g.simboloInicial())) {
            throw new ValidacaoException("Simbolo inicial '" + g.simboloInicial() + "' nao e uma variavel valida");
        }
        for (Producao p : nuloParaVazio(g.producoes())) {
            if (p.variavel() == null || !g.variaveis().contains(p.variavel())) {
                throw new ValidacaoException("Producao com lado esquerdo invalido: '" + p.variavel() + "'");
            }
            if (p.producao() != null && !EPSILON.contains(p.producao())) {
                for (char c : p.producao().toCharArray()) {
                    String s = String.valueOf(c);
                    if (!g.variaveis().contains(s) && !ehTerminal(g, c)) {
                        throw new ValidacaoException("Simbolo '" + s + "' da producao de '" + p.variavel()
                                + "' nao e variavel nem terminal");
                    }
                }
            }
        }
    }

    private <T> java.util.Collection<T> nuloParaVazio(java.util.Collection<T> c) {
        return c == null ? List.of() : c;
    }
}
