package br.edu.av3.service;

/**
 * Strategy comum para os simuladores do projeto.
 * Cada modulo decide como executar seu proprio modelo, mantendo o contrato
 * externo simples: modelo + entrada -> resultado.
 */
public interface ModeloComputacionalService<TModelo, TEntrada, TResultado> {

    TResultado executar(TModelo modelo, TEntrada entrada);
}
