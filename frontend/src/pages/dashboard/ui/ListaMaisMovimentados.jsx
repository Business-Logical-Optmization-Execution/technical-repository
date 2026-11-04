import { Link } from 'react-router-dom'
import { ROTAS } from '@/shared/config'
import { formatarQuantidade, nomeProduto } from '@/shared/lib'
import estilos from './ListaMaisMovimentados.module.css'

export function ListaMaisMovimentados({ itens }) {

    if (itens.length === 0) {
        return <p className={estilos.vazio}>Nenhuma movimentação registrada.</p>
    }

    return (
        <ol className={estilos.lista}>
            {itens.map(({ produto, totalMovimentado }) => (
                <li key={produto.id} className={estilos.item}>
                    <Link to={ROTAS.produto(produto.id)} state={{ de: ROTAS.dashboard }} className={estilos.link}>
                        {nomeProduto(produto)}
                    </Link>
                    <span className={estilos.total}>{formatarQuantidade(totalMovimentado)}</span>
                </li>
            ))}
        </ol>
    )
}
