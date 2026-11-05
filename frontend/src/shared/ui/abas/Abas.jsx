import estilos from './Abas.module.css'

export function Abas({ itens, rotulo }) {

    return (
        <nav aria-label={rotulo} className={estilos.abas}>
            {itens.map((item) => (
                <button
                    key={item.rotulo}
                    type="button"
                    className={estilos.aba + ' ' + (item.ativa ? estilos.ativa : '')}
                    aria-current={item.ativa ? 'page' : undefined}
                    disabled={item.desabilitada}
                    title={item.title}
                >
                    {item.rotulo}
                </button>
            ))}
        </nav>
    )
}
