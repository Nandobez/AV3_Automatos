import { useState } from 'react'
import Home from './Home.jsx'
import Debug from './Debug.jsx'

export default function App() {
  const [debug, setDebug] = useState(false)

  return (
    <div className="app">
      <header className="topo">
        <div>
          <h1>Simulador de Linguagens Formais e Modelos Computacionais</h1>
          <p>Aspectos Teoricos da Computacao</p>
        </div>
        <button className={debug ? 'debugBtn ativo' : 'debugBtn'} onClick={() => setDebug(!debug)}>
          {debug ? '← Voltar' : 'Debug'}
        </button>
      </header>

      {debug ? <Debug /> : <Home />}
    </div>
  )
}
