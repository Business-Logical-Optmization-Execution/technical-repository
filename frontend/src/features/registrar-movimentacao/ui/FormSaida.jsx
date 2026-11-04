import { useState } from 'react'
import { Link, useLocation } from 'react-router-dom'
import { ROTAS } from '@/shared/config'
import { obterErrosDeCampo, obterMensagemErro } from '@/shared/api'
import { formatarQuantidade } from '@/shared/lib'
import { Botao, CampoAreaTexto, CampoTexto, GrupoOpcoes } from '@/shared/ui'
import { MOTIVOS_SAIDA } from '@/entities/movimentacao'
import { useConfirmacaoMovimentacao } from '../model/useConfirmacaoMovimentacao'
import { ModalConfirmarMovimentacao } from './ModalConfirmarMovimentacao'
import { ResumoProduto } from './ResumoProduto'
import estilos from './FormMovimentacao.module.css'

const MAXIMO_OBSERVACAO = 255;

function validar({ quantidade, motivo, observacao }) {

    const erros = {};

    if (!/^\d+$/.test(quantidade) || Number(quantidade) === 0) {
        erros.quantidade = 'Informe um número inteiro maior que zero.';
    }
    if (!motivo) {
        erros.motivo = 'Escolha o motivo da saída.';
    }
    if (motivo === 'OUTRO' && !observacao) {
        erros.observacao = 'Descreva o motivo.';
    }
    if (observacao.length > MAXIMO_OBSERVACAO) {
        erros.observacao = 'Use no máximo ' + MAXIMO_OBSERVACAO + ' caracteres.';
    }

    return erros;
}

// Se o saldo mudou depois que a tela abriu, o 422 do back vai para o campo Quantidade.
function errosDaResposta(erro) {

    if (erro.response?.status === 422) {
        return { quantidade: obterMensagemErro(erro) };
    }

    return obterErrosDeCampo(erro);
}

export function FormSaida({ produto, aoConcluir, aoCancelar }) {

    const location = useLocation();
    const [campos, setCampos] = useState({ quantidade: '', motivo: '', observacao: '' });
    const [erros, setErros] = useState({});
    const { abrir, confirmar, modal } = useConfirmacaoMovimentacao({
        produto,
        aoConcluir,
        aoErrarCampos: setErros,
        errosDeCampo: errosDaResposta,
    });

    const quantidade = campos.quantidade.trim();
    const observacao = campos.motivo === 'OUTRO' ? campos.observacao.trim() : '';
    const acimaDoSaldo = /^\d+$/.test(quantidade) && Number(quantidade) > produto.quantidade;

    const erroQuantidade = acimaDoSaldo
        ? 'Não dá para tirar ' + formatarQuantidade(Number(quantidade)) + ': a quantidade atual é '
            + formatarQuantidade(produto.quantidade) + ' Se o número na prateleira for outro, faça um ajuste por contagem.'
        : erros.quantidade;

    function alterar(campo, valor) {
        setCampos({ ...campos, [campo]: valor });
        setErros({ ...erros, [campo]: undefined });
    }

    function aoEnviar(evento) {

        evento.preventDefault();

        const encontrados = validar({ quantidade, motivo: campos.motivo, observacao });
        setErros(encontrados);

        if (!acimaDoSaldo && Object.keys(encontrados).length === 0) {
            abrir();
        }
    }

    function aoConfirmar() {
        confirmar({
            tipo: 'SAIDA',
            quantidade: Number(quantidade),
            motivo: campos.motivo,
            observacao: observacao || null,
        });
    }

    return (
        <form className={estilos.formulario} noValidate onSubmit={aoEnviar}>
            <ResumoProduto produto={produto} />
            <div className={estilos.campoComLink}>
                <CampoTexto
                    rotulo="Quantidade"
                    obrigatorio
                    caixaAlta={false}
                    inputMode="numeric"
                    erro={erroQuantidade}
                    value={campos.quantidade}
                    onChange={(evento) => alterar('quantidade', evento.target.value)}
                />
                {acimaDoSaldo && (
                    <Link to={ROTAS.ajustarContagem(produto.id)} state={location.state} className={estilos.link}>
                        Ajustar por contagem
                    </Link>
                )}
            </div>
            <GrupoOpcoes
                legenda="Motivo"
                obrigatorio
                opcoes={MOTIVOS_SAIDA}
                valor={campos.motivo}
                aoMudar={(motivo) => alterar('motivo', motivo)}
                erro={erros.motivo}
            />
            {campos.motivo === 'OUTRO' && (
                <CampoAreaTexto
                    rotulo="Descreva o motivo"
                    obrigatorio
                    caixaAlta={false}
                    erro={erros.observacao}
                    value={campos.observacao}
                    onChange={(evento) => alterar('observacao', evento.target.value)}
                />
            )}
            <div className={estilos.acoes}>
                <Botao variante="secundario" onClick={aoCancelar}>Cancelar</Botao>
                <Botao type="submit" disabled={acimaDoSaldo}>Registrar saída</Botao>
            </div>
            <ModalConfirmarMovimentacao
                {...modal}
                tipo="SAIDA"
                quantidade={Number(quantidade)}
                produto={produto}
                aoConfirmar={aoConfirmar}
            />
        </form>
    )
}
