import { useState } from 'react'
import Campo from '../Campo.jsx'
import Linhas from '../Linhas.jsx'
import Saida from '../Saida.jsx'
import { lista, reunirEstados } from '../api.js'
import { useExecucao } from '../useExecucao.js'

// Modulo 2 - Automato Finito (AFD/AFN)
const COLUNAS = [
  { chave: 'origem', rotulo: 'De', placeholder: 'q0' },
  { chave: 'simbolo', rotulo: 'Le', placeholder: 'a' },
  { chave: 'destino', rotulo: 'Vai para', placeholder: 'q1' }
]

export default function AutomatoFinito() {
  const [inicial, setInicial] = useState('q0')
  const [finais, setFinais] = useState('q2')
  const [cadeia, setCadeia] = useState('ab')
  const [transicoes, setTransicoes] = useState([
    { origem: 'q0', simbolo: 'a', destino: 'q1' },
    { origem: 'q1', simbolo: 'b', destino: 'q2' }
  ])
  const ex = useExecucao()

  function executar() {
    const estadosFinais = lista(finais)
    const estados = reunirEstados(inicial, estadosFinais, transicoes, 'origem', 'destino')
    ex.rodar({
      method: 'POST',
      path: '/api/automato-finito/executar',
      body: { automato: { estados, estadoInicial: inicial, estadosFinais, transicoes }, cadeia }
    })
  }

  return (
    <div className="form">
      <div className="campos">
        <Campo rotulo="Estado inicial" valor={inicial} aoMudar={setInicial} />
        <Campo rotulo="Estados finais (virgula)" valor={finais} aoMudar={setFinais} dica="q2" />
        <Campo rotulo="Cadeia" valor={cadeia} aoMudar={setCadeia} dica="ab" />
      </div>
      <p className="rotulo">Transicoes (use λ no simbolo para epsilon)</p>
      <Linhas colunas={COLUNAS} linhas={transicoes} aoMudar={setTransicoes} />
      <button className="executar" onClick={executar} disabled={ex.carregando}>Executar</button>
      <Saida {...ex} />
    </div>
  )
}
