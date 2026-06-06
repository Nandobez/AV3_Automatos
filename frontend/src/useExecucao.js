import { useState } from 'react'
import { executar } from './api.js'

/** Estado de execucao (carregando/erro/resultado) compartilhado pelos formularios. */
export function useExecucao() {
  const [resultado, setResultado] = useState(null)
  const [erro, setErro] = useState(null)
  const [carregando, setCarregando] = useState(false)

  async function rodar(req) {
    setCarregando(true)
    setErro(null)
    setResultado(null)
    try {
      const { ok, dado } = await executar(req)
      if (!ok) setErro(dado.erro || JSON.stringify(dado))
      else setResultado(dado)
    } catch (e) {
      setErro('Falha na requisicao: ' + e.message + ' (o backend esta rodando na porta 8080?)')
    } finally {
      setCarregando(false)
    }
  }

  return { resultado, erro, carregando, rodar }
}
