import { useState } from 'react';
import { alterarSenha } from '@/entities/usuario';

export function useAlterarSenha() {

    const [enviando, setEnviando] = useState(false);
    const [erro, setErro] = useState(null);

    async function enviar(id, dados) {

        setEnviando(true);
        setErro(null);

        try {
            await alterarSenha(id, dados);
        } catch (e) {
            setErro(e);
            throw e;
        } finally {
            setEnviando(false);
        }
    }

    return { enviar, enviando, erro };
}
