import { useContext } from 'react'
import { ContextoSessao } from './ContextoSessao'

export function useSessao() {

    const contexto = useContext(ContextoSessao);

    if (!contexto) {
        throw new Error('useSessao precisa ser usado dentro do SessaoProvider.');
    }

    return contexto;
}
