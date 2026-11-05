import estilos from './CaixaMarcar.module.css'

export function CaixaMarcar({ rotulo, ...resto }) {
    return (
        <label className={estilos.linha}>
            <span>{rotulo}</span>
            <span className={estilos.area}>
                <input {...resto} type="checkbox" className={estilos.caixa} />
            </span>
        </label>
    )
}
