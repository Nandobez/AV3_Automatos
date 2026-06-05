package br.edu.av3.controller.MT;

import br.edu.av3.dto.MT.ExecutarMaquinaRequest;
import br.edu.av3.model.MT.ResultadoTuring;
import br.edu.av3.service.MT.MaquinaTuringService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Modulo 5 - Maquina de Turing.
 * Opcao A: reconhecer L = { w#w }. Opcao B: somar dois numeros em unario.
 * Executa a maquina e devolve o passo a passo (fita e cabecote).
 */
@RestController
@RequestMapping("/api/maquina-turing")
public class MaquinaTuringController {

    private final MaquinaTuringService service;

    public MaquinaTuringController(MaquinaTuringService service) {
        this.service = service;
    }

    /** POST /api/maquina-turing/executar -> executa a maquina sobre a entrada e devolve passos + fita final. */
    @PostMapping("/executar")
    public ResultadoTuring executar(@RequestBody ExecutarMaquinaRequest req) {
        return service.executar(req.maquina(), req.entrada());
    }
}
