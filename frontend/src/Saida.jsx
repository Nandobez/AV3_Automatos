import Resultado from './Resultado.jsx'

/** Area de saida padrao dos formularios: carregando / erro / resultado. */
export default function Saida({ carregando, erro, resultado }) {
  return (
    <div className="saida">
      {carregando && <p className="info">Executando...</p>}
      {erro && <div className="erro">Erro: {erro}</div>}
      {resultado && <Resultado dado={resultado} />}
    </div>
  )
}
