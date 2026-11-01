import { useEffect, useRef, useId } from 'react';
import { BotaoIcone } from '../botao';
import { X } from 'lucide-react';
import estilos from './Modal.module.css';

export function Modal({ aberto, aoFechar, titulo, acoes, children }) {

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

    function aoCancelar(evento) {
        evento.preventDefault();
        aoFechar();
    }

    return (
        <dialog className={estilos.modal} aria-labelledby={id} ref={refDialog} onCancel={aoCancelar} onClick={(evento) => {
            if (evento.target === evento.currentTarget) {
                aoFechar();
            }
        }}>
            <div className={estilos.conteudo}>
                <div className={estilos.cabecalho}>
                    <h2 className={estilos.titulo} id={id}>{titulo}</h2>
                    <BotaoIcone icone={X} rotulo="Fechar" onClick={aoFechar} />
                </div>
                <div>
                    {children}
                </div>
                {acoes && <div className={estilos.acoes}>{acoes}</div>}
            </div>
        </dialog>
    )
}