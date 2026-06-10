package br.edu.av3.service.MT;

import br.edu.av3.model.MT.MaquinaTuring;
import br.edu.av3.model.MT.TransicaoTuring;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/** Factory que indexa transicoes da MT por estado e simbolo lido. */
final class MaquinaTuringFactory {

    private MaquinaTuringFactory() {
    }

    static MaquinaTuringCompilada compilar(MaquinaTuring maquina) {
        Map<ChaveTransicao, TransicaoTuring> transicoes = new LinkedHashMap<>();
        for (TransicaoTuring t : maquina.transicoes()) {
            transicoes.putIfAbsent(new ChaveTransicao(t.estadoAtual(), t.simboloLido()), t);
        }

        char branco = maquina.branco() == 0 ? 'B' : maquina.branco();
        return new MaquinaTuringCompilada(
                maquina.estadoInicial(),
                maquina.estadosFinais() == null ? Set.of() : Set.copyOf(maquina.estadosFinais()),
                branco,
                Collections.unmodifiableMap(transicoes));
    }

    record MaquinaTuringCompilada(
            String estadoInicial,
            Set<String> estadosFinais,
            char branco,
            Map<ChaveTransicao, TransicaoTuring> transicoes) {

        TransicaoTuring transicao(String estado, char simboloLido) {
            return transicoes.get(new ChaveTransicao(estado, simboloLido));
        }
    }

    private record ChaveTransicao(String estado, char simboloLido) {
    }
}
