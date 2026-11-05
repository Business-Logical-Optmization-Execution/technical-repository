import estilos from './Tabela.module.css'

export function Tabela({ legenda, colunas, linhas }) {

    return (
        <div className={estilos.contorno}>
            <table className={estilos.tabela}>
                <caption className="visualmente-oculto">{legenda}</caption>
                <thead>
                    <tr>
                        {colunas.map((coluna) => <th key={coluna.titulo} scope="col">{coluna.titulo}</th>)}
                    </tr>
                </thead>
                <tbody>
                    {linhas.map((linha) => (
                        <tr key={linha.id}>
                            {colunas.map((coluna) => <td key={coluna.titulo}>{coluna.valor(linha)}</td>)}
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    )
}
