import { useState } from 'react';
import { useSessao } from '@/entities/sessao';
import { criarMovimentacao } from '@/entities/movimentacao';

export function useRegistrarMovimentacao() {

    const { usuario } = useSessao();
    const [enviando, setEnviando] = useState(false);
    const [erro, setErro] = useState(null);

    async function registrar(dados) {

        setEnviando(true);
        setErro(null);

        try {
            return await criarMovimentacao({ ...dados, usuarioId: usuario.id });
        } catch (e) {
            setErro(e);
            throw e;
        } finally {
            setEnviando(false);
        }
    }

    function limparErro() {
        setErro(null);
    }

    return { registrar, enviando, erro, limparErro };
}
