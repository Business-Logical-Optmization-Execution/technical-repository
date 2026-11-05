import { useState } from 'react'
import { obterErrosDeCampo, obterMensagemErro } from '@/shared/api'
import { Botao, CampoSenha, Faixa, useToast } from '@/shared/ui'
import { useSessao } from '@/entities/sessao'
import { useAlterarSenha } from '../model/useAlterarSenha'
import estilos from './FormAlterarSenha.module.css'

const MINIMO_SENHA = 8;
const MAXIMO_SENHA = 150;
const CAMPOS_VAZIOS = { senhaAtual: '', novaSenha: '', repetirSenha: '' };

function validar({ senhaAtual, novaSenha, repetirSenha }) {

    const erros = {};

    if (!senhaAtual) {
        erros.senhaAtual = 'Informe a senha atual.';
    }

    if (!novaSenha.trim()) {
        erros.novaSenha = 'Informe a nova senha.';
    } else if (novaSenha.length < MINIMO_SENHA || novaSenha.length > MAXIMO_SENHA) {
        erros.novaSenha = 'Use de ' + MINIMO_SENHA + ' a ' + MAXIMO_SENHA + ' caracteres.';
    }

    if (repetirSenha !== novaSenha) {
        erros.repetirSenha = 'As senhas não são iguais.';
    }

    return erros;
}

export function FormAlterarSenha() {

    const { usuario } = useSessao()
    const toast = useToast()
    const { enviar, enviando, erro: erroEnvio } = useAlterarSenha()
    const [campos, setCampos] = useState(CAMPOS_VAZIOS)
    const [erros, setErros] = useState({})
    const [erroGeral, setErroGeral] = useState(false)

    function alterar(campo, valor) {
        setCampos({ ...campos, [campo]: valor })
        setErros({ ...erros, [campo]: undefined })
    }

    async function aoEnviar(evento) {

        evento.preventDefault()

        const encontrados = validar(campos)
        setErros(encontrados)
        setErroGeral(false)

        if (Object.keys(encontrados).length > 0) {
            return
        }

        try {
            await enviar(usuario.id, { senhaAtual: campos.senhaAtual, novaSenha: campos.novaSenha })
            setCampos(CAMPOS_VAZIOS)
            toast.sucesso('Senha alterada.')
        } catch (e) {
            const errosDoBack = obterErrosDeCampo(e)
            setErros(errosDoBack)
            setErroGeral(Object.keys(errosDoBack).length === 0)
        }
    }

    return (
        <form className={estilos.formulario} noValidate onSubmit={aoEnviar}>
            <CampoSenha
                rotulo="Senha atual"
                caixaAlta={false}
                autoComplete="current-password"
                erro={erros.senhaAtual}
                value={campos.senhaAtual}
                onChange={(evento) => alterar('senhaAtual', evento.target.value)}
            />
            <CampoSenha
                rotulo="Nova senha"
                caixaAlta={false}
                autoComplete="new-password"
                ajuda="Mínimo de 8 caracteres"
                erro={erros.novaSenha}
                value={campos.novaSenha}
                onChange={(evento) => alterar('novaSenha', evento.target.value)}
            />
            <CampoSenha
                rotulo="Repetir nova senha"
                caixaAlta={false}
                autoComplete="new-password"
                erro={erros.repetirSenha}
                value={campos.repetirSenha}
                onChange={(evento) => alterar('repetirSenha', evento.target.value)}
            />
            {erroGeral && (
                <Faixa variante="alerta" role="alert">
                    <p>{obterMensagemErro(erroEnvio)}</p>
                </Faixa>
            )}
            <div className={estilos.acoes}>
                <Botao type="submit" carregando={enviando} textoCarregando="Alterando...">Alterar senha</Botao>
            </div>
        </form>
    )
}
