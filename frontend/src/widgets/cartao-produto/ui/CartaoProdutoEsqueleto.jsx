import { Card, Skeleton } from '@/shared/ui'
import estilos from './CartaoProduto.module.css'

export function CartaoProdutoEsqueleto() {

    return (
        <Card aria-busy="true">
            <p className="visualmente-oculto">Carregando produto...</p>
            <div className={estilos.corpo}>
                <Skeleton altura="240px" />
                <div className={estilos.campos}>
                    <div className={estilos.inteiro}><Skeleton altura="72px" /></div>
                    <div className={estilos.terco}><Skeleton altura="72px" /></div>
                    <div className={estilos.terco}><Skeleton altura="72px" /></div>
                    <div className={estilos.terco}><Skeleton altura="72px" /></div>
                    <div className={estilos.inteiro}><Skeleton altura="72px" /></div>
                </div>
            </div>
        </Card>
    )
}
