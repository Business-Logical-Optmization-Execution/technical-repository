import { ROTAS } from '@/shared/config'
import { Botao, TituloPagina } from '@/shared/ui'

export function UsuariosPage() {
  return (
    <TituloPagina
      titulo="Usuários" 
      admin 
      voltarPara={ROTAS.inicio} 
      acoes={<Botao>Cadastrar usuário</Botao>}
    />
  )
}
