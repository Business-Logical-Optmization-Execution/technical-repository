import { useEffect, useRef, useId } from 'react';
import { BotaoIcone } from '../botao';
import { X } from 'lucide-react';
import estilos from './Gaveta.module.css';

export function Gaveta({ aberto, aoFechar, titulo, lado = 'esquerda', children }) {

    const refDialog = useRef(null);
    const refFocoAnterior = useRef(null);
    const id = useId();

    useEffect(() => {
        if (aberto) {
            refFocoAnterior.current = document.activeElement;
            refDialog.current.showModal();
        } else if (refDialog.current.open) {
            refDialog.current.close();
            refFocoAnterior.current?.focus();
        }
    }, [aberto]);

    // Sem isto, o navegador ignora o bloqueio do onCancel a partir do segundo Esc e fecha sozinho.
    useEffect(() => {
        if (!aberto) {
            return undefined;
        }

        function aoTeclar(evento) {
            if (evento.key === 'Escape') {
                evento.preventDefault();
                aoFechar();
            }
        }

        document.addEventListener('keydown', aoTeclar);

        return () => document.removeEventListener('keydown', aoTeclar);
    }, [aberto, aoFechar]);

    function aoCancelar(evento) {
        evento.preventDefault();
        aoFechar();
    }

    const classesGaveta = estilos.gaveta + (lado === 'direita' ? ' ' + estilos.direita : '');

    return (
        <dialog className={classesGaveta} aria-labelledby={id} ref={refDialog} onCancel={aoCancelar} onClick={(evento) => {
            if (evento.target === evento.currentTarget) {
                aoFechar();
            }
        }}>
            <div className={estilos.conteudo}>
                <div className={estilos.cabecalho}>
                    <h2 id={id} className={estilos.titulo}>{titulo}</h2>
                    <BotaoIcone icone={X} rotulo="Fechar" onClick={aoFechar} />
                </div>
                {children}
            </div>
        </dialog>
    )
}
