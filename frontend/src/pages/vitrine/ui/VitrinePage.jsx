// Importa o CSS Module como um objeto: estilos.pagina, estilos.secao...
import estilos from './VitrinePage.module.css'
import { Faixa, EstadoVazio, Skeleton, Card, Selo, Botao, BotaoIcone, CampoTexto, CampoAreaTexto, CampoSenha, Modal, Gaveta, useToast } from '@/shared/ui'
import { PackageSearch, TriangleAlert, Check, ArrowLeft, Pencil, Search, X } from 'lucide-react'
import { formatarQuantidade, formatarMoeda, formatarDataHora, formatarDataRelativa, normalizarTexto, nomeProduto } from '@/shared/lib'

import { http, obterMensagemErro } from '@/shared/api'
import { useRequisicao } from '@/shared/lib'

import { useState } from 'react'
import { Link } from 'react-router-dom'

// Página de testes da Etapa 1. Cada exercício acrescenta uma <section>
// com o componente novo em todos os estados pedidos no "Pronto quando".
// Datas de exemplo para formatarDataRelativa (fora do componente: new Date() não é puro).
const agora = new Date()
const ontem = new Date(agora)
ontem.setDate(agora.getDate() - 1)

export function VitrinePage() {

  const [valorCampo, setValorCampo] = useState('')
  const [valorCampo2, setValorCampo2] = useState('')
  const [modalAberto, setModalAberto] = useState(false)
  const [gavetaAberta, setGavetaAberta] = useState(false)
  const [painelAberto, setPainelAberto] = useState(false)
  const toast = useToast()
  const { dados, carregando, erro, recarregar } = useRequisicao(() => http.get('/produtos').then((r) => r.data));

  return (
    <main className={estilos.pagina}>
      <header>
        <h1 className={estilos.titulo}>Vitrine</h1>
        <p className={estilos.descricao}>
          Página temporária para testar os componentes de shared/ui em todos os estados.
        </p>
        {<Botao disabled variante="link">Ver histórico completo</Botao>}
      </header>

      {/* Exercício 1.2 — importe com: import { Botao, BotaoIcone } from '@/shared/ui' */}
      <section className={estilos.secao} aria-labelledby="secao-botao">
        <h2 id="secao-botao" className={estilos.tituloSecao}>
          Botão
        </h2>

        <div className={estilos.grupo}>
          <h3 className={estilos.tituloGrupo}>Variantes</h3>
          <div className={estilos.linha}>
            {<Botao onClick={() => console.log('Clicou!')}>Registrar entrada</Botao>}
            {<Botao variante="secundario" onClick={() => console.log('Clicou!')}>Cancelar</Botao>}
            {<Botao variante="destrutivo" onClick={() => console.log('Clicou!')}>Inativar produto</Botao>}
            {<Botao variante="link" onClick={() => console.log('Clicou!')}>Ver histórico completo</Botao>}
          </div>
        </div>

        <div className={estilos.grupo}>
          <h3 className={estilos.tituloGrupo}>Desabilitado</h3>
          <div className={estilos.linha}>
            {<Botao disabled variante="primario">Registrar entrada</Botao>}
            {<Botao disabled variante="secundario">Cancelar</Botao>}
            {<Botao disabled variante="destrutivo">Inativar produto</Botao>}
            {<Botao disabled variante="link">Ver histórico completo</Botao>}
          </div>
        </div>

        <div className={estilos.grupo}>
          <h3 className={estilos.tituloGrupo}>Com ícone e carregando</h3>
          <div className={estilos.linha}>
            {<Botao icone={Pencil} carregando textoCarregando="Editando..." variante="secundario" onClick={() => console.log('Clicou!')}>Entrar</Botao>}
            {<Botao icone={Pencil} carregando textoCarregando="Entrando..." variante="secundario" onClick={() => console.log('Clicou!')}>Entrar</Botao>}
            {<Botao icone={Pencil} larguraTotal={true} variante="secundario" onClick={() => console.log('Clicou!')}>Editar</Botao>}
          </div>
        </div>

        <div className={estilos.grupo}>
          <h3 className={estilos.tituloGrupo}>Só ícone (BotaoIcone)</h3>
          <div className={estilos.linha}>
            <BotaoIcone icone={ArrowLeft} rotulo="Voltar" />
            <BotaoIcone icone={X} rotulo="Fechar" />
            <BotaoIcone icone={Search} rotulo="Buscar" />
            {/* Sem rotulo de propósito: deve gerar o aviso no Console (Pronto quando). */}
            <BotaoIcone icone={Search} />
          </div>
        </div>
      </section>
      <section className={estilos.secao} aria-labelledby="secao-campo">
        <h2 id="secao-campo" className={estilos.tituloSecao}>
          Campo de texto
        </h2>

        <div className={estilos.grupo}>
          <h3 className={estilos.tituloGrupo}>Campos</h3>

          <CampoTexto rotulo="Nota fiscal ou fornecedor (opcional)" placeholder="Ex.: NF 12345 ou Distribuidora X" value={valorCampo} onChange={e => setValorCampo(e.target.value)} />
          Você digitou: {valorCampo}
          <CampoTexto rotulo="Nota fiscal ou fornecedor" value={valorCampo2} onChange={e => setValorCampo2(e.target.value)} />
          Você digitou: {valorCampo2}
          <CampoTexto rotulo="Quantidade recebida" obrigatorio={true} tipo='number' ajuda="Informe a quantidade que você contou, na mesma unidade do produto (un.)." />
          <CampoTexto rotulo="Quantidade recebida" erro='Informe um número inteiro maior que zero.' obrigatorio={true} tipo='number' ajuda="Informe a quantidade que você contou, na mesma unidade do produto (un.)." />
          <CampoTexto rotulo="Nome" value="Martelo" bloqueado motivoBloqueio="Vem do Bling" />
          <CampoTexto rotulo="Qtd" value="10" bloqueado motivoBloqueio="Muda só por movimentação" />
          <CampoAreaTexto rotulo="Observação (opcional)" />
          <CampoAreaTexto rotulo="Justificativa" obrigatorio erro="Escreva uma justificativa com pelo menos 10 caracteres." />
          <CampoSenha rotulo="Senha" />

        </div>

      </section>

      <section className={estilos.secao} aria-labelledby="secao-modal">
        <h2 id="secao-modal" className={estilos.tituloSecao}>
          Modal
        </h2>

        <div className={estilos.grupo}>
          <h3 className={estilos.tituloGrupo}>Modais</h3>
          {<Botao onClick={() => setModalAberto(true)}>Abrir modal</Botao>}
          <Modal aberto={modalAberto} aoFechar={() => setModalAberto(false)} titulo="Inativar produto?" acoes={<><Botao variante="secundario" onClick={() => setModalAberto(false)}>Cancelar</Botao> <Botao variante="destrutivo" onClick={() => setModalAberto(false)}>Inativar produto</Botao></>}>
            <p>O produto 1543 – Fita veda-rosca 18 mm deixará de aparecer nas buscas.</p>
          </Modal>
        </div>

      </section>

      <section className={estilos.secao} aria-labelledby="secao-gaveta">
        <h2 id="secao-gaveta" className={estilos.tituloSecao}>
          Gaveta
        </h2>

        <div className={estilos.grupo}>
          <h3 className={estilos.tituloGrupo}>Esquerda e direita</h3>
          <div className={estilos.linha}>
            <Botao variante="secundario" onClick={() => setGavetaAberta(true)}>Abrir menu</Botao>
            <Botao variante="secundario" onClick={() => setPainelAberto(true)}>Abrir painel</Botao>
          </div>
          <Gaveta aberto={gavetaAberta} aoFechar={() => setGavetaAberta(false)} titulo="Menu">
            <p>Itens do menu lateral (TL02).</p>
          </Gaveta>
          <Gaveta aberto={painelAberto} aoFechar={() => setPainelAberto(false)} titulo="Usuário" lado="direita">
            <p>Painel lateral de Usuários (TL18).</p>
          </Gaveta>
        </div>

      </section>

      <section className={estilos.secao} aria-labelledby="secao-toast">
        <h2 id="secao-toast" className={estilos.tituloSecao}>
          Toast
        </h2>

        <div className={estilos.grupo}>
          <h3 className={estilos.tituloGrupo}>Sucesso e erro (somem em 5 s)</h3>
          <div className={estilos.linha}>
            <Botao variante="secundario" onClick={() => toast.sucesso('Entrada registrada. Quantidade agora: 12 un.')}>Toast de sucesso</Botao>
            <Botao variante="secundario" onClick={() => toast.erro('Não foi possível registrar. Verifique a conexão e tente de novo.')}>Toast de erro</Botao>
          </div>
        </div>

      </section>

      <section className={estilos.secao} aria-labelledby="secao-formatadores">
        <h2 id="secao-formatadores" className={estilos.tituloSecao}>
          Formatadores
        </h2>

        <div className={estilos.grupo}>
          <h3 className={estilos.tituloGrupo}>shared/lib</h3>
          <ul>
            <li>formatarQuantidade(15) → {formatarQuantidade(15)}</li>
            <li>formatarQuantidade(5, {'{ sinal: true }'}) → {formatarQuantidade(5, { sinal: true })}</li>
            <li>formatarQuantidade(-3, {'{ sinal: true }'}) → {formatarQuantidade(-3, { sinal: true })}</li>
            <li>formatarMoeda(34.9) → {formatarMoeda(34.9)}</li>
            <li>formatarMoeda(null) → {formatarMoeda(null)}</li>
            <li>formatarDataHora('2026-10-31T14:32:00') → {formatarDataHora('2026-10-31T14:32:00')}</li>
            <li>formatarDataRelativa(hoje 10:15) → {formatarDataRelativa(new Date(agora).setHours(10, 15))}</li>
            <li>formatarDataRelativa(ontem 16:22) → {formatarDataRelativa(new Date(ontem).setHours(16, 22))}</li>
            <li>formatarDataRelativa('2026-10-28T09:00:00') → {formatarDataRelativa('2026-10-28T09:00:00')}</li>
            <li>normalizarTexto('Espátula') → {normalizarTexto('Espátula')}</li>
            <li>nomeProduto(...) → {nomeProduto({ sku: 1020, nome: 'Parafuso sextavado 6 mm' })}</li>
          </ul>
        </div>

      </section>

      <section className={estilos.secao} aria-labelledby="secao-requisicao">
        <h2 id="secao-requisicao" className={estilos.tituloSecao}>
          Requisição
        </h2>

        <div className={estilos.grupo}>
          <h3 className={estilos.tituloGrupo}>shared/lib</h3>
          {carregando ? <p>Carregando...</p> : erro ? <p>Erro: {obterMensagemErro(erro)}</p> : <p>{dados.length} produtos recebidos do backend.</p>}
          <div className={estilos.linha}>
            <Botao variante="secundario" onClick={recarregar}>Recarregar</Botao>
          </div>
        </div>
        </section>
        <section className={estilos.secao} aria-labelledby='secao-selo'>
          <h2 id='secao-selo' className={estilos.tituloSecao}>
            Selo
          </h2>
          <div className={estilos.grupo}>
            <h3 className={estilos.tituloGrupo}>
              Variantes
            </h3>
              <div className={estilos.linha}>

                <Selo variante='admin'>ADMIN</Selo>
                <Selo variante='alerta' icone={TriangleAlert}>Qtd 2</Selo>
                <Selo variante='erro'>Qtd 0</Selo>
                <Selo variante='sucesso' icone={Check}>OK</Selo>
                <Selo variante='informacao'>Manual</Selo>
                <Selo variante='contador'>3</Selo>

              </div>
          </div>
        </section>
        <section className={estilos.secao} aria-labelledby='secao-card'>
          <h2 id='secao-card' className={estilos.tituloSecao}>
            Card
          </h2>
            <div className={estilos.grupo}>
            <h3 className={estilos.tituloGrupo}>
              Simples
            </h3>
              <div className={estilos.linha}>
                <Card>1020 – Parafuso sextavado 6 mm</Card>
              </div>
          </div>

          <div className={estilos.grupo}>
            <h3 className={estilos.tituloGrupo}>
              Clicável
            </h3>
            <div className={estilos.linha}>
              <Card clicavel como={Link} to="/vitrine">
                1020 – Parafuso sextavado 6 mm
              </Card>
            </div>
          </div>
        </section>
        <section className={estilos.secao} aria-labelledby='secao-skeleton'>
          <h2 id='secao-skeleton' className={estilos.tituloSecao}>
            Skeleton
          </h2>
          <div className={estilos.grupo}>
            <h3 className={estilos.tituloGrupo}>
              Carregando
            </h3>
            <Skeleton largura="100%" altura="var(--espaco-48)"></Skeleton>
            <Skeleton largura="40%"  altura="var(--espaco-16)"></Skeleton>
          </div>
        </section>
        <section className={estilos.secao} aria-labelledby='secao-estado-vazio'>
          <h2 id='secao-estado-vazio' className={estilos.tituloSecao}>
            Estado Vazio
          </h2>
          <div className={estilos.grupo}>
            <h3 className={estilos.tituloGrupo}>
              Exemplos
            </h3>
            <EstadoVazio icone={PackageSearch} texto="Nenhum produto com estoque baixo agora." ></EstadoVazio>
            <EstadoVazio icone={PackageSearch} texto='Nenhum produto encontrado para "parafuzo". Confira a escrita ou busque pelo código.' acao={<Botao variante="secundario">Limpar busca</Botao>}></EstadoVazio>
            </div>
        </section>
        <section className={estilos.secao} aria-labelledby='secao-faixa'>
          <h2 id='secao-faixa' className={estilos.tituloSecao}>
            Faixa
          </h2>
          <div className={estilos.grupo}>
            <h3 className={estilos.tituloGrupo}>
              Variantes
            </h3>
            <Faixa variante="alerta">Sem conexão com o Bling desde 14:32. O Portal continua funcionando normalmente.</Faixa>
            <Faixa>Importação concluída. 120 produtos atualizados.</Faixa>
            </div>
        </section>
    </main>
  )
}
