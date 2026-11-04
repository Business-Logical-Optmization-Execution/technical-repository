import { useSessao } from '@/entities/sessao'
import { Avatar, Gaveta, Selo } from '@/shared/ui'
import estilos from './Cabecalho.module.css'
import { useState } from 'react'
import { itensMenu } from '../config/itensMenu'
import { NavLink } from 'react-router-dom'
import { BotaoSair } from '@/features/autenticar'

export function Cabecalho() {

    const [menuAberto, setMenuAberto] = useState(false);
    const { usuario, ehAdmin } = useSessao();

    const itensVisiveis = itensMenu.filter((item) => ehAdmin || !item.somenteAdmin);

    return (
        <>
            <header className={estilos.barra}>
                <button type="button" className={estilos.abrirMenu} aria-label="Abrir menu" onClick={() => setMenuAberto(true)}>
                    <Avatar />
                    <span className={estilos.identificacao}>
                        <span>{usuario.nome}</span>
                        <span className={estilos.perfil}>{ehAdmin ? 'Admin' : 'Operador'}</span>
                    </span>
                </button>
                <span className={estilos.marca}>PORTAL ÓTTIMOS</span>
            </header>

            <Gaveta aberto={menuAberto} aoFechar={() => setMenuAberto(false)} titulo="Menu">
                <div className={estilos.identificacaoGaveta}>
                    <Avatar tamanho="grande" />
                    <span className={estilos.nomeGaveta}>{usuario.nome}</span>
                </div>
                <hr className={estilos.divisoria} />
                <nav aria-label="Menu principal" className={estilos.menu}>
                    <ul className={estilos.lista}>
                        {itensVisiveis.map((item) => (
                            <li key={item.rota}>
                                <NavLink to={item.rota} end className={estilos.item} onClick={() => setMenuAberto(false)}>
                                    {item.rotulo}
                                    {item.somenteAdmin && <Selo variante="admin">ADMIN</Selo>}
                                </NavLink>
                            </li>
                        ))}
                    </ul>
                </nav>
                <hr className={estilos.divisoria} />
                <BotaoSair />
            </Gaveta>
        </>
    )

}
