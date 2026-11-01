import { useCallback, useEffect, useMemo, useRef, useState } from 'react'
import { CircleCheck, TriangleAlert, X } from 'lucide-react'
import { BotaoIcone } from '../botao'
import { ContextoToast } from './ContextoToast'
import estilos from './Toast.module.css'

const DURACAO = 2000;

function Toast({ id, tipo, texto, remover }) {

    useEffect(() => {
        const temporizador = setTimeout(() => remover(id), DURACAO);
        return () => clearTimeout(temporizador);
    }, [id, remover]);

    const Icone = tipo === 'erro' ? TriangleAlert : CircleCheck;

    return (
        <div className={estilos.toast + ' ' + estilos[tipo]}>
            <Icone size={20} aria-hidden="true" className={estilos.icone} />
            <p className={estilos.texto}>{texto}</p>
            <BotaoIcone icone={X} rotulo="Fechar" onClick={() => remover(id)} />
        </div>
    )
}

export function ToastProvider({ children }) {

    const [toasts, setToasts] = useState([]);
    const refProximoId = useRef(1);

    const remover = useCallback((id) => {
        setToasts((atuais) => atuais.filter((toast) => toast.id !== id));
    }, []);

    const mostrar = useCallback((tipo, texto) => {
        const id = refProximoId.current++;
        setToasts((atuais) => [...atuais, { id, tipo, texto }]);
    }, []);

    const valor = useMemo(() => ({
        sucesso: (texto) => mostrar('sucesso', texto),
        erro: (texto) => mostrar('erro', texto),
    }), [mostrar]);

    const sucessos = toasts.filter((toast) => toast.tipo === 'sucesso');
    const erros = toasts.filter((toast) => toast.tipo === 'erro');

    return (
        <ContextoToast value={valor}>
            {children}
            <div className={estilos.pilha}>
                <div role="alert" className={estilos.regiao}>
                    {erros.map((toast) => <Toast key={toast.id} {...toast} remover={remover} />)}
                </div>
                <div role="status" aria-live="polite" className={estilos.regiao}>
                    {sucessos.map((toast) => <Toast key={toast.id} {...toast} remover={remover} />)}
                </div>
            </div>
        </ContextoToast>
    )
}
