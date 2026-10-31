import { useId } from 'react'
import estilos from './CampoTexto.module.css'
import { TriangleAlert, LockKeyhole } from 'lucide-react'

// acao: elemento clicável dentro do campo, à direita (ex.: o olho do CampoSenha).
export function CampoTexto({ rotulo, obrigatorio, ajuda, erro, bloqueado, motivoBloqueio, acao, tipo = 'text', ...resto }) {

    const id = useId();
    const idAjuda = id + '-ajuda';
    const idErro = id + '-erro';
    const idMotivo = id + '-motivo';

    const mostrarMotivo = bloqueado && motivoBloqueio;

    let idDescricao;
    if (erro) {
        idDescricao = idErro;
    } else if (mostrarMotivo) {
        idDescricao = idMotivo;
    } else if (ajuda) {
        idDescricao = idAjuda;
    }

    const classesEntrada = estilos.entrada
        + (erro ? ' ' + estilos.erro : '')
        + (bloqueado ? ' ' + estilos.bloqueado : '')
        + (acao ? ' ' + estilos.comAcao : '');

    return (
        <div className={estilos.campo}>
            <label className={estilos.rotulo} htmlFor={id}>
                {rotulo + (obrigatorio ? ' (obrigatório)' : '')}
            </label>
            <div className={estilos.caixa}>
                {bloqueado && <LockKeyhole size={16} aria-hidden="true" className={estilos.icone} />}
                <input {...resto} readOnly={bloqueado} type={tipo} id={id} aria-invalid={erro ? true : undefined} aria-describedby={idDescricao} className={classesEntrada} />
                {acao && <div className={estilos.acao}>{acao}</div>}
            </div>
            {ajuda && !erro && !mostrarMotivo && <p className={estilos.ajuda} id={idAjuda}>
                {ajuda}
            </p>}
            {erro && <p className={estilos.mensagemErro} id={idErro}>
                <TriangleAlert size={16} aria-hidden="true" />{erro}
            </p>}
            {mostrarMotivo && !erro && <p className={estilos.ajuda} id={idMotivo}>
                {motivoBloqueio}
            </p>}
        </div>
    )
}
