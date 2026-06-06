import { useState } from 'react'
import Campo from '../Campo.jsx'
import Linhas from '../Linhas.jsx'
import Saida from '../Saida.jsx'
import { lista } from '../api.js'
import { useExecucao } from '../useExecucao.js'

// Modulo 3 - Gramatica Livre de Contexto
const COLUNAS = [
  { chave: 'variavel', rotulo: 'Variavel', placeholder: 'S' },
  { chave: 'producao', rotulo: 'Produz (use λ p/ vazio)', placeholder: 'aSb' }
]

export default function Gramatica() {
  const [variaveis, setVariaveis] = useState('S')
  const [terminais, setTerminais] = useState('a, b')
  const [simboloInicial, setSimboloInicial] = useState('S')
  const [cadeia, setCadeia] = useState('aaabbb')
  const [producoes, setProducoes] = useState([
    { variavel: 'S', producao: 'aSb' },
    { variavel: 'S', producao: 'λ' }
  ])
  const ex = useExecucao()

  function executar() {
    ex.rodar({
      method: 'POST',
      path: '/api/gramatica/derivar',
      body: {
        gramatica: {
          variaveis: lista(variaveis),
          terminais: lista(terminais),
          simboloInicial,
          producoes
        },
        cadeia
      }
    })
  }

  return (
    <div className="form">
      <div className="campos">
        <Campo rotulo="Variaveis (virgula)" valor={variaveis} aoMudar={setVariaveis} dica="S" />
        <Campo rotulo="Terminais (virgula)" valor={terminais} aoMudar={setTerminais} dica="a, b" />
        <Campo rotulo="Simbolo inicial" valor={simboloInicial} aoMudar={setSimboloInicial} dica="S" />
        <Campo rotulo="Cadeia" valor={cadeia} aoMudar={setCadeia} dica="aaabbb" />
      </div>
      <p className="rotulo">Producoes</p>
      <Linhas colunas={COLUNAS} linhas={producoes} aoMudar={setProducoes} />
      <button className="executar" onClick={executar} disabled={ex.carregando}>Executar</button>
      <Saida {...ex} />
    </div>
  )
}
