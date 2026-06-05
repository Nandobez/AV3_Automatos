package br.edu.av3.controller.AF;

import br.edu.av3.dto.AF.ExecutarAutomatoRequest;
import br.edu.av3.model.AF.ResultadoExecucao;
import br.edu.av3.service.AF.AutomatoFinitoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Modulo 2 - Simulador de Automato Finito (AFD/AFN).
 * Carrega o automato por JSON, executa a cadeia e devolve o passo a passo das transicoes.
 */
@RestController
@RequestMapping("/api/automato-finito")
public class AutomatoFinitoController {

    private final AutomatoFinitoService service;

    public AutomatoFinitoController(AutomatoFinitoService service) {
        this.service = service;
    }

    /** POST /api/automato-finito/executar -> executa a cadeia no automato e devolve aceita + passos. */
    @PostMapping("/executar")
    public ResultadoExecucao executar(@RequestBody ExecutarAutomatoRequest req) {
        return service.executar(req.automato(), req.cadeia());
    }
}
