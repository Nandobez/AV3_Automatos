import { useState } from 'react'
import { ACOES } from './acoes.js'
import Resultado from './Resultado.jsx'

// Modo Debug: editor de JSON cru chamando cada endpoint diretamente.
export default function Debug() {
  const [idx, setIdx] = useState(0)
  const [corpo, setCorpo] = useState(textoInicial(ACOES[0]))
  const [caminho, setCaminho] = useState(ACOES[0].path)
  const [resultado, setResultado] = useState(null)
  const [erro, setErro] = useState(null)
  const [carregando, setCarregando] = useState(false)

  const acao = ACOES[idx]

  function selecionar(novoIdx) {
    const a = ACOES[novoIdx]
    setIdx(novoIdx)
    setCorpo(textoInicial(a))
    setCaminho(a.path)
    setResultado(null)
    setErro(null)
  }

  async function executar() {
    setCarregando(true)
    setErro(null)
    setResultado(null)
    try {
      const opcoes = { method: acao.method, headers: { 'Content-Type': 'application/json' } }
      if (acao.method === 'POST') opcoes.body = corpo
      const url = acao.method === 'GET' ? caminho : acao.path
      const resp = await fetch(url, opcoes)
      const dado = await resp.json()
      if (!resp.ok) setErro(dado.erro || JSON.stringify(dado))
      else setResultado(dado)
    } catch (e) {
      setErro('Falha na requisicao: ' + e.message + ' (o backend esta rodando na porta 8080?)')
    } finally {
      setCarregando(false)
    }
  }

  return (
    <div className="painel">
      <aside className="menu">
        {ACOES.map((a, i) => (
          <button key={i} className={i === idx ? 'item ativo' : 'item'} onClick={() => selecionar(i)}>
            <span className="mod">{a.modulo}</span>
            <span className="ac">{a.label}</span>
          </button>
        ))}
      </aside>

      <main className="conteudo">
        <h2>{acao.modulo}</h2>
        <div className="rota">
          <span className="metodo">{acao.method}</span>
          {acao.method === 'GET' ? (
            <input className="path" value={caminho} onChange={(e) => setCaminho(e.target.value)} />
          ) : (
            <code>{acao.path}</code>
          )}
        </div>

        {acao.method === 'POST' && (
          <textarea className="corpo" value={corpo} onChange={(e) => setCorpo(e.target.value)} spellCheck={false} />
        )}

        <button className="executar" onClick={executar} disabled={carregando}>
          {carregando ? 'Executando...' : 'Executar'}
        </button>

        {erro && <div className="erro">Erro: {erro}</div>}
        {resultado && <Resultado dado={resultado} />}
      </main>
    </div>
  )
}

function textoInicial(acao) {
  return acao.exemplo ? JSON.stringify(acao.exemplo, null, 2) : ''
}
