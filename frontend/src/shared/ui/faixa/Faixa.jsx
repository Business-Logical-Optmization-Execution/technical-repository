import estilos from './Faixa.module.css'
import { TriangleAlert, Info } from 'lucide-react';

export function Faixa({variante = 'informacao', children, ...resto}){

    const Icone = variante === 'alerta' ? TriangleAlert : Info;

    return(
        <div {...resto} className={estilos.faixa + ' ' + estilos[variante]}>
            <Icone aria-hidden="true" size={20}/>
            {children}
        </div>
    )
};