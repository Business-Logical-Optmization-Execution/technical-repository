import estilos from './Skeleton.module.css'

export function Skeleton ({altura, largura, ...resto}){

    return(
        <div {...resto} className={estilos.skeleton} aria-hidden="true" style={{ height:altura, width:largura}}></div>
    )
};