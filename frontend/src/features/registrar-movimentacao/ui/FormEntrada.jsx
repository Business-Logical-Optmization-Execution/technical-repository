import { useState } from 'react'
import { obterErrosDeCampo } from '@/shared/api'
import { Botao, CampoAreaTexto, CampoTexto, useToast } from '@/shared/ui'
import { fraseSucesso } from '@/entities/movimentacao'
import { useRegistrarMovimentacao } from '../model/useRegistrarMovimentacao'
import { ModalConfirmarMovimentacao } from './ModalConfirmarMovimentacao'
import estilos from './FormEntrada.module.css'

const MAXIMO_DOCUMENTO = 120;
const MAXIMO_OBSERVACAO = 255;

function validar({ quantidade, documento, observacao }) {

    const erros = {};

    if (!/^\d+$/.test(quantidade) || Number(quantidade) === 0) {
        erros.quantidade = 'Informe um número inteiro maior que zero.';
    }
    if (documento.length > MAXIMO_DOCUMENTO) {
        erros.documento = 'Use no máximo ' + MAXIMO_DOCUMENTO + ' caracteres.';
    }
    if (observacao.length > MAXIMO_OBSERVACAO) {
        erros.observacao = 'Use no máximo ' + MAXIMO_OBSERVACAO + ' caracteres.';
    }

    return erros;
}

export function FormEntrada({ produto, aoConcluir, aoCancelar }) {

    const toast = useToast();
    const { registrar, enviando, erro, limparErro } = useRegistrarMovimentacao();
    const [campos, setCampos] = useState({ quantidade: '', documento: '', observacao: '' });
    const [erros, setErros] = useState({});
    const [confirmando, setConfirmando] = useState(false);

    const quantidade = campos.quantidade.trim();
    const documento = campos.documento.trim();
    const observacao = campos.observacao.trim();

    function alterar(campo) {
        return (evento) => {
            setCampos({ ...campos, [campo]: evento.target.value });
            setErros({ ...erros, [campo]: undefined });
        };
    }

    function aoEnviar(evento) {

        evento.preventDefault();

        const encontrados = validar({ quantidade, documento, observacao });
        setErros(encontrados);

        if (Object.keys(encontrados).length === 0) {
            setConfirmando(true);
        }
    }

    function voltar() {
        limparErro();
        setConfirmando(false);
    }

    async function confirmar() {

        try {
            const movimentacao = await registrar({
                produtoId: produto.id,
                tipo: 'ENTRADA',
                quantidade: Number(quantidade),
                documento: documento || null,
                observacao: observacao || null,
            });
            toast.sucesso(fraseSucesso(movimentacao));
            aoConcluir();
        } catch (e) {
            const errosDeCampo = obterErrosDeCampo(e);
            if (Object.keys(errosDeCampo).length > 0) {
                setErros(errosDeCampo);
                voltar();
            }
        }
    }

    return (
        <form className={estilos.formulario} noValidate onSubmit={aoEnviar}>
            <CampoTexto
                rotulo="Quantidade recebida"
                obrigatorio
                caixaAlta={false}
                inputMode="numeric"
                ajuda="Informe a quantidade que você contou, na mesma unidade do produto (un.)."
                erro={erros.quantidade}
                value={campos.quantidade}
                onChange={alterar('quantidade')}
            />
            <CampoTexto
                rotulo="Nota fiscal ou fornecedor (opcional)"
                caixaAlta={false}
                placeholder="Ex.: NF 12345 ou Distribuidora X"
                erro={erros.documento}
                value={campos.documento}
                onChange={alterar('documento')}
            />
            <CampoAreaTexto
                rotulo="Observação (opcional)"
                caixaAlta={false}
                erro={erros.observacao}
                value={campos.observacao}
                onChange={alterar('observacao')}
            />
            <div className={estilos.acoes}>
                <Botao variante="secundario" onClick={aoCancelar}>Cancelar</Botao>
                <Botao type="submit">Registrar entrada</Botao>
            </div>
            <ModalConfirmarMovimentacao
                aberto={confirmando}
                tipo="ENTRADA"
                quantidade={Number(quantidade)}
                produto={produto}
                enviando={enviando}
                erro={erro}
                aoVoltar={voltar}
                aoConfirmar={confirmar}
            />
        </form>
    )
}
