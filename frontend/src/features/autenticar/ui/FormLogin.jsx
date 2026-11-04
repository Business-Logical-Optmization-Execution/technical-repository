import {useState} from 'react'
import { CampoTexto, CampoSenha, Botao } from '@/shared/ui'
import { ArrowRight, TriangleAlert } from 'lucide-react'
import { useSessao } from '@/entities/sessao'
import { obterErrosDeCampo, obterMensagemErro } from '@/shared/api'

import estilos from './FormLogin.module.css'

function validar({ email, senha }) {

    const erros = {}

    if (!email) {
        erros.email = 'Informe o e-mail.'
    } else if (!/^\S+@\S+$/.test(email)) {
        erros.email = 'Informe um e-mail válido.'
    }
    if (!senha) {
        erros.senha = 'Informe a senha.'
    }

    return erros
}

export function FormLogin() {

    const [email, setEmail] = useState('')
    const [senha, setSenha] = useState('')
    const [enviando, setEnviando] = useState(false)
    const [erro, setErro] = useState(null)
    const [errosDeCampo, setErrosDeCampo] = useState({})


    const { entrar } = useSessao()

    async function aoEnviar(evento) {
        evento.preventDefault()

        const emailLimpo = email.trim()
        const encontrados = validar({ email: emailLimpo, senha })

        setErro(null)
        setErrosDeCampo(encontrados)

        if (Object.keys(encontrados).length > 0) {
            return
        }

        setEnviando(true)

        try {
            await entrar({ email: emailLimpo, senha })
        } catch (e) {
            const doBack = obterErrosDeCampo(e)
            if (Object.keys(doBack).length > 0) {
                setErrosDeCampo(doBack)
            } else {
                setErro(obterMensagemErro(e))
            }
        } finally {
            setEnviando(false)
        }
    }


    return (
        <form className={estilos.formulario} noValidate onSubmit={aoEnviar}>
            <CampoTexto rotulo='E-mail' tipo='email' placeholder='Seu email' erro={errosDeCampo.email} value={email} onChange={e => setEmail(e.target.value)} />
            <CampoSenha rotulo='Senha' erro={errosDeCampo.senha} value={senha} onChange={e => setSenha(e.target.value)} />
            {erro && <p className={estilos.erro} role="alert"><TriangleAlert size={16} aria-hidden="true" />{erro}</p>}
            <div className={estilos.acao}>
                <Botao type='submit' carregando={enviando} textoCarregando='Entrando...'>
                    Entrar <ArrowRight aria-hidden="true" size={16} />
                </Botao>
            </div>
            <p className={estilos.ajuda}>Esqueceu a senha? Peça a um administrador para redefinir.</p>
        </form>
    )
}