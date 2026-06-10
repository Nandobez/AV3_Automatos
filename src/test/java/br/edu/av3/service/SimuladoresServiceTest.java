package br.edu.av3.service;

import br.edu.av3.exception.ValidacaoException;
import br.edu.av3.model.AF.AutomatoFinito;
import br.edu.av3.model.AF.ResultadoExecucao;
import br.edu.av3.model.AF.Transicao;
import br.edu.av3.model.AP.AutomatoPilha;
import br.edu.av3.model.AP.ResultadoPilha;
import br.edu.av3.model.AP.TransicaoPilha;
import br.edu.av3.model.GLC.Gramatica;
import br.edu.av3.model.GLC.Producao;
import br.edu.av3.model.GLC.ResultadoDerivacao;
import br.edu.av3.model.LF.Alfabeto;
import br.edu.av3.model.MT.MaquinaTuring;
import br.edu.av3.model.MT.ResultadoTuring;
import br.edu.av3.model.MT.TransicaoTuring;
import br.edu.av3.service.AF.AutomatoFinitoService;
import br.edu.av3.service.AP.AutomatoPilhaService;
import br.edu.av3.service.GLC.GramaticaService;
import br.edu.av3.service.LF.LinguagemFormalService;
import br.edu.av3.service.MT.MaquinaTuringService;
import br.edu.av3.util.Simbolos;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SimuladoresServiceTest {

    @Test
    void automatoFinitoAceitaComTransicaoEpsilon() {
        AutomatoFinito af = new AutomatoFinito(
                Set.of("q0", "q1", "q2"),
                "q0",
                Set.of("q2"),
                List.of(
                        new Transicao("q0", Simbolos.LAMBDA, "q1"),
                        new Transicao("q1", "a", "q2")));

        ResultadoExecucao resultado = new AutomatoFinitoService().executar(af, "a");

        assertTrue(resultado.aceita());
        assertEquals(Set.of("q2"), resultado.estadosFinais());
    }

    @Test
    void automatoFinitoValidaDestinoMesmoEmEpsilon() {
        AutomatoFinito af = new AutomatoFinito(
                Set.of("q0"),
                "q0",
                Set.of(),
                List.of(new Transicao("q0", Simbolos.LAMBDA, "qX")));

        assertThrows(ValidacaoException.class, () -> new AutomatoFinitoService().executar(af, ""));
    }

    @Test
    void automatoPilhaAceitaANBN() {
        AutomatoPilha ap = new AutomatoPilha(
                Set.of("q0", "q1", "qf"),
                "q0",
                Set.of("qf"),
                "Z",
                List.of(
                        new TransicaoPilha("q0", "a", "Z", "q0", "AZ"),
                        new TransicaoPilha("q0", "a", "A", "q0", "AA"),
                        new TransicaoPilha("q0", "b", "A", "q1", Simbolos.LAMBDA),
                        new TransicaoPilha("q1", "b", "A", "q1", Simbolos.LAMBDA),
                        new TransicaoPilha("q1", Simbolos.LAMBDA, "Z", "qf", "Z")));

        ResultadoPilha resultado = new AutomatoPilhaService().executar(ap, "aabb");

        assertTrue(resultado.aceita());
    }

    @Test
    void maquinaTuringExecutaExemploUnario() {
        MaquinaTuring mt = new MaquinaTuring(
                "q0",
                Set.of("q3"),
                List.of(
                        new TransicaoTuring("q0", '0', "q0", '0', 'R'),
                        new TransicaoTuring("q0", '1', "q1", '0', 'R'),
                        new TransicaoTuring("q1", '0', "q1", '0', 'R'),
                        new TransicaoTuring("q1", 'B', "q2", 'B', 'L'),
                        new TransicaoTuring("q2", '0', "q3", 'B', 'R')),
                'B');

        ResultadoTuring resultado = new MaquinaTuringService().executar(mt, "000100");

        assertTrue(resultado.aceita());
        assertEquals("q3", resultado.estadoFinal());
    }

    @Test
    void gramaticaAceitaANBN() {
        Gramatica gramatica = new Gramatica(
                Set.of("S"),
                Set.of("a", "b"),
                List.of(
                        new Producao("S", "aSb"),
                        new Producao("S", Simbolos.LAMBDA)),
                "S");

        ResultadoDerivacao resultado = new GramaticaService().derivar(gramatica, "aabb");

        assertTrue(resultado.aceita());
        assertEquals("aabb", resultado.passos().getLast());
    }

    @Test
    void linguagemFormalBloqueiaGeracaoMuitoGrande() {
        LinguagemFormalService service = new LinguagemFormalService();

        assertThrows(ValidacaoException.class, () -> service.sigmaEstrela(new Alfabeto("ab"), 20));
    }
}
