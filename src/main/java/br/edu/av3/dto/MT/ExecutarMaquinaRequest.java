package br.edu.av3.dto.MT;

import br.edu.av3.model.MT.MaquinaTuring;

/** Corpo da requisicao: a definicao da maquina e a entrada (fita inicial). */
public record ExecutarMaquinaRequest(MaquinaTuring maquina, String entrada) {
}
