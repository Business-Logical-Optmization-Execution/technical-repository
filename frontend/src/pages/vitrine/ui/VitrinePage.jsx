// Importa o CSS Module como um objeto: estilos.pagina, estilos.secao...
import estilos from './VitrinePage.module.css'
import { Botao, useToast } from '@/shared/ui'
import { obterMensagemErro } from '@/shared/api'
import { useSessao } from '@/entities/sessao'

// Página de testes. Cada exercício acrescenta uma <section>
// com o que precisa ser conferido no "Pronto quando".
export function VitrinePage() {

  const toast = useToast()
  const { usuario, ehAdmin, entrar, sair } = useSessao()

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
    </main>
  )
}
