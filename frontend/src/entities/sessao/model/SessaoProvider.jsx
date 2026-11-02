import { useState } from 'react'
import { ContextoSessao } from './ContextoSessao'
import { entrar as entrarApi } from '../api/sessaoApi'
import { PERFIS } from '../config/perfis'

const CHAVE = 'portal-ottimos:sessao';
function lerSessaoSalva() {
    try {
        const salvo = localStorage.getItem(CHAVE);
        return salvo ? JSON.parse(salvo) : null;
    } catch {
        return null;
    }
}

export function SessaoProvider({ children }) {

    const [usuario, setUsuario] = useState(lerSessaoSalva);

    async function entrar({ email, senha }) {
        const usuarioLogado = await entrarApi({ email, senha });
        setUsuario(usuarioLogado);
        localStorage.setItem(CHAVE, JSON.stringify(usuarioLogado));
        return usuarioLogado;
    }

    function sair() {
        setUsuario(null);
        localStorage.removeItem(CHAVE);
    }

    const ehAdmin = usuario?.perfilAcesso?.nome === PERFIS.ADMIN;

    return (
        <ContextoSessao.Provider value={{ usuario, entrar, sair, ehAdmin }}>
            {children}
        </ContextoSessao.Provider>
    )

}