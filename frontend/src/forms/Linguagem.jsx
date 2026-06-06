import { useState } from 'react'
import Campo from '../Campo.jsx'
import Saida from '../Saida.jsx'
import { useExecucao } from '../useExecucao.js'

// Modulo 1 - Linguagens Formais
export default function Linguagem() {
  const [op, setOp] = useState('validar')
  const [alfabeto, setAlfabeto] = useState('ab')
  const [cadeia, setCadeia] = useState('abba')
  const [w1, setW1] = useState('ab')
  const [w2, setW2] = useState('ba')
  const [maxTamanho, setMaxTamanho] = useState('2')
  const ex = useExecucao()

  function executar() {
    if (op === 'validar') {
      ex.rodar({ method: 'POST', path: '/api/linguagem-formal/validar', body: { alfabeto, cadeia } })
    } else if (op === 'concatenar') {
      ex.rodar({ method: 'POST', path: '/api/linguagem-formal/concatenar', body: { w1, w2 } })
    } else {
      const rota = op === 'sigma-estrela' ? 'sigma-estrela' : 'sigma-mais'
      ex.rodar({ method: 'GET', path: `/api/linguagem-formal/${rota}?alfabeto=${alfabeto}&maxTamanho=${maxTamanho}` })
    }
  }

  return (
    <div className="form">
      <label className="campo">
        <span>Operacao</span>
        <select value={op} onChange={(e) => setOp(e.target.value)}>
          <option value="validar">Validar cadeia</option>
          <option value="concatenar">Concatenar</option>
          <option value="sigma-estrela">Sigma* (ate tamanho)</option>
          <option value="sigma-mais">Sigma+ (ate tamanho)</option>
        </select>
      </label>

      {op === 'validar' && (
        <div className="campos">
          <Campo rotulo="Alfabeto (Σ)" valor={alfabeto} aoMudar={setAlfabeto} dica="ex.: ab" />
          <Campo rotulo="Cadeia" valor={cadeia} aoMudar={setCadeia} dica="ex.: abba" />
        </div>
      )}
      {op === 'concatenar' && (
        <div className="campos">
          <Campo rotulo="Cadeia w1" valor={w1} aoMudar={setW1} />
          <Campo rotulo="Cadeia w2" valor={w2} aoMudar={setW2} />
        </div>
      )}
      {(op === 'sigma-estrela' || op === 'sigma-mais') && (
        <div className="campos">
          <Campo rotulo="Alfabeto (Σ)" valor={alfabeto} aoMudar={setAlfabeto} dica="ex.: ab" />
          <Campo rotulo="Tamanho maximo" valor={maxTamanho} aoMudar={setMaxTamanho} dica="ex.: 2" />
        </div>
      )}

      <button className="executar" onClick={executar} disabled={ex.carregando}>Executar</button>
      <Saida {...ex} />
    </div>
  )
}
