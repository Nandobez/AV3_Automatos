// Renderiza a resposta do backend de forma amigavel: um resumo (aceita/valida/...),
// o passo a passo (estados, pilha ou fita, conforme o modulo) e o JSON cru.

export default function Resultado({ dado }) {
  // Endpoints que devolvem uma lista pura (Sigma* / Sigma+).
  if (Array.isArray(dado)) {
    return (
      <div className="resultado">
        <h3>Cadeias ({dado.length})</h3>
        <div className="cadeias">
          {dado.map((c, i) => (
            <span key={i} className="chip">{c === '' ? 'λ' : c}</span>
          ))}
        </div>
      </div>
    )
  }

  return (
    <div className="resultado">
      <Resumo dado={dado} />
      {dado.passos && <Passos passos={dado.passos} />}
      {dado.arvore && (
        <div className="bloco">
          <h3>Arvore de derivacao</h3>
          <div className="tree"><Arvore raiz={dado.arvore} /></div>
        </div>
      )}
      <details className="bruto">
        <summary>JSON da resposta</summary>
        <pre>{JSON.stringify(dado, null, 2)}</pre>
      </details>
    </div>
  )
}

function Resumo({ dado }) {
  const itens = []
  if (dado.aceita !== undefined) {
    itens.push(
      <span key="ac" className={dado.aceita ? 'badge ok' : 'badge nao'}>
        {dado.aceita ? 'ACEITA' : 'REJEITA'}
      </span>
    )
  }
  if (dado.valida !== undefined) {
    itens.push(
      <span key="vl" className={dado.valida ? 'badge ok' : 'badge nao'}>
        {dado.valida ? 'VALIDA' : 'INVALIDA'}
      </span>
    )
  }
  if (dado.tamanho !== undefined) itens.push(<span key="tm" className="info">tamanho = {dado.tamanho}</span>)
  if (dado.resultado !== undefined) itens.push(<span key="rs" className="info">resultado = "{dado.resultado}"</span>)
  if (dado.estadoFinal) itens.push(<span key="ef" className="info">estado final = {dado.estadoFinal}</span>)
  if (dado.fitaFinal) itens.push(<span key="ff" className="info">fita final = {dado.fitaFinal}</span>)
  if (dado.mensagem) itens.push(<span key="ms" className="info">{dado.mensagem}</span>)
  if (dado.motivoParada) itens.push(<span key="mp" className="info">{dado.motivoParada}</span>)

  return itens.length ? <div className="resumo">{itens}</div> : null
}

function Passos({ passos }) {
  if (passos.length === 0) return null

  // GLC: passos = lista de strings (formas sentenciais).
  if (typeof passos[0] === 'string') {
    return (
      <div className="bloco">
        <h3>Derivacao passo a passo</h3>
        <div className="derivacao">
          {passos.map((forma, i) => (
            <span key={i}>
              <code className="forma">{forma}</code>
              {i < passos.length - 1 && <span className="seta"> ⇒ </span>}
            </span>
          ))}
        </div>
      </div>
    )
  }

  // Demais modulos: passos = lista de objetos -> tabela com as colunas.
  const colunas = Object.keys(passos[0])
  return (
    <div className="bloco">
      <h3>Passo a passo</h3>
      <table className="passos">
        <thead>
          <tr>{colunas.map((c) => <th key={c}>{c}</th>)}</tr>
        </thead>
        <tbody>
          {passos.map((p, i) => (
            <tr key={i}>
              {colunas.map((c) => <td key={c}><code>{formatar(p[c])}</code></td>)}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}

// Arvore em SVG: calcula a posicao de cada no (folhas distribuidas na horizontal,
// pais centralizados sobre os filhos) e liga os centros com linhas.
function Arvore({ raiz }) {
  const R = 16        // raio do no
  const EX = 48       // espacamento horizontal entre folhas
  const EY = 72       // espacamento vertical entre niveis
  const PAD = 16      // margem

  let folha = 0
  let profMax = 0

  function posicionar(no, prof) {
    profMax = Math.max(profMax, prof)
    const filhos = (no.filhos || []).map((f) => posicionar(f, prof + 1))
    let x
    if (filhos.length === 0) {
      x = folha * EX + R + PAD
      folha++
    } else {
      x = (filhos[0].x + filhos[filhos.length - 1].x) / 2
    }
    return { simbolo: no.simbolo, x, y: prof * EY + R + PAD, filhos }
  }

  const arvore = posicionar(raiz, 0)
  const largura = folha * EX + PAD * 2
  const altura = profMax * EY + R * 2 + PAD * 2

  const arestas = []
  const nos = []
  ;(function percorrer(n) {
    nos.push(n)
    n.filhos.forEach((f) => {
      arestas.push([n, f])
      percorrer(f)
    })
  })(arvore)

  const ehVariavel = (s) => /^[A-Z]/.test(s)

  return (
    <svg className="svgtree" width={largura} height={altura} viewBox={`0 0 ${largura} ${altura}`}>
      {arestas.map(([a, b], i) => (
        <line key={i} x1={a.x} y1={a.y} x2={b.x} y2={b.y} className="aresta" />
      ))}
      {nos.map((n, i) => (
        <g key={i}>
          <circle cx={n.x} cy={n.y} r={R} className={ehVariavel(n.simbolo) ? 'no variavel' : 'no'} />
          <text
            x={n.x}
            y={n.y}
            className={ehVariavel(n.simbolo) ? 'noTxt variavel' : 'noTxt'}
            textAnchor="middle"
            dominantBaseline="central"
          >
            {n.simbolo}
          </text>
        </g>
      ))}
    </svg>
  )
}

function formatar(v) {
  if (Array.isArray(v)) return '{' + v.join(', ') + '}'
  if (v === '') return 'λ'
  return String(v)
}
