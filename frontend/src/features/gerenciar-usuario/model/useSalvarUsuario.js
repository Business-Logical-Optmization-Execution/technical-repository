import { useState } from 'react';
import { atualizarUsuario, criarUsuario } from '@/entities/usuario';

export function useSalvarUsuario() {

    const [enviando, setEnviando] = useState(false);
    const [erro, setErro] = useState(null);

    async function salvar(dados, id) {

        setEnviando(true);
        setErro(null);

        try {
            return await (id ? atualizarUsuario(id, dados) : criarUsuario(dados));
        } catch (e) {
            setErro(e);
            throw e;
        } finally {
            setEnviando(false);
        }
    }

    return { salvar, enviando, erro };
}
