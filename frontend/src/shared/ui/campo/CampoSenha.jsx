import { useState } from 'react'
import { Eye, EyeOff } from 'lucide-react'
import { BotaoIcone } from '../botao'
import { CampoTexto } from './CampoTexto'

export function CampoSenha({ ...resto }) {

    const [mostrar, setMostrar] = useState(false);

    const botaoOlho = (
        <BotaoIcone
            icone={mostrar ? EyeOff : Eye}
            rotulo={mostrar ? 'Ocultar senha' : 'Mostrar senha'}
            onClick={() => setMostrar(!mostrar)}
        />
    );

    return (
        <CampoTexto {...resto} tipo={mostrar ? 'text' : 'password'} acao={botaoOlho} />
    )
}
