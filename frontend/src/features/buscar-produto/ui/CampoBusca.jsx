import { useId, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { Search } from 'lucide-react'
import { BotaoIcone } from '@/shared/ui'
import { ROTAS } from '@/shared/config'
import estilos from './CampoBusca.module.css'

const MINIMO_LETRAS = 2;

export function CampoBusca({ valorInicial = '', focoAutomatico = false, comBotao = false }) {

    const [termo, setTermo] = useState(valorInicial);
    const [mostrarAjuda, setMostrarAjuda] = useState(false);
    const navegar = useNavigate();
    const id = useId();
    const idAjuda = id + '-ajuda';

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

    const classesEntrada = estilos.entrada + ' ' + (comBotao ? estilos.comBotao : estilos.comIcone);

    return (
        <form role="search" className={estilos.formulario} onSubmit={aoEnviar}>
            <label htmlFor={id} className="visualmente-oculto">Pesquisar produto</label>
            <div className={estilos.caixa}>
                {!comBotao && <Search aria-hidden="true" size={20} className={estilos.icone} />}
                <input
                    id={id}
                    type="search"
                    className={classesEntrada}
                    placeholder="Pesquisar produto"
                    value={termo}
                    onChange={aoDigitar}
                    autoFocus={focoAutomatico}
                    aria-describedby={mostrarAjuda ? idAjuda : undefined}
                />
                {comBotao && (
                    <div className={estilos.acao}>
                        <BotaoIcone icone={Search} rotulo="Pesquisar" type="submit" />
                    </div>
                )}
            </div>
            <p id={idAjuda} className={estilos.ajuda} aria-live="polite">
                {mostrarAjuda ? 'Digite pelo menos 2 letras.' : ''}
            </p>
        </form>
    )
}
