package br.edu.av3.controller.GLC;

import br.edu.av3.dto.GLC.DerivarRequest;
import br.edu.av3.model.GLC.ResultadoDerivacao;
import br.edu.av3.service.GLC.GramaticaService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Modulo 3 - Simulador de Gramatica Livre de Contexto (GLC).
 * Variaveis, terminais, producoes, simbolo inicial.
 * Derivacao passo a passo e arvore de derivacao simplificada.
 */
@RestController
@RequestMapping("/api/gramatica")
public class GramaticaController {

    private final GramaticaService service;

    public GramaticaController(GramaticaService service) {
        this.service = service;
    }

    /** POST /api/gramatica/derivar -> tenta derivar a cadeia e devolve aceita + passos + arvore. */
    @PostMapping("/derivar")
    public ResultadoDerivacao derivar(@RequestBody DerivarRequest req) {
        return service.derivar(req.gramatica(), req.cadeia());
    }
}
