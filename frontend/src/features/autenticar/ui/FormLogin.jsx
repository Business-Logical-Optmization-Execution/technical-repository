import {useState} from 'react'
import { CampoTexto, CampoSenha, Botao } from '@/shared/ui'
import { ArrowRight, TriangleAlert } from 'lucide-react'
import { useSessao } from '@/entities/sessao'
import { obterMensagemErro } from '@/shared/api'

import estilos from './FormLogin.module.css'
export function FormLogin() {
    
    const [email, setEmail] = useState('')
    const [senha, setSenha] = useState('')
    const [enviando, setEnviando] = useState(false)
    const [erro, setErro] = useState(null)

    
    const { entrar } = useSessao()

    async function aoEnviar(evento) {
        evento.preventDefault()

        setEnviando(true)
        setErro(null)
        
        try {
            await entrar({ email, senha })
        } catch (e) {
            setErro(obterMensagemErro(e))
        } finally {
            setEnviando(false)
        }
    }


    return (
        <form className={estilos.formulario} noValidate onSubmit={aoEnviar}>
            <CampoTexto rotulo='E-mail' tipo='email' placeholder='Seu email' value={email} onChange={e => setEmail(e.target.value)} />
            <CampoSenha rotulo='Senha' value={senha} onChange={e => setSenha(e.target.value)} />
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