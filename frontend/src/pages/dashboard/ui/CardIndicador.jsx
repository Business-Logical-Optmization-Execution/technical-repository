import { Card } from '@/shared/ui'
import estilos from './CardIndicador.module.css'

export function CardIndicador({ rotulo, valor, icone, tom }) {

    const Icone = icone;

    return (
        <Card>
            <p className={estilos.rotulo}>{rotulo}</p>
            <p className={estilos.valor + (tom ? ' ' + estilos[tom] : '')}>
                {icone && <Icone size={24} aria-hidden="true" />}
                {valor}
            </p>
        </Card>
    )
}
