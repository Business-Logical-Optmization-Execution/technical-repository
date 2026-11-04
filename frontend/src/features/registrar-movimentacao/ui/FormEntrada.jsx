import { useState } from 'react'
import { Botao, CampoAreaTexto, CampoTexto } from '@/shared/ui'
import { useConfirmacaoMovimentacao } from '../model/useConfirmacaoMovimentacao'
import { ModalConfirmarMovimentacao } from './ModalConfirmarMovimentacao'
import { ResumoProduto } from './ResumoProduto'
import estilos from './FormMovimentacao.module.css'

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

    const [campos, setCampos] = useState({ quantidade: '', documento: '', observacao: '' });
    const [erros, setErros] = useState({});
    const { abrir, confirmar, modal } = useConfirmacaoMovimentacao({ produto, aoConcluir, aoErrarCampos: setErros });

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
            abrir();
        }
    }

    function aoConfirmar() {
        confirmar({
            tipo: 'ENTRADA',
            quantidade: Number(quantidade),
            documento: documento || null,
            observacao: observacao || null,
        });
    }

    return (
        <form className={estilos.formulario} noValidate onSubmit={aoEnviar}>
            <ResumoProduto produto={produto} />
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
                {...modal}
                tipo="ENTRADA"
                quantidade={Number(quantidade)}
                produto={produto}
                aoConfirmar={aoConfirmar}
            />
        </form>
    )
}
