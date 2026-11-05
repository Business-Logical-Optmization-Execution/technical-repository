import { useState } from 'react'
import { obterErrosDeCampo, obterMensagemErro } from '@/shared/api'
import { useRequisicao } from '@/shared/lib'
import { Botao, CampoTexto, Faixa, GrupoOpcoes, Skeleton, useToast } from '@/shared/ui'
import { listarPerfis } from '@/entities/usuario'
import { gerarSenhaProvisoria } from '../lib/gerarSenhaProvisoria'
import { useSalvarUsuario } from '../model/useSalvarUsuario'
import estilos from './PainelUsuario.module.css'

const MAXIMO_NOME = 120;
const MAXIMO_EMAIL = 180;
const FORMATO_EMAIL = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

function validar({ nome, email, perfilAcessoId }) {

    const erros = {};

    if (!nome) {
        erros.nome = 'Informe o nome.';
    } else if (nome.length > MAXIMO_NOME) {
        erros.nome = 'Use no máximo ' + MAXIMO_NOME + ' caracteres.';
    }

    if (!FORMATO_EMAIL.test(email)) {
        erros.email = 'Informe um e-mail válido.';
    } else if (email.length > MAXIMO_EMAIL) {
        erros.email = 'Use no máximo ' + MAXIMO_EMAIL + ' caracteres.';
    }

    if (!perfilAcessoId) {
        erros.perfilAcessoId = 'Escolha um perfil.';
    }

    return erros;
}

export function FormUsuario({ usuario, perfilBloqueado, aoSalvar, aoFechar, aoTravar }) {

    const toast = useToast()
    const { dados: perfis, carregando, erro: erroPerfis, recarregar } = useRequisicao(listarPerfis)
    const [campos, setCampos] = useState({
        nome: usuario?.nome ?? '',
        email: usuario?.email ?? '',
        perfilAcessoId: usuario?.perfilAcesso.id ?? null,
    })
    const [erros, setErros] = useState({})
    const { salvar, enviando, erro: erroEnvio } = useSalvarUsuario()
    const [senhaProvisoria, setSenhaProvisoria] = useState(null)

    const nome = campos.nome.trim()
    const email = campos.email.trim()
    const opcoesPerfil = (perfis ?? []).map((perfil) => ({ valor: perfil.id, rotulo: perfil.nome }))

    function alterar(campo, valor) {
        setCampos({ ...campos, [campo]: valor })
        setErros({ ...erros, [campo]: undefined })
    }

    async function aoEnviar(evento) {

        evento.preventDefault()

        const perfilAcessoId = campos.perfilAcessoId
        const encontrados = validar({ nome, email, perfilAcessoId })
        setErros(encontrados)

        if (Object.keys(encontrados).length > 0) {
            return
        }

        aoTravar(true)

        try {
            if (usuario) {
                await salvar({ nome, email, perfilAcessoId }, usuario.id)
                toast.sucesso('Usuário atualizado.')
                aoSalvar()
                aoFechar()
            } else {
                const senha = gerarSenhaProvisoria()
                await salvar({ nome, email, senha, perfilAcessoId })
                setSenhaProvisoria(senha)
                aoSalvar()
            }
        } catch (e) {
            aoTravar(false)

            if (e.response?.status === 409) {
                setErros({ email: 'Já existe um usuário com este e-mail.' })
            } else {
                setErros(obterErrosDeCampo(e))
            }
        }
    }

    function erroEhGeral() {
        return erroEnvio
            && erroEnvio.response?.status !== 409
            && Object.keys(obterErrosDeCampo(erroEnvio)).length === 0
    }

    function campoPerfil() {

        if (perfilBloqueado) {
            return (
                <CampoTexto
                    rotulo="Perfil"
                    caixaAlta={false}
                    bloqueado
                    motivoBloqueio="Este é o último administrador ativo."
                    value={usuario.perfilAcesso.nome}
                />
            )
        }

        if (carregando) {
            return <Skeleton altura="96px" />
        }

        if (erroPerfis) {
            return (
                <div className={estilos.erro}>
                    <p>Não foi possível carregar os perfis.</p>
                    <Botao variante="secundario" onClick={recarregar}>Tentar de novo</Botao>
                </div>
            )
        }

        return (
            <GrupoOpcoes
                legenda="Perfil"
                opcoes={opcoesPerfil}
                valor={campos.perfilAcessoId}
                aoMudar={(valor) => alterar('perfilAcessoId', valor)}
                erro={erros.perfilAcessoId}
            />
        )
    }

    if (senhaProvisoria) {
        return (
            <div className={estilos.formulario}>
                <Faixa role="status">
                    <p>Usuário cadastrado. Senha provisória: <strong>{senhaProvisoria}</strong>. Anote e entregue ao funcionário.</p>
                </Faixa>
                <div className={estilos.acoes}>
                    <Botao onClick={aoFechar}>Fechar</Botao>
                </div>
            </div>
        )
    }

    return (
        <form className={estilos.formulario} noValidate onSubmit={aoEnviar}>
            <CampoTexto
                rotulo="Nome"
                caixaAlta={false}
                erro={erros.nome}
                value={campos.nome}
                onChange={(evento) => alterar('nome', evento.target.value)}
            />
            <CampoTexto
                rotulo="E-mail"
                tipo="email"
                autoComplete="off"
                caixaAlta={false}
                erro={erros.email}
                value={campos.email}
                onChange={(evento) => alterar('email', evento.target.value)}
            />
            {campoPerfil()}
            {erroEhGeral() && (
                <Faixa variante="alerta" role="alert">
                    <p>{obterMensagemErro(erroEnvio)}</p>
                </Faixa>
            )}
            <div className={estilos.acoes}>
                <Botao type="submit" carregando={enviando} textoCarregando="Salvando...">Salvar</Botao>
            </div>
        </form>
    )
}

