package br.edu.av3.model.MT;

import java.util.ArrayList;
import java.util.List;

/**
 * Modulo 5 - fita infinita (dos dois lados) da Maquina de Turing.
 * Baseada na Tape do projeto do professor: lista de celulas + ponteiro do cabecote,
 * expandindo com simbolo branco quando o cabecote sai dos limites.
 */
public class Fita {

    private final List<Character> celulas = new ArrayList<>();
    private int ponteiro;
    private final char branco;

    public Fita(String entrada, char branco) {
        this.branco = branco;
        celulas.add(branco);
        for (char c : entrada.toCharArray()) {
            celulas.add(c);
        }
        celulas.add(branco);
        ponteiro = 1;
    }

    public char ler() {
        return celulas.get(ponteiro);
    }

    public void escrever(char simbolo) {
        celulas.set(ponteiro, simbolo);
    }

    public void mover(char direcao) {
        if (direcao == 'R') {
            ponteiro++;
            if (ponteiro >= celulas.size()) {
                celulas.add(branco);
            }
        } else if (direcao == 'L') {
            ponteiro--;
            if (ponteiro < 0) {
                celulas.add(0, branco);
                ponteiro = 0;
            }
        }
        // qualquer outra direcao = fica parado
    }

    public String conteudo() {
        StringBuilder sb = new StringBuilder();
        for (char c : celulas) {
            sb.append(c);
        }
        return sb.toString();
    }

    public int getPonteiro() {
        return ponteiro;
    }
}
