import { useEffect, useId, useRef, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { Search } from 'lucide-react'
import { BotaoIcone } from '@/shared/ui'
import { ROTAS } from '@/shared/config'
import estilos from './CampoBusca.module.css'

const MINIMO_LETRAS = 2;

export function CampoBusca({ valorInicial = '', focoAutomatico = false, comBotao = false, placeholder = 'Pesquisar produto' }) {

    const [termo, setTermo] = useState(valorInicial);
    const [valorAnterior, setValorAnterior] = useState(valorInicial);
    const [mostrarAjuda, setMostrarAjuda] = useState(false);
    const refEntrada = useRef(null);
    const navegar = useNavigate();
    const id = useId();
    const idAjuda = id + '-ajuda';

    if (valorInicial !== valorAnterior) {
        setValorAnterior(valorInicial);
        setTermo(valorInicial);
        setMostrarAjuda(false);
    }

    useEffect(() => {
        if (focoAutomatico) {
            refEntrada.current.focus();
        }
    }, [focoAutomatico]);

    function aoEnviar(evento) {
        evento.preventDefault();

        const termoLimpo = termo.trim();

        if (termoLimpo.length < MINIMO_LETRAS) {
            setMostrarAjuda(true);
            return;
        }

        navegar(ROTAS.buscaPor(termoLimpo));
    }

    function aoDigitar(evento) {
        setTermo(evento.target.value);
        setMostrarAjuda(false);
    }

    return (
        <form role="search" className={estilos.formulario} onSubmit={aoEnviar}>
            <label htmlFor={id} className="visualmente-oculto">Pesquisar produto</label>
            <div className={estilos.linha}>
                <div className={estilos.caixa}>
                    <Search aria-hidden="true" size={20} className={estilos.icone} />
                    <input
                        id={id}
                        ref={refEntrada}
                        type="search"
                        className={estilos.entrada}
                        placeholder={placeholder}
                        value={termo}
                        onChange={aoDigitar}
                        aria-describedby={mostrarAjuda ? idAjuda : undefined}
                    />
                </div>
                {comBotao && <BotaoIcone icone={Search} rotulo="Pesquisar" type="submit" />}
            </div>
            <p id={idAjuda} className={estilos.ajuda} aria-live="polite">
                {mostrarAjuda ? 'Digite pelo menos 2 letras.' : ''}
            </p>
        </form>
    )
}
