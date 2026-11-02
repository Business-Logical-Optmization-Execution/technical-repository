import { useTituloDocumento } from '@/shared/lib'
import { FormLogin } from '@/features/autenticar'
import { useSessao } from '@/entities/sessao'
import { Navigate, useLocation } from 'react-router-dom'
import { ROTAS } from '@/shared/config'
import estilos from './LoginPage.module.css'
import ilustracao from './ilustracao-login.webp'

export function LoginPage() {

  const { usuario } = useSessao()
  const location = useLocation()
  const de = location.state?.de

  useTituloDocumento('Login')

  if (usuario) {
    return <Navigate to={de ? de.pathname + de.search : ROTAS.inicio} replace />
  }

  return (
    <main className={estilos.pagina}>
      <div className={estilos.coluna}>
        <div className={estilos.conteudo}>
          <p className={estilos.marca}>PORTAL ÓTTIMOS</p>
          <h1 className={estilos.titulo}>Entrar</h1>
          <FormLogin />
        </div>
      </div>
      <div className={estilos.painel}>
        <img src={ilustracao} alt="" className={estilos.ilustracao} />
        <p className={estilos.sobreImagem + ' ' + estilos.credito}>designed by Drazen Zigic - Magnific.com</p>
        <p className={estilos.sobreImagem + ' ' + estilos.marca} aria-hidden="true">PORTAL ÓTTIMOS</p>
      </div>
    </main>
  )
}
