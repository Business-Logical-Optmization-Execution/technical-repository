import { useState } from 'react'
import { obterMensagemErro } from '@/shared/api'
import { Botao, Modal, useToast } from '@/shared/ui'
import { desativarUsuario } from '@/entities/usuario'

export function BotaoDesativarUsuario({ usuario, aoDesativar }) {

  const toast = useToast()
  const [aberto, setAberto] = useState(false)
  const [enviando, setEnviando] = useState(false)

  function fechar() {
    if (!enviando) {
      setAberto(false)
    }
  }

  async function confirmar() {

    setEnviando(true)

    try {
      await desativarUsuario(usuario.id)
      toast.sucesso('Acesso de ' + usuario.nome + ' desativado.')
      aoDesativar()
    } catch (e) {
      toast.erro(obterMensagemErro(e))
    } finally {
      setEnviando(false)
      setAberto(false)
    }
  }

  return (
    <>
      <Botao variante="destrutivo" onClick={() => setAberto(true)}>Desativar</Botao>
      <Modal
        aberto={aberto}
        aoFechar={fechar}
        titulo={'Desativar ' + usuario.nome + '?'}
        acoes={
          <>
            <Botao variante="secundario" disabled={enviando} onClick={fechar}>Cancelar</Botao>
            <Botao variante="destrutivo" carregando={enviando} textoCarregando="Desativando..." onClick={confirmar}>Desativar</Botao>
          </>
        }
      >
        <p>Essa pessoa não consegue mais entrar. O nome continua no histórico.</p>
      </Modal>
    </>
  )
}
