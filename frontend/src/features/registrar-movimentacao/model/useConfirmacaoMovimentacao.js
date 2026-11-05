import { useState } from 'react';
import { obterErrosDeCampo } from '@/shared/api';
import { useToast } from '@/shared/ui';
import { fraseSucesso } from '@/entities/movimentacao';
import { useRegistrarMovimentacao } from './useRegistrarMovimentacao';

// Fluxo comum dos três formulários: abre o modal, grava e conclui.
// Erro que pertence a um campo fecha o modal; os demais ficam no modal, com "Tentar de novo".
export function useConfirmacaoMovimentacao({ produto, aoConcluir, aoErrarCampos, errosDeCampo = obterErrosDeCampo }) {

    const toast = useToast();
    const { registrar, enviando, erro, limparErro } = useRegistrarMovimentacao();
    const [aberto, setAberto] = useState(false);

    function voltar() {
        limparErro();
        setAberto(false);
    }

    async function confirmar(dados) {

        try {
            const movimentacao = await registrar({ ...dados, produtoId: produto.id });
            toast.sucesso(fraseSucesso(movimentacao));
            aoConcluir();
        } catch (e) {
            const campos = errosDeCampo(e);
            if (Object.keys(campos).length > 0) {
                aoErrarCampos(campos);
                voltar();
            }
        }
    }

    return {
        abrir: () => setAberto(true),
        confirmar,
        modal: { aberto, enviando, erro, aoVoltar: voltar },
    };
}
