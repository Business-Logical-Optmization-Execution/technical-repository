import { formatarQuantidade, nomeProduto } from '@/shared/lib'
import estilos from './ResumoProduto.module.css'

export function ResumoProduto({ produto, mostrarQuantidade = true }) {

    return (
        <p className={estilos.resumo}>
            {nomeProduto(produto)}
            {mostrarQuantidade && ' · quantidade atual ' + formatarQuantidade(produto.quantidade)}
        </p>
    )
}
