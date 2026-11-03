import estilos from './Botao.module.css'

export function Botao({variante = 'primario', carregando, textoCarregando,children, icone, larguraTotal, como = 'button', ...resto}) {

    const Icone = icone;
    const Elemento = como;
    const estaDesabilitado = carregando || resto.disabled;
    const propsDeBotao = como === 'button' ? { type: resto.type || 'button', disabled: estaDesabilitado } : {};

    return (
        <Elemento {...resto} {...propsDeBotao} className={estilos.botao + ' ' + estilos[variante] + ' ' + (larguraTotal ? estilos.larguraTotal : '') } aria-busy={carregando}>
            {icone && !carregando && <Icone aria-hidden="true" size={20}/>}
            {carregando ? textoCarregando ?? children : children}
        </Elemento>
    )
};
