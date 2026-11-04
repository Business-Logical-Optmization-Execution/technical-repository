import { useState } from 'react'
import { formatarQuantidade } from '@/shared/lib'
import { Botao, CampoAreaTexto, CampoTexto } from '@/shared/ui'
import { useConfirmacaoMovimentacao } from '../model/useConfirmacaoMovimentacao'
import { ModalConfirmarMovimentacao } from './ModalConfirmarMovimentacao'
import { ResumoProduto } from './ResumoProduto'
import estilos from './FormMovimentacao.module.css'

const MINIMO_JUSTIFICATIVA = 10;
const MAXIMO_JUSTIFICATIVA = 255;

function validar({ contada, justificativa }) {

    const erros = {};

    if (!/^\d+$/.test(contada)) {
        erros.quantidade = 'Informe um número inteiro igual ou maior que zero.';
    }
    if (justificativa.length < MINIMO_JUSTIFICATIVA) {
        erros.observacao = 'Escreva uma justificativa com pelo menos ' + MINIMO_JUSTIFICATIVA + ' caracteres.';
    } else if (justificativa.length > MAXIMO_JUSTIFICATIVA) {
        erros.observacao = 'Use no máximo ' + MAXIMO_JUSTIFICATIVA + ' caracteres.';
    }

    return erros;
}

function Diferenca({ valor }) {

    if (valor === 0) {
        return <p className={estilos.diferenca}>Sem diferença</p>
    }

    const tom = valor > 0 ? estilos.positiva : estilos.negativa;

    return (
        <p className={estilos.diferenca + ' ' + tom}>
            Diferença: {formatarQuantidade(valor, { sinal: true })}
        </p>
    )
}

export function FormAjuste({ produto, aoConcluir, aoCancelar }) {

    const [campos, setCampos] = useState({ quantidade: '', observacao: '' });
    const [erros, setErros] = useState({});
    const { abrir, confirmar, modal } = useConfirmacaoMovimentacao({ produto, aoConcluir, aoErrarCampos: setErros });

    const contada = campos.quantidade.trim();
    const justificativa = campos.observacao.trim();
    const contagemValida = /^\d+$/.test(contada);

    function alterar(campo) {
        return (evento) => {
            setCampos({ ...campos, [campo]: evento.target.value });
            setErros({ ...erros, [campo]: undefined });
        };
    }

    function aoEnviar(evento) {

        evento.preventDefault();

        const encontrados = validar({ contada, justificativa });
        setErros(encontrados);

        if (Object.keys(encontrados).length === 0) {
            abrir();
        }
    }

    function aoConfirmar() {
        confirmar({ tipo: 'AJUSTE', quantidade: Number(contada), observacao: justificativa });
    }

    return (
        <form className={estilos.formulario} noValidate onSubmit={aoEnviar}>
            <ResumoProduto produto={produto} mostrarQuantidade={false} />
            <p className={estilos.noSistema + (produto.quantidade < 0 ? ' ' + estilos.negativa : '')}>
                Quantidade no sistema: {formatarQuantidade(produto.quantidade)}
            </p>
            <CampoTexto
                rotulo="Quantidade contada na prateleira"
                obrigatorio
                caixaAlta={false}
                inputMode="numeric"
                erro={erros.quantidade}
                value={campos.quantidade}
                onChange={alterar('quantidade')}
            />
            <div aria-live="polite">
                {contagemValida && <Diferenca valor={Number(contada) - produto.quantidade} />}
            </div>
            <CampoAreaTexto
                rotulo="Justificativa"
                obrigatorio
                caixaAlta={false}
                placeholder="Ex.: contagem da prateleira C3-P2 na quarta-feira"
                erro={erros.observacao}
                value={campos.observacao}
                onChange={alterar('observacao')}
            />
            <div className={estilos.acoes}>
                <Botao variante="secundario" onClick={aoCancelar}>Cancelar</Botao>
                <Botao type="submit">Registrar ajuste</Botao>
            </div>
            <ModalConfirmarMovimentacao
                {...modal}
                tipo="AJUSTE"
                quantidade={Number(contada)}
                produto={produto}
                aoConfirmar={aoConfirmar}
            />
        </form>
    )
}
