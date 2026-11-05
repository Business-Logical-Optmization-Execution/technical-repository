import { ArrowRight } from 'lucide-react'
import { useSessao } from '@/entities/sessao'
import estilos from './BotaoSair.module.css'

export function BotaoSair() {

    const { sair } = useSessao();

    return (
        <button type="button" className={estilos.botao} onClick={sair}>
            Sair <ArrowRight aria-hidden="true" size={16} />
        </button>
    )
}
