import estilos from './EstadoVazio.module.css'

export function EstadoVazio({icone, texto, acao, ...resto}){

    const Icone = icone;

    return(
        <div {...resto} className={estilos.estadoVazio}>
            {icone && <Icone aria-hidden="true" size={32}></Icone>}
            <p>{texto}</p>
            {acao}
        </div>
    )
};