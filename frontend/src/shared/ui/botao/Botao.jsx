import estilos from './Botao.module.css'

export function Botao({variante = 'primario', carregando, textoCarregando,children, icone, larguraTotal, ...resto}) {

    const Icone = icone;
    const estaDesabilitado = carregando || resto.disabled;

    return (
        <button type={resto.type || 'button'} {...resto} className={estilos.botao + ' ' + estilos[variante] + ' ' + (larguraTotal ? estilos.larguraTotal : '') } disabled={estaDesabilitado} aria-busy={carregando}>
            {icone && !carregando && <Icone aria-hidden="true" size={20}/>}
            {carregando ? textoCarregando : children}
        </button>
    )
};
