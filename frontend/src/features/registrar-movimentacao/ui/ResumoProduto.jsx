import { formatarQuantidade, nomeProduto } from '@/shared/lib'
import estilos from './ResumoProduto.module.css'

export function ResumoProduto({ produto }) {

    return (
        <p className={estilos.resumo}>
            {nomeProduto(produto)} · quantidade atual {formatarQuantidade(produto.quantidade)}
        </p>
    )
}
