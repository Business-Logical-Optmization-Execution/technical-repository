import { createBrowserRouter } from 'react-router-dom'
import { ROTAS } from '@/shared/config'
import { LoginPage } from '@/pages/login'
import { InicioPage } from '@/pages/inicio'
import { BuscaPage } from '@/pages/busca'
import { ProdutoDetalhePage } from '@/pages/produto-detalhe'
import { RegistrarEntradaPage } from '@/pages/registrar-entrada'
import { RegistrarSaidaPage } from '@/pages/registrar-saida'
import { AjustarContagemPage } from '@/pages/ajustar-contagem'
import { HistoricoPage } from '@/pages/historico'
import { UsuariosPage } from '@/pages/usuarios'
import { DashboardPage } from '@/pages/dashboard'
import { MeuPerfilPage } from '@/pages/meu-perfil'
import { ConfiguracoesPage } from '@/pages/configuracoes'
import { DivergenciasPage } from '@/pages/divergencias'
import { ResolverDivergenciaPage } from '@/pages/resolver-divergencia'
import { ImportarProdutosPage } from '@/pages/importar-produtos'
import { IntegracaoBlingPage } from '@/pages/integracao-bling'
import { NaoEncontradaPage } from '@/pages/nao-encontrada'
import { VitrinePage } from '@/pages/vitrine'
import { LayoutAutenticado } from '../layouts/LayoutAutenticado'
import { ExigeLogin, ExigeAdmin } from './rotasProtegidas'

export const router = createBrowserRouter([
  { path: ROTAS.login, element: <LoginPage /> },
  // Temporária (Etapa 1): fora do LayoutAutenticado porque ainda não há login.
  { path: ROTAS.vitrine, element: <VitrinePage /> },
  {
    element: <ExigeLogin />,
    children: [
      {
        element: <LayoutAutenticado />,
        children: [
          { path: ROTAS.inicio, element: <InicioPage /> },
          { path: ROTAS.busca, element: <BuscaPage /> },
          { path: ROTAS.produto(':id'), element: <ProdutoDetalhePage /> },
          { path: ROTAS.registrarEntrada(':id'), element: <RegistrarEntradaPage /> },
          { path: ROTAS.registrarSaida(':id'), element: <RegistrarSaidaPage /> },
          { path: ROTAS.ajustarContagem(':id'), element: <AjustarContagemPage /> },
          { path: ROTAS.perfil, element: <MeuPerfilPage /> },
          {
            element: <ExigeAdmin />,
            children: [
              { path: ROTAS.historico, element: <HistoricoPage /> },
              { path: ROTAS.usuarios, element: <UsuariosPage /> },
              { path: ROTAS.dashboard, element: <DashboardPage /> },
              { path: ROTAS.configuracoes, element: <ConfiguracoesPage /> },
              { path: ROTAS.divergencias, element: <DivergenciasPage /> },
              { path: ROTAS.divergencia(':id'), element: <ResolverDivergenciaPage /> },
              { path: ROTAS.importar, element: <ImportarProdutosPage /> },
              { path: ROTAS.integracao, element: <IntegracaoBlingPage /> },
            ],
          },
        ],
      },
    ],
  },
  { path: '*', element: <NaoEncontradaPage /> },
])
