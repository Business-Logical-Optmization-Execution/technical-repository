import estilos from './CampoLeitura.module.css'

export function CampoLeitura({ rotulo, children, className = '' }) {

    return (
        <div className={estilos.campo + ' ' + className}>
            <dt className={estilos.rotulo}>{rotulo}</dt>
            <dd className={estilos.valor}>{children}</dd>
        </div>
    )
}
