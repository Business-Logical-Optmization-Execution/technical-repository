// Importa o CSS Module como um objeto: estilos.pagina, estilos.secao...
import estilos from './VitrinePage.module.css'
import { Botao, BotaoIcone, CampoTexto, CampoAreaTexto, CampoSenha } from '@/shared/ui'
import { ArrowLeft, Pencil, Search, X } from 'lucide-react'

import { useState } from 'react'

// Página de testes da Etapa 1. Cada exercício acrescenta uma <section>
// com o componente novo em todos os estados pedidos no "Pronto quando".
export function VitrinePage() {

  const [valorCampo, setValorCampo] = useState('')
  const [valorCampo2, setValorCampo2] = useState('')

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
    </main>
  )
}
