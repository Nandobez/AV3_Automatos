// Tabela editavel generica (usada para transicoes e producoes).
// colunas: [{ chave, rotulo, placeholder }]
export default function Linhas({ colunas, linhas, aoMudar }) {
  function mudar(i, chave, valor) {
    aoMudar(linhas.map((l, idx) => (idx === i ? { ...l, [chave]: valor } : l)))
  }
  function adicionar() {
    aoMudar([...linhas, Object.fromEntries(colunas.map((c) => [c.chave, '']))])
  }
  function remover(i) {
    aoMudar(linhas.filter((_, idx) => idx !== i))
  }

  return (
    <div className="linhas">
      <table>
        <thead>
          <tr>
            {colunas.map((c) => <th key={c.chave}>{c.rotulo}</th>)}
            <th></th>
          </tr>
        </thead>
        <tbody>
          {linhas.map((linha, i) => (
            <tr key={i}>
              {colunas.map((c) => (
                <td key={c.chave}>
                  <input
                    value={linha[c.chave] ?? ''}
                    placeholder={c.placeholder}
                    onChange={(e) => mudar(i, c.chave, e.target.value)}
                  />
                </td>
              ))}
              <td>
                <button className="rem" onClick={() => remover(i)} title="Remover">×</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      <button className="add" onClick={adicionar}>+ adicionar linha</button>
    </div>
  )
}
