import { useId } from 'react'
import { TriangleAlert } from 'lucide-react'
import estilos from './GrupoOpcoes.module.css'

export function GrupoOpcoes({ legenda, obrigatorio, opcoes, valor, aoMudar, erro }) {

    const nome = useId();
    const idErro = nome + '-erro';

    return (
        <fieldset className={estilos.grupo} aria-describedby={erro ? idErro : undefined}>
            <legend className={estilos.legenda}>{legenda + (obrigatorio ? ' (obrigatório)' : '')}</legend>
            <div className={estilos.opcoes}>
                {opcoes.map((opcao) => (
                    <label key={opcao.valor} className={estilos.opcao}>
                        <input
                            type="radio"
                            name={nome}
                            value={opcao.valor}
                            checked={valor === opcao.valor}
                            onChange={() => aoMudar(opcao.valor)}
                        />
                        {opcao.rotulo}
                    </label>
                ))}
            </div>
            {erro && (
                <p className={estilos.erro} id={idErro}>
                    <TriangleAlert size={16} aria-hidden="true" />{erro}
                </p>
            )}
        </fieldset>
    )
}
