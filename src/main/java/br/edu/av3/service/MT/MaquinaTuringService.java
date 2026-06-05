package br.edu.av3.service.MT;

import br.edu.av3.exception.ValidacaoException;
import br.edu.av3.model.MT.Fita;
import br.edu.av3.model.MT.MaquinaTuring;
import br.edu.av3.model.MT.PassoTuring;
import br.edu.av3.model.MT.ResultadoTuring;
import br.edu.av3.model.MT.TransicaoTuring;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
public class MaquinaTuringService {

    /** Teto de passos para nao travar em maquinas que nao param. */
    private static final int LIMITE_PASSOS = 100_000;

    public ResultadoTuring executar(MaquinaTuring maquina, String entrada) {
        validar(maquina);

        char branco = maquina.branco() == 0 ? 'B' : maquina.branco();
        Set<String> finais = maquina.estadosFinais();

        Fita fita = new Fita(entrada == null ? "" : entrada, branco);
        String estadoAtual = maquina.estadoInicial();
        List<PassoTuring> passos = new ArrayList<>();

        int passo = 0;
        while (true) {
            char simbolo = fita.ler();
            passos.add(new PassoTuring(passo, estadoAtual, String.valueOf(simbolo),
                    fita.conteudo(), fita.getPonteiro()));

            // Estado final -> aceita e para.
            if (finais != null && finais.contains(estadoAtual)) {
                return new ResultadoTuring(true, estadoAtual, fita.conteudo(),
                        "Alcancou estado final", passos);
            }

            final String estado = estadoAtual;
            Optional<TransicaoTuring> transicao = maquina.transicoes().stream()
                    .filter(t -> t.estadoAtual().equals(estado) && t.simboloLido() == simbolo)
                    .findFirst();

            // Sem transicao -> para (rejeita).
            if (transicao.isEmpty()) {
                return new ResultadoTuring(false, estadoAtual, fita.conteudo(),
                        "Parou sem transicao valida", passos);
            }

            TransicaoTuring t = transicao.get();
            fita.escrever(t.simboloEscrito());
            fita.mover(t.movimento());
            estadoAtual = t.novoEstado();
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
    }
}
