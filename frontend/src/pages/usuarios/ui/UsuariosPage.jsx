import { useState } from 'react'
import { Users } from 'lucide-react'
import { obterMensagemErro } from '@/shared/api'
import { ROTAS } from '@/shared/config'
import { useRequisicao } from '@/shared/lib'
import { Botao, EstadoVazio, Skeleton, Tabela, TituloPagina } from '@/shared/ui'
import { BotaoDesativarUsuario, PainelUsuario } from '@/features/gerenciar-usuario'
import { PERFIS } from '@/entities/sessao'
import { listarUsuarios } from '@/entities/usuario'
import estilos from './UsuariosPage.module.css'

const COLUNAS = [
  { titulo: 'Nome', valor: (usuario) => usuario.nome },
  { titulo: 'E-mail', valor: (usuario) => usuario.email },
  { titulo: 'Perfil', valor: (usuario) => usuario.perfilAcesso.nome },
  { titulo: 'Situação', valor: (usuario) => (usuario.ativo ? 'Ativo' : 'Desativado') },
]

function buscarUsuarios() {
  return listarUsuarios({ incluirInativos: true })
}

function ehAdminAtivo(usuario) {
  return usuario.ativo && usuario.perfilAcesso.nome === PERFIS.ADMIN
}

export function UsuariosPage() {

  const { dados: usuarios, carregando, erro, recarregar } = useRequisicao(buscarUsuarios)

  const [painel, setPainel] = useState({ aberto: false, usuario: null })

  const perfilBloqueado = Boolean(painel.usuario)
    && ehAdminAtivo(painel.usuario)
    && usuarios.filter(ehAdminAtivo).length <= 1

  const colunas = [
    ...COLUNAS,
    {
      titulo: 'Ações',
      valor: (usuario) => usuario.ativo && (
        <div className={estilos.acoes}>
          <Botao variante="secundario" onClick={() => setPainel({ aberto: true, usuario })}>Editar</Botao>
          <BotaoDesativarUsuario usuario={usuario} aoDesativar={recarregar} />
        </div>
      ),
    },
  ]

  function conteudo() {
    if (carregando && !usuarios) {
      return (
        <div aria-busy="true">
          <p className="visualmente-oculto">Carregando usuários...</p>
          <Skeleton altura="136px" />
        </div>
      )
    }

    if (erro) {
      return (
        <div className={estilos.erro}>
          <p>{obterMensagemErro(erro)}</p>
          <Botao variante="secundario" onClick={recarregar}>Tentar de novo</Botao>
        </div>
      )
    }

    if (usuarios.length === 0) {
      return <EstadoVazio icone={Users} texto="Nenhum usuário cadastrado." />
    }

    return <Tabela legenda="Usuários do sistema" colunas={colunas} linhas={usuarios} />
  }

  return (
    <div className={estilos.pagina}>
      <TituloPagina
        titulo="Usuários"
        admin
        voltarPara={ROTAS.inicio}
        acoes={<Botao onClick={() => setPainel({ aberto: true, usuario: null })}>Cadastrar usuário</Botao>}
      />
      {conteudo()}
      <PainelUsuario
        aberto={painel.aberto}
        usuario={painel.usuario}
        perfilBloqueado={perfilBloqueado}
        aoFechar={() => setPainel({ aberto: false, usuario: null })}
        aoSalvar={recarregar}
      />
    </div>
  )

}
