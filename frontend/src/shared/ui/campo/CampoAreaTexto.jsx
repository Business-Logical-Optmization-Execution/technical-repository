import { useId } from 'react'
import estilos from './CampoTexto.module.css'
import estilosArea from './CampoAreaTexto.module.css'
import { TriangleAlert } from 'lucide-react'

export function CampoAreaTexto({ rotulo, obrigatorio, ajuda, erro, caixaAlta = true, ...resto }) {

    const id = useId();
    const idAjuda = id + '-ajuda';
    const idErro = id + '-erro';

    let idDescricao;
    if (erro) {
        idDescricao = idErro;
    } else if (ajuda) {
        idDescricao = idAjuda;
    }

    const classesEntrada = estilos.entrada + ' ' + estilosArea.areaTexto
        + (erro ? ' ' + estilos.erro : '');

    return (
        <div className={estilos.campo}>
            <label className={estilos.rotulo + (caixaAlta ? '' : ' ' + estilos.rotuloComum)} htmlFor={id}>
                {rotulo + (obrigatorio ? ' (obrigatório)' : '')}
            </label>
            <textarea rows={3} {...resto} id={id} aria-invalid={erro ? true : undefined} aria-describedby={idDescricao} className={classesEntrada} />
            {ajuda && !erro && <p className={estilos.ajuda} id={idAjuda}>
                {ajuda}
            </p>}
            {erro && <p className={estilos.mensagemErro} id={idErro}>
                <TriangleAlert size={16} aria-hidden="true" />{erro}
            </p>}
        </div>
    )
}
