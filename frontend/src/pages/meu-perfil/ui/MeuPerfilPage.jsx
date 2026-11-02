import { ROTAS } from '@/shared/config'
import { TituloPagina } from '@/shared/ui'

export function MeuPerfilPage() {
  return (
    <TituloPagina
      titulo="Meu perfil"
      voltarPara={ROTAS.inicio}
    />
  )
}
