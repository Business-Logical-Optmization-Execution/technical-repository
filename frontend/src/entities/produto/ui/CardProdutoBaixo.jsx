import { Link, useLocation } from 'react-router-dom'
import { Card } from '@/shared/ui'
import { ROTAS } from '@/shared/config'
import { formatarLocalizacoes } from '../lib/formatarLocalizacoes'
import { FotoProduto } from './FotoProduto'
import { SeloSituacao } from './SeloSituacao'
import estilos from './CardProdutoBaixo.module.css'

export function CardProdutoBaixo({ produto, exibirFoto = true }) {

    const location = useLocation();

    return (
        <Card clicavel como={Link} to={ROTAS.produto(produto.id)} state={{ de: location.pathname + location.search }}>
            <div className={estilos.conteudo}>
                {exibirFoto && <FotoProduto produto={produto} className={estilos.foto} />}
                <div className={estilos.rodape}>
                    <div className={estilos.textos}>
                        <h3 className={estilos.nome}>{produto.nome}</h3>
                        <p className={estilos.local}>{formatarLocalizacoes(produto)}</p>
                    </div>
                    <SeloSituacao produto={produto} />
                </div>
            </div>
        </Card>
    )
}
