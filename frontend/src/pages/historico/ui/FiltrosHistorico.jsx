import { nomeProduto, useRequisicao } from '@/shared/lib'
import { Botao } from '@/shared/ui'
import { listarProdutos } from '@/entities/produto'
import { listarUsuarios } from '@/entities/usuario'
import estilos from './FiltrosHistorico.module.css'

const OPCOES_PERIODO = [
  { valor: '', rotulo: 'Todo o período' },
  { valor: 'hoje', rotulo: 'Hoje' },
  { valor: '7', rotulo: 'Últimos 7 dias' },
  { valor: '30', rotulo: 'Últimos 30 dias' },
];

const OPCOES_TIPO = [
  { valor: '', rotulo: 'Todos os tipos' },
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

  const opcoesProduto = [
    { valor: '', rotulo: 'Todos os produtos' },
    ...produtos.map((produto) => ({ valor: String(produto.id), rotulo: nomeProduto(produto) })),
  ]
  const opcoesUsuario = [
    { valor: '', rotulo: 'Todos os usuários' },
    ...usuarios.map((usuario) => ({ valor: String(usuario.id), rotulo: usuario.nome })),
  ]

  function filtro(rotulo, chave, opcoes, desabilitado) {

    const valor = valores[chave] ?? ''

    return (
      <select
        aria-label={rotulo}
        className={estilos.filtro + (valor ? ' ' + estilos.ativo : '')}
        disabled={desabilitado}
        value={valor}
        onChange={(evento) => aoMudar(chave, evento.target.value)}
      >
        {opcoes.map((opcao) => <option key={opcao.valor} value={opcao.valor}>{opcao.rotulo}</option>)}
      </select>
    )
  }

  return (
    <div className={estilos.filtros}>
      <div className={estilos.campos}>
        {filtro('Produto', 'produtoId', opcoesProduto, carregando)}
        {filtro('Usuário', 'usuarioId', opcoesUsuario, carregando)}
        {filtro('Período', 'periodo', OPCOES_PERIODO)}
        {filtro('Tipo', 'tipo', OPCOES_TIPO)}
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
