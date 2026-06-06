import { useState } from 'react'
import Linguagem from './forms/Linguagem.jsx'
import AutomatoFinito from './forms/AutomatoFinito.jsx'
import Gramatica from './forms/Gramatica.jsx'
import Pilha from './forms/Pilha.jsx'
import Turing from './forms/Turing.jsx'

const MODULOS = [
  { nome: 'Linguagens Formais', desc: 'Modulo 1', Form: Linguagem },
  { nome: 'Automato Finito', desc: 'Modulo 2 (AFD/AFN)', Form: AutomatoFinito },
  { nome: 'Gramatica (GLC)', desc: 'Modulo 3', Form: Gramatica },
  { nome: 'Automato com Pilha', desc: 'Modulo 4', Form: Pilha },
  { nome: 'Maquina de Turing', desc: 'Modulo 5', Form: Turing }
]

export default function Home() {
  const [idx, setIdx] = useState(0)
  const Form = MODULOS[idx].Form

  return (
    <div className="painel">
      <aside className="menu">
        {MODULOS.map((m, i) => (
          <button key={i} className={i === idx ? 'item ativo' : 'item'} onClick={() => setIdx(i)}>
            <span className="mod">{m.desc}</span>
            <span className="ac">{m.nome}</span>
          </button>
        ))}
      </aside>

      <main className="conteudo">
        <h2>{MODULOS[idx].nome}</h2>
        <Form />
      </main>
    </div>
  )
}
