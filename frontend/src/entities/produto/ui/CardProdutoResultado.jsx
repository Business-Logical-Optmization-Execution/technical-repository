import { Link, useLocation } from 'react-router-dom'
import { Card } from '@/shared/ui'
import { ROTAS } from '@/shared/config'
import { formatarMoeda } from '@/shared/lib'
import { formatarLocalizacoes } from '../lib/formatarLocalizacoes'
import { FotoProduto } from './FotoProduto'
import { SeloSituacao } from './SeloSituacao'
import estilos from './CardProdutoResultado.module.css'

export function CardProdutoResultado({ produto, exibirFoto = true }) {

    const location = useLocation();

    return (
        <Card clicavel como={Link} to={ROTAS.produto(produto.id)} state={{ de: location.pathname + location.search }}>
            <div className={exibirFoto ? estilos.conteudo : estilos.conteudoSemFoto}>
                {exibirFoto && <FotoProduto produto={produto} className={estilos.foto} />}
                <div className={estilos.dados}>
                    <p>ID <span className={estilos.codigo}>{produto.sku}</span></p>
                    <h3 className={estilos.nome}>{produto.nome}</h3>
                    <p>Qtd {produto.quantidade} · {formatarMoeda(produto.preco)}</p>
                    <p className={estilos.local}>
                        {produto.localizacoes?.length ? 'Local ' : ''}{formatarLocalizacoes(produto)}
                    </p>
                    <SeloSituacao produto={produto} mostrarNome />
                </div>
            </div>
        </Card>
    )
}
