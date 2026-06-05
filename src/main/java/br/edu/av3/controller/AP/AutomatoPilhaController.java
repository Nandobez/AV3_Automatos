package br.edu.av3.controller.AP;

import br.edu.av3.dto.AP.ExecutarPilhaRequest;
import br.edu.av3.model.AP.ResultadoPilha;
import br.edu.av3.service.AP.AutomatoPilhaService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Modulo 4 - Simulador de Automato com Pilha (AP).
 * Pilha (push/pop). Linguagem minima: L = { a^n b^n | n >= 1 }.
 * Executa a cadeia e devolve o passo a passo (leitura, estado, conteudo da pilha).
 */
@RestController
@RequestMapping("/api/automato-pilha")
public class AutomatoPilhaController {

    private final AutomatoPilhaService service;

    public AutomatoPilhaController(AutomatoPilhaService service) {
        this.service = service;
    }

    /** POST /api/automato-pilha/executar -> executa a cadeia no automato com pilha. */
    @PostMapping("/executar")
    public ResultadoPilha executar(@RequestBody ExecutarPilhaRequest req) {
        return service.executar(req.automato(), req.cadeia());
    }
}
