import { LockKeyhole } from 'lucide-react'
import estilos from './CampoLeitura.module.css'

export function CampoLeitura({ rotulo, children, bloqueado = false, className = '' }) {

    return (
        <div className={estilos.campo + ' ' + className}>
            <dt className={estilos.rotulo}>{rotulo}</dt>
            <dd className={estilos.valor}>
                <span className={estilos.conteudo}>{children}</span>
                {bloqueado && <LockKeyhole aria-hidden="true" size={16} className={estilos.cadeado} />}
            </dd>
        </div>
    )
}
