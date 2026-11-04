import { useId } from 'react'
import estilos from './CampoTexto.module.css'

export function CampoSelecao({ rotulo, opcoes, ...resto }) {

    const id = useId();

    return (
        <div className={estilos.campo}>
            <label className={estilos.rotulo + ' ' + estilos.rotuloComum} htmlFor={id}>{rotulo}</label>
            <select {...resto} id={id} className={estilos.entrada}>
                {opcoes.map((opcao) => <option key={opcao.valor} value={opcao.valor}>{opcao.rotulo}</option>)}
            </select>
        </div>
    )
}
