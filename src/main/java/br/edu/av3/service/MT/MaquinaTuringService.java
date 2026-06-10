package br.edu.av3.service.MT;

import br.edu.av3.exception.ValidacaoException;
import br.edu.av3.model.MT.Fita;
import br.edu.av3.model.MT.MaquinaTuring;
import br.edu.av3.model.MT.PassoTuring;
import br.edu.av3.model.MT.ResultadoTuring;
import br.edu.av3.model.MT.TransicaoTuring;
import br.edu.av3.service.ModeloComputacionalService;
import br.edu.av3.service.MT.MaquinaTuringFactory.MaquinaTuringCompilada;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Logica do Modulo 5 - Maquina de Turing.
 *
 * Mesma ideia do execute() do projeto do professor: a cada passo le o simbolo
 * sob o cabecote, procura a transicao (estadoAtual, simboloLido), escreve, move
 * e troca de estado. Aqui, em vez de imprimir no console, registramos cada passo
 * numa lista para a interface mostrar (estado, simbolo, fita, cabecote).
 *
 * Para por:
 *  - chegar em um estado final  -> aceita;
 *  - nao achar transicao        -> rejeita (parou sem transicao);
 *  - estourar o limite de passos -> rejeita (protege contra loop infinito).
 */
@Service
public class MaquinaTuringService implements ModeloComputacionalService<MaquinaTuring, String, ResultadoTuring> {

    /** Teto de passos para nao travar em maquinas que nao param. */
    private static final int LIMITE_PASSOS = 100_000;

    @Override
    public ResultadoTuring executar(MaquinaTuring maquina, String entrada) {
        validar(maquina);
        MaquinaTuringCompilada maquinaCompilada = MaquinaTuringFactory.compilar(maquina);

        Fita fita = new Fita(entrada == null ? "" : entrada, maquinaCompilada.branco());
        String estadoAtual = maquinaCompilada.estadoInicial();
        List<PassoTuring> passos = new ArrayList<>();

        int passo = 0;
        while (true) {
            char simbolo = fita.ler();
            passos.add(new PassoTuring(passo, estadoAtual, String.valueOf(simbolo),
                    fita.conteudo(), fita.getPonteiro()));

            if (maquinaCompilada.estadosFinais().contains(estadoAtual)) {
                return new ResultadoTuring(true, estadoAtual, fita.conteudo(),
                        "Alcancou estado final", passos);
            }

            TransicaoTuring transicao = maquinaCompilada.transicao(estadoAtual, simbolo);
            if (transicao == null) {
                return new ResultadoTuring(false, estadoAtual, fita.conteudo(),
                        "Parou sem transicao valida", passos);
            }

            fita.escrever(transicao.simboloEscrito());
            fita.mover(transicao.movimento());
            estadoAtual = transicao.novoEstado();
            passo++;

            if (passo > LIMITE_PASSOS) {
                return new ResultadoTuring(false, estadoAtual, fita.conteudo(),
                        "Limite de passos atingido (possivel loop)", passos);
            }
        }
    }

    private void validar(MaquinaTuring m) {
        if (m == null) {
            throw new ValidacaoException("Maquina nao informada");
        }
        if (m.estadoInicial() == null || m.estadoInicial().isBlank()) {
            throw new ValidacaoException("Estado inicial nao informado");
        }
        if (m.transicoes() == null) {
            throw new ValidacaoException("A maquina precisa de uma lista de transicoes");
        }
        for (TransicaoTuring t : m.transicoes()) {
            if (t == null) {
                throw new ValidacaoException("Transicao invalida: valor nulo");
            }
            if (t.estadoAtual() == null || t.estadoAtual().isBlank()
                    || t.novoEstado() == null || t.novoEstado().isBlank()) {
                throw new ValidacaoException("Transicao da maquina usa estado vazio");
            }
        }
    }
}
