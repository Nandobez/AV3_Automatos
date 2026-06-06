import { useState } from 'react'
import Campo from '../Campo.jsx'
import Linhas from '../Linhas.jsx'
import Saida from '../Saida.jsx'
import { lista, reunirEstados } from '../api.js'
import { useExecucao } from '../useExecucao.js'

// Modulo 4 - Automato com Pilha (a^n b^n)
const COLUNAS = [
  { chave: 'estadoAtual', rotulo: 'Estado', placeholder: 'q0' },
  { chave: 'simboloLido', rotulo: 'Le (λ=vazio)', placeholder: 'a' },
  { chave: 'desempilha', rotulo: 'Desempilha', placeholder: 'Z' },
  { chave: 'novoEstado', rotulo: 'Vai para', placeholder: 'q0' },
  { chave: 'empilha', rotulo: 'Empilha (λ=nada)', placeholder: 'AZ' }
]

export default function Pilha() {
  const [inicial, setInicial] = useState('q0')
  const [finais, setFinais] = useState('qf')
  const [pilhaInicial, setPilhaInicial] = useState('Z')
  const [cadeia, setCadeia] = useState('aabb')
  const [transicoes, setTransicoes] = useState([
    { estadoAtual: 'q0', simboloLido: 'a', desempilha: 'Z', novoEstado: 'q0', empilha: 'AZ' },
    { estadoAtual: 'q0', simboloLido: 'a', desempilha: 'A', novoEstado: 'q0', empilha: 'AA' },
    { estadoAtual: 'q0', simboloLido: 'b', desempilha: 'A', novoEstado: 'q1', empilha: 'λ' },
    { estadoAtual: 'q1', simboloLido: 'b', desempilha: 'A', novoEstado: 'q1', empilha: 'λ' },
    { estadoAtual: 'q1', simboloLido: 'λ', desempilha: 'Z', novoEstado: 'qf', empilha: 'Z' }
  ])
  const ex = useExecucao()

  function executar() {
    const estadosFinais = lista(finais)
    const estados = reunirEstados(inicial, estadosFinais, transicoes, 'estadoAtual', 'novoEstado')
    ex.rodar({
      method: 'POST',
      path: '/api/automato-pilha/executar',
      body: {
        automato: { estados, estadoInicial: inicial, estadosFinais, simboloInicialPilha: pilhaInicial, transicoes },
        cadeia
      }
    })
  }

  return (
    <div className="form">
      <div className="campos">
        <Campo rotulo="Estado inicial" valor={inicial} aoMudar={setInicial} />
        <Campo rotulo="Estados finais (virgula)" valor={finais} aoMudar={setFinais} dica="qf" />
        <Campo rotulo="Simbolo inicial da pilha" valor={pilhaInicial} aoMudar={setPilhaInicial} dica="Z" />
        <Campo rotulo="Cadeia" valor={cadeia} aoMudar={setCadeia} dica="aabb" />
      </div>
      <p className="rotulo">Transicoes</p>
      <Linhas colunas={COLUNAS} linhas={transicoes} aoMudar={setTransicoes} />
      <button className="executar" onClick={executar} disabled={ex.carregando}>Executar</button>
      <Saida {...ex} />
    </div>
  )
}
