package br.edu.av3.service.AP;

import br.edu.av3.model.AP.AutomatoPilha;
import br.edu.av3.model.AP.TransicaoPilha;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Factory que agrupa transicoes por estado sem alterar a ordem de prioridade. */
final class AutomatoPilhaFactory {

    private AutomatoPilhaFactory() {
    }

    static AutomatoPilhaCompilado compilar(AutomatoPilha ap) {
        Map<String, List<TransicaoPilha>> porEstado = new LinkedHashMap<>();
        for (TransicaoPilha t : ap.transicoes()) {
            porEstado.computeIfAbsent(t.estadoAtual(), k -> new ArrayList<>()).add(t);
        }

        Map<String, List<TransicaoPilha>> copia = new LinkedHashMap<>();
        porEstado.forEach((estado, transicoes) ->
                copia.put(estado, Collections.unmodifiableList(new ArrayList<>(transicoes))));

        return new AutomatoPilhaCompilado(
                ap.estadoInicial(),
                ap.estadosFinais() == null ? Set.of() : Set.copyOf(ap.estadosFinais()),
                ap.simboloInicialPilha().charAt(0),
                Collections.unmodifiableMap(copia));
    }

    record AutomatoPilhaCompilado(
            String estadoInicial,
            Set<String> estadosFinais,
            char simboloInicialPilha,
            Map<String, List<TransicaoPilha>> transicoesPorEstado) {

        List<TransicaoPilha> transicoesDoEstado(String estado) {
            return transicoesPorEstado.getOrDefault(estado, List.of());
        }
    }
}
