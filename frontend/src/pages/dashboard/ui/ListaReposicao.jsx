import { Link } from 'react-router-dom'
import { ROTAS } from '@/shared/config'
import { formatarQuantidade, nomeProduto } from '@/shared/lib'
import estilos from './ListaIndicador.module.css'

function textoFalta(unidades) {
    return (unidades === 1 ? 'Falta ' : 'Faltam ') + formatarQuantidade(unidades)
}

export function ListaReposicao({ grupos }) {

    if (grupos.length === 0) {
        return <p className={estilos.vazio}>Nenhum produto precisa de reposição agora.</p>
    }

    return (
        <div>
            {grupos.map((grupo) => (
                <details key={grupo.chave} className={estilos.grupo}>
                    <summary className={estilos.resumoGrupo}>
                        <span className={estilos.nomeGrupo}>{grupo.nome ?? 'Sem fornecedor'}</span>
                        <span className={estilos.contagem}>{grupo.itens.length === 1 ? '1 produto' : grupo.itens.length + ' produtos'}</span>
                        <span className={estilos.valor}>{textoFalta(grupo.total)}</span>
                    </summary>
                    <ul className={estilos.lista}>
                        {grupo.itens.map(({ produto, falta }) => (
                            <li key={produto.id} className={estilos.item}>
                                <Link to={ROTAS.produto(produto.id)} state={{ de: ROTAS.dashboard }} className={estilos.link}>
                                    {nomeProduto(produto)}
                                </Link>
                                <span className={estilos.valor}>{textoFalta(falta)}</span>
                            </li>
                        ))}
                    </ul>
                </details>
            ))}
        </div>
    )
}
