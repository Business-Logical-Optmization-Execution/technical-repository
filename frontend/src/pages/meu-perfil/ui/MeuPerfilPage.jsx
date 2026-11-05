import { ROTAS } from '@/shared/config'
import { Card, CampoTexto, TituloPagina } from '@/shared/ui'
import { useSessao } from '@/entities/sessao'
import { FormAlterarSenha } from '@/features/alterar-senha'
import estilos from './MeuPerfilPage.module.css'

export function MeuPerfilPage() {

  const { usuario } = useSessao()

  return (
    <div className={estilos.pagina}>
      <TituloPagina
        titulo="Meu perfil"
        voltarPara={ROTAS.inicio}
      />
      <Card>
        <div className={estilos.dados}>
          <CampoTexto rotulo="Nome" caixaAlta={false} bloqueado value={usuario.nome} />
          <CampoTexto rotulo="E-mail" caixaAlta={false} bloqueado value={usuario.email} />
          <CampoTexto rotulo="Perfil" caixaAlta={false} bloqueado value={usuario.perfilAcesso.nome} />
        </div>
      </Card>
      <Card>
        <FormAlterarSenha />
      </Card>
    </div>
  )
}
