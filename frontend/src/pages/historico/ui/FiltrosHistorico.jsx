import { nomeProduto, useRequisicao } from '@/shared/lib'
import { Botao, CampoSelecao } from '@/shared/ui'
import { listarProdutos } from '@/entities/produto'
import { listarUsuarios } from '@/entities/usuario'
import estilos from './FiltrosHistorico.module.css'

const TODOS = { valor: '', rotulo: 'Todos' };

const OPCOES_PERIODO = [
  { valor: '', rotulo: 'Tudo' },
  { valor: 'hoje', rotulo: 'Hoje' },
  { valor: '7', rotulo: 'Últimos 7 dias' },
  { valor: '30', rotulo: 'Últimos 30 dias' },
];

const OPCOES_TIPO = [
  TODOS,
  { valor: 'ENTRADA', rotulo: 'Entrada' },
  { valor: 'SAIDA', rotulo: 'Saída' },
  { valor: 'AJUSTE', rotulo: 'Ajuste' },
];

// O histórico também mostra quem foi desativado e o que foi inativado.
function buscarOpcoes() {
  return Promise.all([listarProdutos({ incluirInativos: true }), listarUsuarios({ incluirInativos: true })])
}

export function FiltrosHistorico({ valores, aoMudar }) {

  const { dados, carregando, erro, recarregar } = useRequisicao(buscarOpcoes)
  const [produtos, usuarios] = dados ?? [[], []]

  const opcoesProduto = [TODOS, ...produtos.map((produto) => ({ valor: String(produto.id), rotulo: nomeProduto(produto) }))]
  const opcoesUsuario = [TODOS, ...usuarios.map((usuario) => ({ valor: String(usuario.id), rotulo: usuario.nome }))]

  function campo(rotulo, chave, opcoes, desabilitado) {
    return (
      <CampoSelecao
        rotulo={rotulo}
        opcoes={opcoes}
        disabled={desabilitado}
        value={valores[chave] ?? ''}
        onChange={(evento) => aoMudar(chave, evento.target.value)}
      />
    )
  }

  return (
    <div className={estilos.filtros}>
      <div className={estilos.campos}>
        {campo('Produto', 'produtoId', opcoesProduto, carregando)}
        {campo('Usuário', 'usuarioId', opcoesUsuario, carregando)}
        {campo('Período', 'periodo', OPCOES_PERIODO)}
        {campo('Tipo', 'tipo', OPCOES_TIPO)}
      </div>
      {erro && (
        <div className={estilos.erro}>
          <p>Não foi possível carregar os produtos e usuários dos filtros.</p>
          <Botao variante="secundario" onClick={recarregar}>Tentar de novo</Botao>
        </div>
      )}
    </div>
  )
}
