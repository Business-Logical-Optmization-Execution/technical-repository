// Importa o CSS Module como um objeto: estilos.pagina, estilos.secao...
import { useState } from 'react'
import estilos from './VitrinePage.module.css'
import { Botao, CampoTexto, useToast } from '@/shared/ui'
import { obterMensagemErro } from '@/shared/api'
import { useRequisicao } from '@/shared/lib'
import { useSessao } from '@/entities/sessao'
import {
  listarProdutos,
  calcularSituacao,
  ordenarPorCriticidade,
  filtrarProdutos,
  CardProdutoBaixo,
  CardProdutoResultado,
} from '@/entities/produto'
import { CampoBusca } from '@/features/buscar-produto'

// Página de testes. Cada exercício acrescenta uma <section>
// com o que precisa ser conferido no "Pronto quando".
export function VitrinePage() {

  const toast = useToast()
  const { usuario, ehAdmin, entrar, sair } = useSessao()
  const { dados: produtos, carregando, erro } = useRequisicao(listarProdutos)
  const [filtro, setFiltro] = useState('')

  const produtosVisiveis = produtos ? filtrarProdutos(ordenarPorCriticidade(produtos), filtro) : []

  function testarEntrada(email, senha) {
    entrar({ email, senha }).catch((e) => toast.erro(obterMensagemErro(e)))
  }

  return (
    <main className={estilos.pagina}>
      <header>
        <h1 className={estilos.titulo}>Vitrine</h1>
        <p className={estilos.descricao}>
          Página temporária para testar os exercícios da Etapa 2.
        </p>
      </header>

      <section className={estilos.secao} aria-labelledby="secao-sessao">
        <h2 id="secao-sessao" className={estilos.tituloSecao}>
          Sessão
        </h2>
        <div className={estilos.grupo}>
          <p>{usuario ? usuario.nome + ' · ' + (ehAdmin ? 'Admin' : 'Não é admin') : 'Não logado'}</p>
          <div className={estilos.linha}>
            <Botao variante="secundario" onClick={() => testarEntrada('marcia@ottimos.com', '123456')}>Entrar como Márcia</Botao>
            <Botao variante="secundario" onClick={() => testarEntrada('ronaldo@ottimos.com', '123456')}>Entrar como Ronaldo</Botao>
            <Botao variante="secundario" onClick={() => testarEntrada('marcia@ottimos.com', 'errada')}>Senha errada</Botao>
            <Botao variante="secundario" onClick={sair}>Sair</Botao>
          </div>
        </div>
      </section>
      <section className={estilos.secao} aria-labelledby="secao-produtos">
        <h2 id="secao-produtos" className={estilos.tituloSecao}>
          Produtos
        </h2>
        {carregando && <p>Carregando produtos...</p>}
        {erro && <p>{obterMensagemErro(erro)}</p>}
        {produtos && (
          <>
            <div className={estilos.grupo}>
              <CampoTexto
                rotulo="Filtrar produtos (teste)"
                placeholder="Ex.: parafuso 6"
                value={filtro}
                onChange={(e) => setFiltro(e.target.value)}
              />
              <ul>
                {produtosVisiveis.map((produto) => (
                  <li key={produto.id}>{produto.nome} · {calcularSituacao(produto)}</li>
                ))}
              </ul>
            </div>

            <div className={estilos.grupo}>
              <h3 className={estilos.tituloGrupo}>Card de baixo estoque (tela Início)</h3>
              <div className={estilos.grade3}>
                {produtosVisiveis.map((produto) => (
                  <CardProdutoBaixo key={produto.id} produto={produto} />
                ))}
              </div>
            </div>

            <div className={estilos.grupo}>
              <h3 className={estilos.tituloGrupo}>Card de resultado (tela Busca)</h3>
              <div className={estilos.grade2}>
                {produtosVisiveis.map((produto) => (
                  <CardProdutoResultado key={produto.id} produto={produto} />
                ))}
              </div>
            </div>

            {produtosVisiveis.length > 0 && (
              <div className={estilos.grupo}>
                <h3 className={estilos.tituloGrupo}>Sem foto (exibirFoto = false)</h3>
                <div className={estilos.grade2}>
                  <CardProdutoBaixo produto={produtosVisiveis[0]} exibirFoto={false} />
                  <CardProdutoResultado produto={produtosVisiveis[0]} exibirFoto={false} />
                </div>
              </div>
            )}
          </>
        )}
      </section>

      <section className={estilos.secao} aria-labelledby="secao-busca">
        <h2 id="secao-busca" className={estilos.tituloSecao}>
          Campo de busca
        </h2>
        <div className={estilos.grupo}>
          <h3 className={estilos.tituloGrupo}>Padrão (tela Início)</h3>
          <CampoBusca />
        </div>
        <div className={estilos.grupo}>
          <h3 className={estilos.tituloGrupo}>Com botão de lupa (tela Busca)</h3>
          <CampoBusca comBotao valorInicial="martelo" />
        </div>
      </section>
    </main>
  )
}
