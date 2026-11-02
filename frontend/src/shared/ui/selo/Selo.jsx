import estilos from './Selo.module.css'

export function Selo({children, variante = 'informacao', icone, ...resto}){

    const Icone = icone;

return (
    <span {...resto} className={estilos.selo + ' ' + estilos[variante]}>
        {icone && <Icone aria-hidden="true" size={14}/>}
        {children}
    </span>
)
};
