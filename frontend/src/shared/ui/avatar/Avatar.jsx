import { UserRound } from 'lucide-react'
import estilos from './Avatar.module.css'

const TAMANHO_ICONE = { pequeno: 22, grande: 36 };

export function Avatar({ tamanho = 'pequeno' }) {

    return (
        <span className={estilos.avatar + ' ' + estilos[tamanho]}>
            <UserRound aria-hidden="true" size={TAMANHO_ICONE[tamanho]} />
        </span>
    )
}
