import { useState } from 'react';
import { salvarConfiguracoes } from '@/entities/configuracao';

export function useSalvarConfiguracoes() {

    const [enviando, setEnviando] = useState(false);
    const [erro, setErro] = useState(null);

    async function salvar(dados) {

        setEnviando(true);
        setErro(null);

        try {
            return await salvarConfiguracoes(dados);
        } catch (e) {
            setErro(e);
            throw e;
        } finally {
            setEnviando(false);
        }
    }

    return { salvar, enviando, erro };
}
