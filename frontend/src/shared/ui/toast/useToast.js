import { useContext } from 'react'
import { ContextoToast } from './ContextoToast'

export function useToast() {

    const contexto = useContext(ContextoToast);

    if (!contexto) {
        throw new Error('useToast precisa ser usado dentro do ToastProvider.');
    }

    return contexto;
}
