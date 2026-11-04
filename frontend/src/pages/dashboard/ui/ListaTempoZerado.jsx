import { Link } from 'react-router-dom'
import { ROTAS } from '@/shared/config'
import { nomeProduto } from '@/shared/lib'
import estilos from './ListaIndicador.module.css'

function textoTempo(dias) {

    if (dias === null) {
        return 'Sem data registrada'
    }
    if (dias === 0) {
        return 'Zerou hoje'
    }

    return dias === 1 ? 'Zerado há 1 dia' : 'Zerado há ' + dias + ' dias'
}

export function ListaTempoZerado({ itens }) {

    if (itens.length === 0) {
        return <p className={estilos.vazio}>Nenhum produto zerado agora.</p>
    }

    return (
        <ul className={estilos.lista}>
            {itens.map(({ produto, dias }) => (
                <li key={produto.id} className={estilos.item}>
                    <Link to={ROTAS.produto(produto.id)} state={{ de: ROTAS.dashboard }} className={estilos.link}>
                        {nomeProduto(produto)}
                    </Link>
                    <span className={estilos.valor}>{textoTempo(dias)}</span>
                </li>
            ))}
        </ul>
    )
}
