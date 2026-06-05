package br.edu.av3.controller.LF;

import br.edu.av3.dto.LF.ConcatenarRequest;
import br.edu.av3.dto.LF.ValidacaoResponse;
import br.edu.av3.dto.LF.ValidarCadeiaRequest;
import br.edu.av3.model.LF.Alfabeto;
import br.edu.av3.service.LF.LinguagemFormalService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Modulo 1 - Linguagens Formais.
 * Alfabeto, validacao de cadeia, tamanho, concatenacao, Sigma* e Sigma+.
 */
@RestController
@RequestMapping("/api/linguagem-formal")
public class LinguagemFormalController {

    private final LinguagemFormalService service;

    public LinguagemFormalController(LinguagemFormalService service) {
        this.service = service;
    }

    /**
     * POST /api/linguagem-formal/validar -> valida se a cadeia pertence ao alfabeto e devolve o tamanho.
     * Se a cadeia for invalida, o service lanca ValidacaoException e o GlobalExceptionHandler
     * responde 400 com a mensagem do erro.
     */
    @PostMapping("/validar")
    public ValidacaoResponse validar(@RequestBody ValidarCadeiaRequest req) {
        Alfabeto alfabeto = new Alfabeto(req.alfabeto());
        service.validarCadeia(alfabeto, req.cadeia());
        return new ValidacaoResponse(true, service.tamanho(req.cadeia()), "Cadeia valida para Sigma");
    }

    /** POST /api/linguagem-formal/concatenar -> w1 . w2. */
    @PostMapping("/concatenar")
    public Map<String, Object> concatenar(@RequestBody ConcatenarRequest req) {
        String resultado = service.concatenar(req.w1(), req.w2());
        return Map.of("resultado", resultado, "tamanho", resultado.length());
    }

    /** GET /api/linguagem-formal/sigma-estrela?alfabeto=ab&maxTamanho=2 -> cadeias de Sigma* ate o tamanho dado. */
    @GetMapping("/sigma-estrela")
    public List<String> sigmaEstrela(@RequestParam String alfabeto, @RequestParam int maxTamanho) {
        return service.sigmaEstrela(new Alfabeto(alfabeto), maxTamanho);
    }

    /** GET /api/linguagem-formal/sigma-mais?alfabeto=ab&maxTamanho=2 -> cadeias de Sigma+ ate o tamanho dado. */
    @GetMapping("/sigma-mais")
    public List<String> sigmaMais(@RequestParam String alfabeto, @RequestParam int maxTamanho) {
        return service.sigmaMais(new Alfabeto(alfabeto), maxTamanho);
    }
}
