import { useState } from 'react'
import { Gaveta } from '@/shared/ui'
import { FormUsuario } from './FormUsuario'

export function PainelUsuario({ aberto, usuario, perfilBloqueado, aoFechar, aoSalvar }) {

  const [travado, setTravado] = useState(false)

  function fechar() {
    if (!travado) {
      aoFechar()
    }
  }

  function concluir() {
    setTravado(false)
    aoFechar()
  }

  return (
    <Gaveta aberto={aberto} aoFechar={fechar} titulo={usuario ? 'Editar' : 'Cadastrar usuário'} lado="direita">
      {aberto && (
        <FormUsuario
          usuario={usuario}
          perfilBloqueado={perfilBloqueado}
          aoSalvar={aoSalvar}
          aoFechar={concluir}
          aoTravar={setTravado}
        />
      )}
    </Gaveta>
  )
}
