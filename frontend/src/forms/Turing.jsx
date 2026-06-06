import { useState } from 'react'
import Campo from '../Campo.jsx'
import Linhas from '../Linhas.jsx'
import Saida from '../Saida.jsx'
import { lista } from '../api.js'
import { useExecucao } from '../useExecucao.js'

// Modulo 5 - Maquina de Turing
const COLUNAS = [
  { chave: 'estadoAtual', rotulo: 'Estado', placeholder: 'q0' },
  { chave: 'simboloLido', rotulo: 'Le', placeholder: '0' },
  { chave: 'novoEstado', rotulo: 'Vai para', placeholder: 'q1' },
  { chave: 'simboloEscrito', rotulo: 'Escreve', placeholder: '0' },
  { chave: 'movimento', rotulo: 'Move (L/R)', placeholder: 'R' }
]

export default function Turing() {
  const [inicial, setInicial] = useState('q0')
  const [finais, setFinais] = useState('q3')
  const [branco, setBranco] = useState('B')
  const [entrada, setEntrada] = useState('000100')
  const [transicoes, setTransicoes] = useState([
    { estadoAtual: 'q0', simboloLido: '0', novoEstado: 'q0', simboloEscrito: '0', movimento: 'R' },
    { estadoAtual: 'q0', simboloLido: '1', novoEstado: 'q1', simboloEscrito: '0', movimento: 'R' },
    { estadoAtual: 'q1', simboloLido: '0', novoEstado: 'q1', simboloEscrito: '0', movimento: 'R' },
    { estadoAtual: 'q1', simboloLido: 'B', novoEstado: 'q2', simboloEscrito: 'B', movimento: 'L' },
    { estadoAtual: 'q2', simboloLido: '0', novoEstado: 'q3', simboloEscrito: 'B', movimento: 'R' }
  ])
  const ex = useExecucao()

  function executar() {
    // simbolos de 1 caractere: pega so o 1o char dos campos relevantes
    const trans = transicoes.map((t) => ({
      estadoAtual: t.estadoAtual,
      simboloLido: (t.simboloLido || '')[0] || '',
      novoEstado: t.novoEstado,
      simboloEscrito: (t.simboloEscrito || '')[0] || '',
      movimento: (t.movimento || '')[0] || ''
    }))
    ex.rodar({
      method: 'POST',
      path: '/api/maquina-turing/executar',
      body: {
        maquina: { estadoInicial: inicial, estadosFinais: lista(finais), branco: (branco || 'B')[0], transicoes: trans },
        entrada
      }
    })
  }

  return (
    <div className="form">
      <div className="campos">
        <Campo rotulo="Estado inicial" valor={inicial} aoMudar={setInicial} />
        <Campo rotulo="Estados finais (virgula)" valor={finais} aoMudar={setFinais} dica="q3" />
        <Campo rotulo="Simbolo branco" valor={branco} aoMudar={setBranco} dica="B" />
        <Campo rotulo="Entrada (fita)" valor={entrada} aoMudar={setEntrada} dica="000100" />
      </div>
      <p className="rotulo">Transicoes</p>
      <Linhas colunas={COLUNAS} linhas={transicoes} aoMudar={setTransicoes} />
      <button className="executar" onClick={executar} disabled={ex.carregando}>Executar</button>
      <Saida {...ex} />
    </div>
  )
}
