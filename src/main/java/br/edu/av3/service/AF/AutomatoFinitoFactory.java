package br.edu.av3.service.AF;

import br.edu.av3.model.AF.AutomatoFinito;
import br.edu.av3.model.AF.Transicao;
import br.edu.av3.util.Simbolos;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/** Factory que prepara uma estrutura de lookup eficiente para o AF/AFN. */
final class AutomatoFinitoFactory {

    private AutomatoFinitoFactory() {
    }

    static AutomatoFinitoCompilado compilar(AutomatoFinito af) {
        Map<ChaveTransicao, Set<String>> transicoes = new HashMap<>();
        Map<String, Set<String>> transicoesEpsilon = new HashMap<>();

        for (Transicao t : af.transicoes()) {
            if (Simbolos.ehEpsilon(t.simbolo())) {
                transicoesEpsilon
                        .computeIfAbsent(t.origem(), k -> new TreeSet<>())
                        .add(t.destino());
            } else {
                transicoes
                        .computeIfAbsent(new ChaveTransicao(t.origem(), t.simbolo()), k -> new TreeSet<>())
                        .add(t.destino());
            }
        }

        return new AutomatoFinitoCompilado(
                af.estadosFinais() == null ? Set.of() : new TreeSet<>(af.estadosFinais()),
                congelar(transicoes),
                congelar(transicoesEpsilon));
    }

    private static <K> Map<K, Set<String>> congelar(Map<K, Set<String>> origem) {
        Map<K, Set<String>> copia = new HashMap<>();
        origem.forEach((chave, destinos) ->
                copia.put(chave, Collections.unmodifiableSet(new TreeSet<>(destinos))));
        return Collections.unmodifiableMap(copia);
    }

    record AutomatoFinitoCompilado(
            Set<String> estadosFinais,
            Map<ChaveTransicao, Set<String>> transicoes,
            Map<String, Set<String>> transicoesEpsilon) {

        Set<String> destinos(String origem, String simbolo) {
            return transicoes.getOrDefault(new ChaveTransicao(origem, simbolo), Set.of());
        }

        Set<String> destinosEpsilon(String origem) {
            return transicoesEpsilon.getOrDefault(origem, Set.of());
        }
    }

    private record ChaveTransicao(String origem, String simbolo) {
    }
}
