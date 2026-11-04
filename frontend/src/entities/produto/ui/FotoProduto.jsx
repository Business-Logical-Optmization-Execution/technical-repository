import { Image } from 'lucide-react'
import estilos from './FotoProduto.module.css'

export function FotoProduto({ produto, className = '' }) {

    if (!produto.fotoUrl) {
        return (
            <div className={estilos.foto + ' ' + estilos.vazia + ' ' + className}>
                <Image aria-hidden="true" size={32} />
            </div>
        )
    }

    return <img src={produto.fotoUrl} alt={produto.nome} className={estilos.foto + ' ' + className} />
}
