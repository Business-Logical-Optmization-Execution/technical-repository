import { useNavigate } from 'react-router-dom'
import { ArrowLeft } from 'lucide-react'
import { useTituloDocumento } from '@/shared/lib'
import { BotaoIcone } from '../botao'
import { Selo } from '../selo'
import estilos from './TituloPagina.module.css'

export function TituloPagina({titulo, admin, voltarPara, estadoVoltar, acoes, ...resto}){

    const navegar = useNavigate();
    useTituloDocumento(titulo);

    return (
        <div {...resto} className={estilos.linha}>
            {voltarPara && <BotaoIcone icone={ArrowLeft} rotulo="Voltar" onClick={() => navegar(voltarPara, { state: estadoVoltar })} />}
            <h1 className={estilos.titulo}>
            {titulo}
            </h1>
            {admin && <Selo variante="admin">ADMIN</Selo>}
            {acoes && <div className={estilos.acoes}>{acoes}</div>}
        </div>
    )
};

