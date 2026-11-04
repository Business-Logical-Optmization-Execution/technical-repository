# Portal Óttimos — Plano de Implementação do Front-end

> **React + React Router + axios · Vite · JavaScript · CSS próprio · Feature-Sliced Design (FSD)**
> Backend: Spring Boot 4 (`technical-repository/backend`) · Mock: json-server 0.17.4
> Grupo BLOX — ADS 3º semestre, 2026/2 · Versão 1.1 · 02/11/2026 (contrato revisado com o back do schema v3, ver seção 11.0) · **Entrega da Sprint 2: 05/11/2026**

Este documento é, ao mesmo tempo, **o roteiro do projeto** e **uma prova prática de React**. Cada passo é um *exercício*: tem um conceito para aprender, um checklist do que construir e um critério objetivo de "pronto". **Quem escreve o código é o grupo**; o agente (Claude Code) atua como professor: explica, dá dicas e corrige. Ao final de todos os passos, o Portal Óttimos roda completo em `localhost`, primeiro com dados de mentira (mock) e, bloco a bloco, ligado ao backend Spring.

---

## Sumário

0. [Como usar este documento](#0-como-usar-este-documento)
1. [Visão geral e escopo](#1-visão-geral-e-escopo)
2. [FSD em 10 minutos, aplicado ao Portal](#2-fsd-em-10-minutos-aplicado-ao-portal)
3. [Fase 0 — Comando inicial (scaffolding)](#3-fase-0--comando-inicial-scaffolding)
4. [O ciclo de cada bloco: mock → back → integração](#4-o-ciclo-de-cada-bloco-mock--back--integração)
5. [Etapa 1 — Fundação](#5-etapa-1--fundação)
6. [Etapa 2 — Bloco A: jornada do balconista](#6-etapa-2--bloco-a-jornada-do-balconista)
7. [Etapa 3 — Bloco B: completa o fluxo + Dashboard](#7-etapa-3--bloco-b-completa-o-fluxo--dashboard)
8. [Etapa 4 — Bloco C: acabamento](#8-etapa-4--bloco-c-acabamento)
9. [Etapa 5 — Bloco D: prévia das telas do Bling](#9-etapa-5--bloco-d-prévia-das-telas-do-bling)
10. [Etapa 6 — Fechamento e apresentação](#10-etapa-6--fechamento-e-apresentação)
11. [O que falta no backend](#11-o-que-falta-no-backend)
- [Apêndice A — Contrato da API](#apêndice-a--contrato-da-api)
- [Apêndice B — `mock/server.cjs`](#apêndice-b--mockservercjs)
- [Apêndice C — `mock/db.seed.json`](#apêndice-c--mockdbseedjson)
- [Apêndice D — Mapa das telas no Figma](#apêndice-d--mapa-das-telas-no-figma)
- [Apêndice E — Decisões sobre as lacunas do brief](#apêndice-e--decisões-sobre-as-lacunas-do-brief)
- [Apêndice F — Glossário](#apêndice-f--glossário)

---

## 0. Como usar este documento

### 0.1 Para o grupo

- **Vocês escrevem o código de cada exercício.** O agente explica o conceito, diz quais arquivos criar, dá dicas quando vocês travam e corrige o que vocês escreveram. Ele só escreve código quando vocês pedirem explicitamente ("pode implementar este").
- O grupo trabalha **junto e em sequência** (uma trilha só, sem tarefas paralelas). Um colega pilota o teclado por exercício; os outros acompanham, sugerem e conferem.
- Faça os exercícios **na ordem**. Cada um usa o que veio antes.
- Só marque `[x]` quando o critério de **"Pronto quando"** for verificado no navegador.
- Exceções, feitas pelo agente porque são infraestrutura e não exercício: o **comando inicial da Fase 0** e o **mock** (`mock/server.cjs` e `mock/db.seed.json`).
- Os textos exatos da interface (títulos, botões, mensagens) estão no **design brief** (`docs/portal-ottimos-design-brief.md`). Este plano cita o essencial; quando houver dúvida, o brief manda. Quando o brief divergir do backend, **o backend manda** (ex.: login por e-mail).

### 0.2 Arquivos que precisam estar no repositório

Na raiz do `technical-repository`, antes de começar:

```
technical-repository/
├── backend/                               ← já existe
└── docs/
    ├── PLANO-FRONTEND.md                  ← este arquivo
    └── portal-ottimos-design-brief.md     ← o design brief
```

A Fase 0 cria a pasta `frontend/` ao lado de `backend/`.

### 0.3 Regras para o agente (Claude Code)

> Estas regras também são copiadas para `frontend/CLAUDE.md` na Fase 0.

1. **Modo professor: o grupo escreve o código, não você.** Um exercício por vez, na ordem do plano. Quando o grupo pedir um exercício:
   - **Enunciado:** é uma **tarefa guiada, não uma especificação**. Siga o modelo da seção 0.5:
     1. **Conceito** em até 10 linhas, em português simples (o grupo está aprendendo front-end). Pode ter um exemplo genérico e curto que não resolva o exercício.
     2. **Passos numerados, do mais simples ao completo.** O passo 1 é sempre a versão mínima que já aparece no navegador. Cada passo acrescenta **uma** coisa e tem três partes: **Faça** (o que escrever, em qual arquivo, em 1 a 4 frases, sem o código), **Confira** (o que deve aparecer no navegador ou no DevTools) e, quando ajudar a entender, **Pense** (uma pergunta curta). Medidas, cores, tokens e textos do brief entram **dentro do passo em que são usados**, nunca numa lista solta no fim. Use quantos passos forem necessários para cobrir o checklist do exercício (normalmente de 5 a 10).
     3. **Final:** o "Pronto quando" do plano e o lembrete para pedir a correção.
     4. Se houver divergência entre o Figma, o brief e o plano, aponte no passo em que ela aparece e diga qual seguir.
     **Não escreva o código da solução**, nem parcial.
   - **Dica:** quando pedirem, dê dicas **progressivas**: primeiro a ideia, depois o recurso do React/JS a usar, depois o trecho onde está o problema. Nunca a solução completa.
   - **Correção:** quando pedirem, leia o código deles, rode `npm run lint` e `npm run build` e revise contra o checklist, o "Pronto quando", as regras de FSD, tokens e acessibilidade. Aponte cada problema com arquivo, linha e o porquê, **sem reescrever o código**. Diga também o que ficou bom.
   - **Implementação:** só escreva código quando o grupo pedir explicitamente ("pode implementar este"). Nesse caso, implemente apenas aquele exercício e comente o código para que sirva de estudo.
   - Não avance de exercício sem o grupo confirmar que conferiu o "Pronto quando".
2. **FSD é lei** (seção 2): respeite camadas, a regra de importação e a API pública (`index.js`) de cada slice.
3. **Estilo:** só CSS Modules + as variáveis de `src/app/styles/tokens.css`. Nunca cores, tamanhos ou fontes "soltos" no código. Nada de Tailwind, styled-components ou bibliotecas de UI.
4. **Layout pelo Figma:** use o MCP do Figma (`get_design_context` / `get_screenshot`) com `fileKey = 403Y8A2xrycyIBl46F36x3` e o `nodeId` da tela (Apêndice D) para **descrever** ao grupo o layout no enunciado (estrutura, espaçamentos, tamanhos, quais tokens usar) e para conferir a fidelidade na correção. O código que o Figma devolve é só referência: não entregue ao grupo como solução.
5. **Textos da interface** vêm do design brief (seção "Telas" e "Tom de Voz"). Botões sempre verbo + objeto. Quantidades sempre com sinal e "un.".
6. **Bibliotecas permitidas:** `react`, `react-dom`, `react-router-dom`, `axios`, `lucide-react`, `recharts`, `json-server@0.17.4`, `concurrently`. Qualquer outra: pergunte antes.
7. **JavaScript** (sem TypeScript). Componentes funcionais, `export` nomeado (sem `export default`), um componente por arquivo.
8. **Não altere** `mock/server.cjs` exceto para corrigir bug, e **não altere o backend** a partir deste plano. Se uma tela precisar de algo que o back não tem, aponte o item do capítulo 11. Fora do modo "pode implementar este", não edite nenhum arquivo do grupo.
9. **Acessibilidade não é opcional:** rótulo visível em todo campo, foco visível, ícone sempre com texto ou `aria-label`, nenhum estado indicado só por cor.
10. Na correção, rode `npm run lint` e `npm run build` e inclua o resultado na revisão. Quem corrige é o grupo.
11. **Git é somente leitura para o agente.** Pode usar `git status`, `git diff`, `git log` e `git show` para consultar. **Nunca** execute `git add`, `commit`, `push`, `pull`, `merge`, `rebase`, `checkout`, `switch`, `branch`, `reset`, `restore`, `stash`, `tag` nem nada que altere o repositório ou o histórico. Commits e branches são feitos pelo grupo.

### 0.4 Roteiro de um exercício

Deixe `npm run dev:mock` rodando e `http://localhost:5173` aberto. Para cada exercício:

| Passo | Quem | Como pedir ao agente |
|---|---|---|
| 1. Enunciado | Agente | *"Enunciado do exercício 1.2."* (vem em passos, como na seção 0.5) |
| 2. Escrever | **Grupo** | Fazer um passo por vez e conferir no navegador antes de ir para o próximo |
| 3. Dica (se travar) | Agente | *"Dica para o passo 3 do 1.2, sem mostrar a resposta."* Peça outra dica se ainda não sair |
| 4. Correção | Agente | *"Corrija o exercício 1.2."* |
| 5. Ajustar | **Grupo** | Corrigir o que a revisão apontou e pedir nova correção, até não sobrar problema |
| 6. Conferir | **Grupo** | Testar o "Pronto quando" no navegador e marcar `[x]` |

Se quiserem que o agente faça algum exercício: *"Pode implementar o exercício 1.5 e comentar o código."*

**Dicas**

- Comecem uma conversa nova a cada etapa (ou usem `/clear`) com: *"Leia docs/PLANO-FRONTEND.md e frontend/CLAUDE.md. Estamos no exercício 2.1."*
- Perguntem **por quê** sempre que algo não fizer sentido ("por que isso fica em `entities` e não em `features`?").
- Na Etapa 1 ainda não há telas: mostrem cada componente na rota temporária `/vitrine`, em todos os estados.

### 0.5 Modelo de enunciado (exercício 1.2)

É assim que o agente deve entregar o enunciado de qualquer exercício: o checklist do plano vira uma sequência de passos pequenos, cada um terminando com algo visível.

> **Exercício 1.2 — Botão**
>
> **Conceito.** Um componente é uma função que recebe um objeto de *props* e devolve JSX. O mesmo `Botao` pode virar "Registrar entrada" (azul) ou "Inativar produto" (vermelho) só mudando uma prop. O texto chega pela prop especial `children`, que é o que fica entre `<Botao>` e `</Botao>`. E `...resto` junta as props que você não tratou para repassá-las ao `<button>` (assim `onClick`, `disabled` e `aria-*` funcionam sem listar uma por uma).
>
> **Passo 1 — O botão mais simples possível.**
> *Faça:* crie `src/shared/ui/botao/Botao.jsx` com uma função `Botao` que recebe `children` e devolve um `<button>` com esse conteúdo. Exporte por `shared/ui/botao/index.js` e `shared/ui/index.js`. Crie a página `pages/vitrine`, registre a rota `/vitrine` (fora do `LayoutAutenticado`) e coloque nela `<Botao>Registrar entrada</Botao>`.
> *Confira:* em `/vitrine` aparece um botão cinza, o padrão do navegador.
> *Pense:* por que receber o texto por `children` e não por uma prop `texto`?
>
> **Passo 2 — Estilo base.**
> *Faça:* crie `Botao.module.css` com a classe `.botao`: altura `var(--altura-botao-secundario)`, `border-radius: var(--raio-8)`, padding horizontal de 20 px e a tipografia `--texto-botao-*`. Importe o CSS no componente e aplique a classe.
> *Confira:* no DevTools, o botão tem 44 px de altura e cantos arredondados.
> *Pense:* que nome a classe ganhou no HTML? Por que não é só `botao`?
>
> **Passo 3 — Variantes.**
> *Faça:* acrescente a prop `variante` com padrão `'primario'`. Crie uma classe para cada variante (`primario`, `secundario`, `destrutivo`, `link`) com as cores da tabela "Botão" do brief, e combine a classe da variante com a `.botao`. Primário e destrutivo usam `--altura-botao-principal` (48 px). Na vitrine, mostre "Registrar entrada", "Cancelar", "Inativar produto" e "Ver histórico completo".
> *Confira:* quatro botões, quatro aparências; o link não tem fundo nem borda.
> ⚠️ *Divergência:* no Figma o "Inativar produto" tem 44 px; o plano pede 48 px. Siga o plano.
>
> **Passo 4 — Hover e desabilitado.**
> *Faça:* hover do primário em `--cor-primaria-hover` e do secundário em `--cor-primaria-suave`. Desabilitado com opacidade 0.5 e `cursor: not-allowed`, sem efeito de hover. Na vitrine, mostre uma versão desabilitada de cada variante.
> *Confira:* passar o mouse num botão desabilitado não muda nada; `Tab` mostra o anel de foco (vem do `global.css`).
>
> **Passo 5 — Props nativas.**
> *Faça:* use `...resto` para repassar o restante das props ao `<button>` e faça `type` valer `'button'` quando não for informado.
> *Confira:* um `onClick={() => console.log('ok')}` na vitrine escreve no console.
> *Pense:* o que aconteceria dentro de um `<form>` se o padrão fosse `submit`?
>
> **Passo 6 — Ícone.**
> *Faça:* acrescente a prop `icone`, que recebe um componente do `lucide-react` e o desenha antes do texto, com 20 px e `aria-hidden`.
> *Confira:* "Editar" aparece com o ícone de lápis (`Pencil`) à esquerda.
> *Pense:* por que o ícone fica escondido do leitor de tela aqui?
>
> **Passo 7 — Carregando e largura total.**
> *Faça:* acrescente `carregando` e `textoCarregando`: quando carregando, o botão fica `disabled`, recebe `aria-busy` e mostra o texto alternativo. Acrescente `larguraTotal`, que faz o botão ocupar 100% da largura.
> *Confira:* "Entrar" carregando mostra "Entrando..." e não responde ao clique.
>
> **Passo 8 — `BotaoIcone`.**
> *Faça:* em `BotaoIcone.jsx` (com o próprio `.module.css`), crie um botão só com ícone, área de 44 × 44 e ícone de 20 px. A prop `rotulo` vira `aria-label` e `title`; se ela faltar, use `console.warn` para avisar. Exporte pelo `index.js`.
> *Confira:* a seta "Voltar" aparece com tooltip ao passar o mouse; um `BotaoIcone` sem `rotulo` gera o aviso no DevTools.
>
> **Final.** *Pronto quando:* em `/vitrine`, as 4 variantes aparecem em todos os estados (normal, hover, foco com `Tab`, desabilitado e carregando), e o `BotaoIcone` sem `rotulo` mostra um aviso no console. Quando terminar, peça *"Corrija o exercício 1.2"*. Se travar, peça uma dica do passo em que parou.

### 0.6 Legenda

| Marca | Significado |
|---|---|
| **Conceito** | O que você aprende de React/FSD neste exercício |
| **Onde** | Pasta(s) do FSD onde o código mora |
| **Figma** | Frame de referência (Apêndice D) |
| **Pronto quando** | Teste objetivo no navegador |
| ⭐ | Desafio extra, opcional |
| 🛑 **Parada do back** | O backend precisa entregar algo antes de seguir |
| 🔌 **Integração** | Trocar o mock pelo Spring numa rota |

---

## 1. Visão geral e escopo

### 1.1 O que é o Portal Óttimos

Sistema interno de estoque da Óttimos Ferragens, para 6 funcionários (3 Operadores, 3 Admin). Nesta fase **não há contato com o Bling**: tudo é prova de conceito (POC). Os produtos entram pelo banco (script SQL de exemplo) e pelo mock.

### 1.2 Stack fixa (validada em 31/10/2026)

| Pacote | Versão | Para quê |
|---|---|---|
| Node.js | 22 LTS | Rodar Vite e o mock |
| Vite (`create-vite`, template `react`) | 8.x | Build e servidor de desenvolvimento |
| React / React DOM | 19.x | Interface |
| react-router-dom | 7.x | Rotas |
| axios | 1.x | Chamadas HTTP |
| lucide-react | 1.x | Ícones (o Figma usa a família Lucide) |
| recharts | 3.x | Gráfico do Dashboard |
| json-server | **0.17.4** (fixo) | Mock da API. A v1 mudou a API; não atualize |
| concurrently | 10.x | Subir mock + front com um comando |

### 1.3 Escopo por bloco

| Bloco | Telas | Situação até 05/11 |
|---|---|---|
| **A — Núcleo** | TL01 Login · TL02 Menu · TL03 Início · TL04 Busca · TL05 Detalhe · TL06 Registrar entrada · TL09 Confirmação | **Obrigatório**, integrado ao Spring |
| **B — Completa o fluxo** | TL07 Registrar saída · TL08 Ajuste por contagem · TL11 Histórico · TL18 Usuários · **Dashboard** (tela nova) | **Meta**, integrado ao Spring |
| **C — Acabamento** | TL10 Avisar falta · TL12 Editar produto + TL09 Inativar · TL13 Configurações · TL19 Meu perfil | Bônus, integrado se der tempo |
| **D — Prévia Bling** | TL14 Divergências · TL15 Resolver · TL16 Importar · TL17 Integração | Bônus, **só com mock**, ações desabilitadas |
| Fora | TL20 Conferência guiada · aba "Cadastrar produto" | Próximas sprints |

A avaliação pede **3 a 5 telas integradas + 1 dashboard integrado**. Só o Bloco A já entrega 5 telas integradas; A + B coloca o grupo com o fluxo praticamente completo.

### 1.4 Calendário sugerido

| Dia | Meta |
|---|---|
| Sáb 31/10 (tarde) | Fase 0 + exercícios 1.1 a 1.3 |
| Dom 01/11 | Exercícios 1.4 a 1.9 e 2.1 a 2.4 |
| Seg 02/11 | Exercícios 2.5 a 2.12 (Bloco A com mock) |
| Ter 03/11 | 🛑 Parada do back P1 + 🔌 Integração A · exercícios 3.1 a 3.3 |
| Qua 04/11 | Exercícios 3.4 e 3.5 · 🛑 P2 + 🔌 Integração B · Bloco C ou D conforme o tempo |
| Qui 05/11 (manhã) | Etapa 6: revisão, roteiro da demonstração |

Se atrasar: **o Bloco A integrado + Dashboard integrado é a linha de segurança**. Corte de trás para frente (D, depois C, depois partes do B).

---

## 2. FSD em 10 minutos, aplicado ao Portal

### 2.1 A ideia

Feature-Sliced Design organiza o front **pelo que o código significa para o negócio**, em camadas empilhadas. Cada camada só pode usar as que estão **abaixo** dela. Isso evita o "tudo importa tudo" que deixa projeto React difícil de mexer.

```
app        ← liga tudo: rotas, providers, estilos globais
pages      ← uma tela inteira (uma rota)
widgets    ← blocos grandes e autossuficientes de uma tela
features   ← uma AÇÃO do usuário que traz valor (registrar entrada, avisar falta)
entities   ← os "substantivos" do negócio (produto, movimentação, usuário)
shared     ← peças sem regra de negócio (botão, axios, formatadores)
```

### 2.2 A regra de importação (a mais importante)

> Um arquivo só importa de camadas **abaixo** da sua. Dentro da mesma camada, um slice **não** importa outro slice.

| Pode | Não pode |
|---|---|
| `pages/inicio` importar `widgets/painel-baixo-estoque` | `entities/produto` importar `features/buscar-produto` (subiu) |
| `features/registrar-movimentacao` importar `entities/produto` | `features/avisar-falta` importar `features/registrar-movimentacao` (vizinho) |
| qualquer camada importar `shared` | `shared` importar qualquer coisa de cima |

**Se dois vizinhos precisam da mesma coisa, ela desce uma camada.** Ex.: o modal de confirmação é usado por várias features → o `Modal` genérico mora em `shared/ui`, e a frase "A quantidade vai de 10 para 15" mora em `entities/movimentacao`.

### 2.3 Slices e segments

- **Slice** = uma pasta de negócio dentro da camada: `entities/produto`, `features/avisar-falta`. (`app` e `shared` não têm slices.)
- **Segment** = a pasta técnica dentro do slice:

| Segment | O que vai nele |
|---|---|
| `ui/` | Componentes React e seus `.module.css` |
| `model/` | Estado e regras: hooks, contextos, funções de domínio |
| `api/` | Funções que chamam o backend |
| `lib/` | Funções auxiliares do slice (formatar, calcular) |
| `config/` | Constantes do slice |

### 2.4 API pública

Todo slice tem um `index.js` na raiz que exporta **só o que os outros podem usar**. Quem está fora importa sempre pelo `index.js`:

```js
// ✅ certo
import { CardProdutoBaixo, calcularSituacao } from '@/entities/produto'
// ❌ errado: atravessa a API pública
import { CardProdutoBaixo } from '@/entities/produto/ui/CardProdutoBaixo'
```

Dentro do próprio slice, use caminhos relativos (`../model/situacao`).

### 2.5 Como decidir onde algo mora

1. É uma tela inteira de uma rota? → `pages`
2. É um pedaço grande de tela, com dados e várias partes, que poderia aparecer em outra tela? → `widgets`
3. É algo que o usuário **faz** (clicar, enviar, confirmar) e muda dados? → `features`
4. É a representação de uma "coisa" do negócio (mostrar um produto, buscar um usuário)? → `entities`
5. Não tem nada a ver com ferragens nem estoque? → `shared`

### 2.6 O mapa do Portal

```
src/
├── app/
│   ├── App.jsx                     # monta os providers e o router
│   ├── providers/                  # SessaoProvider, ToastProvider juntos
│   ├── router/                     # router.jsx, guardas de rota
│   ├── layouts/                    # LayoutAutenticado (cabeçalho + <Outlet/>)
│   └── styles/                     # tokens.css, reset.css, global.css, index.css
├── pages/
│   ├── login/            inicio/           busca/
│   ├── produto-detalhe/  registrar-entrada/ registrar-saida/  ajustar-contagem/
│   ├── historico/        usuarios/          dashboard/
│   ├── meu-perfil/       configuracoes/
│   ├── divergencias/     resolver-divergencia/  importar-produtos/  integracao-bling/
│   └── acesso-negado/    nao-encontrada/
├── widgets/
│   ├── cabecalho/                  # barra superior + gaveta do menu
│   ├── painel-baixo-estoque/       # grade de cards do Início
│   ├── avisos-falta/               # lista de avisos do Início
│   ├── cartao-produto/             # card de dados do Detalhe (leitura e edição)
│   └── ultimas-movimentacoes/      # tabela do Detalhe
├── features/
│   ├── autenticar/                 # formulário de login, sair
│   ├── buscar-produto/             # campo de busca
│   ├── registrar-movimentacao/     # formulários de entrada/saída/ajuste + confirmação
│   ├── avisar-falta/   resolver-aviso/
│   ├── editar-produto/ inativar-produto/
│   ├── gerenciar-usuario/  alterar-senha/  salvar-configuracoes/
├── entities/
│   ├── sessao/   produto/   movimentacao/   usuario/
│   ├── aviso-falta/   configuracao/   divergencia/   integracao/
└── shared/
    ├── api/      # instância axios, normalização de erro
    ├── config/   # ROTAS, constantes globais
    ├── lib/      # formatadores, normalizar texto, useRequisicao
    └── ui/       # Botao, CampoTexto, Selo, Card, Modal, Toast...
```

### 2.7 Nomenclatura

| O quê | Padrão | Exemplo |
|---|---|---|
| Pasta de slice | `kebab-case` | `produto-detalhe`, `aviso-falta` |
| Componente | `PascalCase.jsx`, um por arquivo | `CardProdutoBaixo.jsx` |
| CSS do componente | mesmo nome + `.module.css` | `CardProdutoBaixo.module.css` |
| Página | sufixo `Page` | `InicioPage` |
| Hook | `useAlgo.js` | `useSessao.js`, `useRequisicao.js` |
| Função de API | verbo + recurso | `listarProdutos`, `criarMovimentacao` |
| Funções e variáveis | `camelCase`, em português | `quantidadeDepois`, `calcularSituacao` |
| Constantes | `UPPER_SNAKE_CASE` | `ROTAS`, `MOTIVOS_SAIDA` |
| Componentes de `shared/ui` | nome do brief | `Botao`, `CampoTexto`, `Selo`, `Faixa`, `Gaveta` |
| Imports entre camadas | alias `@/` | `import { http } from '@/shared/api'` |

---

## 3. Fase 0 — Comando inicial (scaffolding)

### 3.1 Pré-requisitos

- [ ] Node.js 22 LTS (`node -v`) e npm 10+
- [ ] Git e VS Code com a extensão do Claude Code
- [ ] Docker Desktop (para subir o backend a partir do Bloco A)
- [ ] Este arquivo e o design brief em `docs/` na raiz do `technical-repository`
- [ ] (Recomendado) MCP do Figma conectado no Claude Code

### 3.2 O comando

Abra o Claude Code **na raiz do `technical-repository`** e cole o bloco abaixo:

````text
Execute o COMANDO INICIAL do Portal Óttimos, seguindo docs/PLANO-FRONTEND.md
(seções 0.3, 2 e 3). Faça na ordem, sem pular passos, e me mostre o resultado
de cada verificação no final. NÃO execute nenhum comando git que altere o
repositório (add, commit, push, branch etc.); git só para leitura.

1) PROJETO
   - Na raiz do repositório:
     npm create vite@latest frontend -- --template react --no-interactive
   - cd frontend && npm install
   - npm install react-router-dom axios lucide-react recharts
   - npm install -D json-server@0.17.4 concurrently

2) LIMPEZA DO TEMPLATE
   - Apague src/App.jsx, src/App.css, src/index.css, a pasta src/assets e
     public/icons.svg. Mantenha public/favicon.svg.

3) ESTRUTURA FSD (seção 2.6 do plano)
   - Crie as camadas app, pages, widgets, features, entities, shared.
   - app/: App.jsx, providers/AppProviders.jsx, router/router.jsx,
     router/rotasProtegidas.jsx (vazio por enquanto, com comentário TODO),
     layouts/LayoutAutenticado.jsx, styles/{tokens.css, reset.css,
     global.css, index.css}. index.css importa os outros três na ordem
     reset → tokens → global.
   - shared/: api/{http.js, erros.js, index.js}, config/{rotas.js, index.js},
     lib/{index.js}, ui/{index.js}.
       * http.js: instância axios com baseURL '/api' e timeout 10000.
       * erros.js: função obterMensagemErro(erro) que devolve
         erro.response?.data?.mensagem ou "Não foi possível falar com o
         servidor. Tente de novo." quando não houver resposta.
       * rotas.js: objeto ROTAS com TODAS as rotas da seção 3.4 do plano e
         funções para rotas com parâmetro (ex.: ROTAS.produto(id)).
   - Para CADA slice listado na seção 2.6 (pages, widgets, features,
     entities), crie a pasta com um index.js contendo apenas o comentário
     "// API pública do slice <nome>". Não crie segments vazios.
   - Para cada page, crie ui/<Nome>Page.jsx com um placeholder
     (<h1> com o título da tela) e exporte pelo index.js.
   - router.jsx: createBrowserRouter com todas as rotas da seção 3.4
     apontando para as pages placeholder; rotas autenticadas dentro de
     LayoutAutenticado (por enquanto só renderiza <Outlet/>); rota "*" para
     NaoEncontradaPage.
   - App.jsx: <AppProviders><RouterProvider router={router}/></AppProviders>,
     importa './styles/index.css'. AppProviders por enquanto só devolve children.
   - src/main.jsx: importa { App } de './app/App' e renderiza em StrictMode.

4) CONFIGURAÇÃO
   - vite.config.js: exatamente o da seção 3.3 do plano (alias @ e proxy
     híbrido mock/Spring).
   - jsconfig.json com o alias "@/*" → "src/*" (para o VS Code autocompletar).
   - .env.example com MOCK_API_URL, SPRING_API_URL e ROTAS_NO_SPRING
     (conteúdo da seção 3.3) e copie para .env.local.
   - .gitignore: acrescente "mock/db.json" (o .local já está ignorado).
   - package.json, scripts:
       "dev": "vite",
       "mock": "node mock/server.cjs",
       "mock:reset": "node -e \"require('fs').copyFileSync('mock/db.seed.json','mock/db.json')\"",
       "dev:mock": "concurrently -k -n mock,web -c yellow,cyan \"npm:mock\" \"npm:dev\"",
       "build": "vite build", "lint": "oxlint", "preview": "vite preview"
   - index.html: lang="pt-BR", <title>Portal Óttimos</title>, preconnect e
     link do Google Fonts para Inter (400, 600, 700) e JetBrains Mono (500).

5) MOCK
   - mock/server.cjs com o conteúdo EXATO do Apêndice B.
   - mock/db.seed.json com o conteúdo EXATO do Apêndice C.

6) ARQUIVOS DE APOIO
   - frontend/CLAUDE.md: copie as regras da seção 0.3 do plano (incluindo a
     regra 11, git somente leitura) + a tabela de
     nomenclatura da seção 2.7 + "Plano completo: ../docs/PLANO-FRONTEND.md".
   - frontend/README.md: como instalar, rodar (npm run dev:mock), resetar o
     mock, apontar rotas para o Spring (ROTAS_NO_SPRING) e logins de teste
     (Apêndice C: marcia@ottimos.com / 123456 é Admin; ronaldo@ottimos.com /
     123456 é Operador).

7) VERIFICAÇÃO (mostre a saída de cada uma)
   - npm run build termina sem erro.
   - npm run lint sem erros.
   - Com npm run dev:mock rodando:
       curl http://localhost:3001/produtos   → lista de produtos
       curl http://localhost:5173/api/produtos → a mesma lista (via proxy)
       http://localhost:5173/login abre o placeholder do Login
       http://localhost:5173/rota-que-nao-existe abre a página não encontrada
   - Liste a árvore de src/ (sem node_modules) para conferirmos com a
     seção 2.6.
````

### 3.3 Arquivos de configuração (referência do comando)

**`vite.config.js`**

```js
import { fileURLToPath, URL } from 'node:url'
import react from '@vitejs/plugin-react'
import { defineConfig, loadEnv } from 'vite'

// Toda chamada do front vai para /api/... e o Vite decide o destino:
//  - rotas listadas em ROTAS_NO_SPRING  -> backend Spring (8080)
//  - todo o resto                       -> mock json-server (3001)
// Assim a integração acontece rota por rota, editando só o .env.local.
export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), '')
  const mockUrl = env.MOCK_API_URL || 'http://localhost:3001'
  const springUrl = env.SPRING_API_URL || 'http://localhost:8080'
  const rotasNoSpring = (env.ROTAS_NO_SPRING || '')
    .split(',')
    .map((rota) => rota.trim())
    .filter(Boolean)

  const semPrefixoApi = (caminho) => caminho.replace(/^\/api/, '')
  const proxy = {}
  // A ordem importa: as rotas do Spring vêm antes do "/api" genérico.
  for (const rota of rotasNoSpring) {
    proxy[`/api/${rota}`] = { target: springUrl, changeOrigin: true, rewrite: semPrefixoApi }
  }
  proxy['/api'] = { target: mockUrl, changeOrigin: true, rewrite: semPrefixoApi }

  return {
    plugins: [react()],
    resolve: {
      alias: { '@': fileURLToPath(new URL('./src', import.meta.url)) },
    },
    server: { port: 5173, proxy },
  }
})
```

**`.env.example`** (copiar para `.env.local`, que não vai para o Git)

```bash
# Copie para .env.local (não vai para o Git) e ajuste.
# Lidas só pelo vite.config.js (sem prefixo VITE_ = não vão para o navegador).

# Onde roda o mock (npm run mock)
MOCK_API_URL=http://localhost:3001

# Onde roda o backend Spring (docker compose up, na pasta backend/)
SPRING_API_URL=http://localhost:8080

# Rotas que JÁ estão prontas no Spring, separadas por vírgula.
# Vazio = tudo no mock. Ex.: ROTAS_NO_SPRING=auth,produtos,movimentacoes
ROTAS_NO_SPRING=
```

### 3.4 Rotas do front

| Rota | Página | Perfil | Bloco |
|---|---|---|---|
| `/login` | `LoginPage` | público | A |
| `/` | `InicioPage` | todos | A |
| `/busca?q=termo` | `BuscaPage` | todos | A |
| `/produtos/:id` | `ProdutoDetalhePage` | todos (Editar/Inativar só Admin) | A |
| `/produtos/:id/entrada` | `RegistrarEntradaPage` | todos | A |
| `/produtos/:id/saida` | `RegistrarSaidaPage` | todos | B |
| `/produtos/:id/ajuste` | `AjustarContagemPage` | todos | B |
| `/historico?produtoId=&usuarioId=&periodo=&tipo=` | `HistoricoPage` | Admin | B |
| `/usuarios` | `UsuariosPage` | Admin | B |
| `/dashboard` | `DashboardPage` | Admin | B |
| `/perfil` | `MeuPerfilPage` | todos | C |
| `/configuracoes` | `ConfiguracoesPage` | Admin | C |
| `/divergencias` | `DivergenciasPage` | Admin | D |
| `/divergencias/:id` | `ResolverDivergenciaPage` | Admin | D |
| `/importar` | `ImportarProdutosPage` | Admin | D |
| `/integracao` | `IntegracaoBlingPage` | Admin | D |
| `*` | `NaoEncontradaPage` | todos | 0 |

`/vitrine` é uma rota temporária de testes criada na Etapa 1 e apagada na Etapa 6. `AcessoNegadoPage` não tem rota: é renderizada pela guarda quando um Operador abre uma URL de Admin (exercício 2.3). TL12 (Editar) e os modais TL09/TL10 não têm rota: são estados dentro do Detalhe.

### 3.5 Verificação da Fase 0

- [ ] `npm run dev:mock` sobe o mock (3001) e o front (5173)
- [ ] `http://localhost:5173/api/produtos` devolve a lista do mock
- [ ] Todas as rotas da 3.4 abrem um placeholder; rota inexistente abre a "não encontrada"
- [ ] A árvore de `src/` bate com a seção 2.6
- [ ] `npm run build` e `npm run lint` sem erros

---

## 4. O ciclo de cada bloco: mock → back → integração

Cada bloco (A, B, C) passa por três momentos:

1. **Front com mock.** As telas chamam `/api/...`; o Vite manda tudo para o json-server. O mock simula o Spring, seguindo o contrato do Apêndice A.
2. 🛑 **Parada do back.** O grupo implementa no Spring os endpoints que aquele bloco precisa (capítulo 11), seguindo o contrato.
3. 🔌 **Integração.** Acrescente a rota em `ROTAS_NO_SPRING` no `.env.local` e o Vite passa a mandar só aquela rota para o Spring. Se algo quebrar, a diferença está no contrato. **Regra do grupo (02/11):** quando o back já tem o recurso de outro jeito, vale o back codado, e front e mock se adaptam; quando o recurso ainda não existe, o back implementa conforme a seção 11.0.

```bash
# .env.local — exemplo depois da Integração A
ROTAS_NO_SPRING=auth,produtos,movimentacoes
```

**Atenção ao prefixo.** O proxy decide pelo começo da rota: com `produtos` no Spring, **tudo** que começa com `/api/produtos` vai para o Spring, inclusive `/api/produtos/1/quantidade`. Se uma tela nova usa um endpoint que o Spring ainda não tem, mas cujo prefixo já está em `ROTAS_NO_SPRING`, **tire esse prefixo da lista enquanto desenvolve com mock** e recoloque depois da parada do back. (Acontece no Bloco C com `produtos`, `usuarios` e `configuracoes`.)

> O Vite reinicia sozinho quando o `.env.local` muda. Se não reiniciar, pare e rode `npm run dev:mock` de novo.

**Como subir o backend** (na pasta `backend/` do repositório): `cp .env.example .env` (só na primeira vez) e `docker compose up --build`. Swagger: `http://localhost:8080/swagger-ui.html`.

**Dica:** `npm run mock:reset` devolve o mock ao estado original (útil antes de apresentar).

---

## 5. Etapa 1 — Fundação

Objetivo da etapa: ter as peças de Lego prontas. Nenhuma tela de verdade ainda, mas tudo que as telas vão usar. Para testar os componentes, crie uma rota temporária `/vitrine` (página `pages/vitrine`) que mostra cada peça em todos os estados. Apague-a no fim da Etapa 6.

### Exercício 1.1 — Tokens de design e estilos globais

**Conceito:** variáveis CSS (`--cor-primaria`) como "fonte da verdade" visual. Mudou o token, mudou o sistema inteiro.
**Onde:** `app/styles/`

- [ ] `tokens.css`: em `:root`, todas as cores, fontes, espaçamentos, raios, sombra, alturas e área clicável da seção "Design Tokens" do brief. Nome do token = nome do brief (`--cor-primaria`, `--texto-h1-tamanho`, `--espaco-16`, `--raio-8`, `--altura-botao-principal`...).
- [ ] Tipografia como trio de variáveis por estilo: `--texto-h1-tamanho`, `--texto-h1-peso`, `--texto-h1-altura`. Fontes: `--fonte-interface: 'Inter', sans-serif` e `--fonte-codigo: 'JetBrains Mono', monospace`.
- [ ] `reset.css`: `box-sizing: border-box`, margens zeradas, `img { max-width: 100% }`, botões e inputs herdam a fonte.
- [ ] `global.css`: `body` com `--cor-fundo`, `--cor-texto`, `--fonte-interface`, 16 px; classe utilitária `.visualmente-oculto` (texto só para leitor de tela); foco global `:focus-visible { outline: 3px solid var(--cor-foco); outline-offset: 2px; }`.
- [ ] Nenhum `#FFFFFF` em lugar nenhum (branco é `--cor-branco: #FAFAFC`).

**Pronto quando:** o fundo da página é `#F1F1F6`, a fonte é Inter, e `Tab` mostra o anel de foco azul em qualquer link.

### Exercício 1.2 — `Botao`

**Conceito:** componente com **props** e **variantes**; `children`; repassar props nativas com `...resto`.
**Onde:** `shared/ui/botao/` (`Botao.jsx`, `Botao.module.css`, `index.js`) e reexportar em `shared/ui/index.js`.
**Figma:** botões da TL05 (`3:40778`).

- [ ] Props: `variante` (`'primario' | 'secundario' | 'destrutivo' | 'link'`), `carregando`, `textoCarregando`, `icone` (componente Lucide), `larguraTotal`, `type` (padrão `'button'`), `...resto`.
- [ ] Alturas: 48 px (primário/destrutivo) e 44 px (secundário); `raio-8`; padding horizontal 20 px; `texto-botao`.
- [ ] Estados: hover, foco (anel), desabilitado (opacidade 0.5, `cursor: not-allowed`), carregando (mostra `textoCarregando`, ex. "Entrando...", e fica `disabled` + `aria-busy`).
- [ ] `BotaoIcone`: só ícone, 44 × 44, **exige** `rotulo` (vira `aria-label` e `title`).

**Pronto quando:** na `/vitrine`, as 4 variantes aparecem em todos os estados, e o `BotaoIcone` sem `rotulo` mostra um aviso no console.

### Exercício 1.3 — Campos de formulário

**Conceito:** **componente controlado** (`value` + `onChange`); `useId` para ligar `<label>` e mensagem de erro ao campo (`htmlFor`, `aria-describedby`, `aria-invalid`).
**Onde:** `shared/ui/campo/`
**Figma:** campos da TL06 (`3:40993`) e TL12 (`3:41581`).

- [ ] `CampoTexto`: `rotulo`, `obrigatorio` (escreve "(obrigatório)" no rótulo), `ajuda`, `erro`, `bloqueado` + `motivoBloqueio` (ícone de cadeado + texto "Vem do Bling" / "Muda só por movimentação"), `tipo` (`text`, `number`, `email`), `...resto`.
- [ ] `CampoSenha`: `CampoTexto` + `BotaoIcone` de olho dentro do campo ("Mostrar senha" / "Ocultar senha").
- [ ] `CampoAreaTexto`: mesma anatomia com `<textarea>`.
- [ ] Erro: borda `--cor-erro`, ícone `TriangleAlert` + mensagem abaixo, ligada por `aria-describedby`.
- [ ] Rótulo sempre visível acima (`texto-rotulo`, caixa alta como no Figma).

**Pronto quando:** clicar no rótulo foca o campo; com erro, o leitor de tela anuncia a mensagem (confira no DevTools → Accessibility que o `input` tem `aria-describedby` apontando para o id da mensagem).

### Exercício 1.4 — `Selo`, `Card`, `Skeleton`, `EstadoVazio`, `Faixa`

**Conceito:** componentes de **apresentação** pura (só recebem props e desenham). Composição com `children`.
**Onde:** `shared/ui/selo/`, `shared/ui/card/`, `shared/ui/skeleton/`, `shared/ui/estado-vazio/`, `shared/ui/faixa/`

- [ ] `Selo`: variantes `admin`, `alerta`, `erro`, `sucesso`, `informacao`, `contador`. Aceita `icone`. Cores conforme a tabela "Selo (badge)" do brief.
- [ ] `Card`: `clicavel` (borda `--cor-borda` + hover `--cor-primaria-suave`) ou não (borda `--cor-divisoria`); `raio-12`; padding `espaco-24`; **sem sombra**. Quando `clicavel`, renderiza como o elemento passado em `como` (ex.: `Link`).
- [ ] `Skeleton`: bloco `--cor-primaria-suave` com `largura`/`altura`, animação suave de opacidade; `aria-hidden`.
- [ ] `EstadoVazio`: ícone + texto + ação opcional.
- [ ] `Faixa`: variantes `alerta` e `informacao`, com ícone e texto.

**Pronto quando:** a `/vitrine` mostra todos os selos do brief ("ADMIN", "⚠ Qtd 2", "Qtd 0", "✓ OK", "Manual") com contraste correto.

### Exercício 1.5 — `Modal` e `Gaveta` com `<dialog>`

**Conceito:** `useRef` + `useEffect` para conversar com o DOM; o elemento nativo `<dialog>` já prende o foco e fecha com `Esc`.
**Onde:** `shared/ui/modal/`, `shared/ui/gaveta/`
**Figma:** modal da TL09 (`3:41114`), gaveta da TL02 (`3:40273`).

- [ ] `Modal`: props `aberto`, `aoFechar`, `titulo`, `children`, `acoes` (botões). Abre com `showModal()` quando `aberto` vira `true`; fecha com `close()`.
- [ ] Guardar o elemento que tinha foco antes de abrir e devolver o foco a ele ao fechar.
- [ ] `Esc`, botão "×" (`BotaoIcone` "Fechar") e clique no fundo escurecido chamam `aoFechar`.
- [ ] Fundo escurecido via `::backdrop`; `sombra-sobreposicao`; `raio-12`; largura máx. 640 px; título `texto-h2`; botões à direita (secundário à esquerda, ação à direita).
- [ ] `Gaveta`: mesmo mecanismo, presa à esquerda, altura total, largura 360 px (no mobile, 85% da tela); prop `lado` (`'esquerda' | 'direita'`) para reaproveitar no painel lateral de Usuários (TL18).

**Pronto quando:** abrir o modal pelo teclado, navegar com `Tab` (o foco não sai do modal), apertar `Esc` e o foco voltar ao botão que abriu.

### Exercício 1.6 — Toasts

**Conceito:** **Context API**: um `ToastProvider` no topo do app e um hook `useToast()` que qualquer componente usa sem passar props.
**Onde:** `shared/ui/toast/` (`ToastProvider.jsx`, `useToast.js`, `Toast.module.css`). Registrar o provider em `app/providers/AppProviders.jsx`.

- [ ] `useToast()` devolve `{ sucesso(texto), erro(texto) }`.
- [ ] Pilha no canto superior direito, abaixo do cabeçalho; some em 5 s; botão "Fechar".
- [ ] Sucesso: ícone `CircleCheck` + `--cor-sucesso`; erro: `TriangleAlert` + `--cor-erro`.
- [ ] Container com `role="status"` e `aria-live="polite"` (o de erro com `role="alert"`).

**Pronto quando:** dois botões na `/vitrine` disparam toasts que empilham e somem sozinhos.

### Exercício 1.7 — Cliente HTTP e `useRequisicao`

**Conceito:** separar "buscar dados" de "desenhar dados". Um **hook personalizado** encapsula `loading`, `erro` e `dados`.
**Onde:** `shared/api/` e `shared/lib/useRequisicao.js`

- [ ] Em `shared/api/http.js`: já existe a instância. Acrescente um interceptor de resposta que só repassa o erro (ponto onde, no futuro, tratar sessão expirada).
- [ ] `obterMensagemErro(erro)` e `obterErrosDeCampo(erro)` → transforma `erros: [{campo, mensagem}]` (Apêndice A.1) em objeto `{ quantidade: 'mensagem' }`.
- [ ] `useRequisicao(funcao, dependencias)` devolve `{ dados, carregando, erro, recarregar }`. Chama a função no `useEffect`, ignora a resposta se o componente foi desmontado (flag `ativo`).
- [ ] Documente no topo do arquivo, em comentário, o formato de retorno.

**Pronto quando:** a `/vitrine` usa `useRequisicao(() => http.get('/produtos').then(r => r.data))` e mostra "Carregando...", depois a quantidade de produtos; com o mock desligado, mostra a mensagem de erro amigável.

### Exercício 1.8 — Formatadores e texto

**Conceito:** **funções puras** (mesma entrada → mesma saída) são fáceis de testar e reaproveitar. `Intl` do navegador.
**Onde:** `shared/lib/formatar.js`, `shared/lib/texto.js`

- [ ] `formatarQuantidade(n, { sinal })` → `"15 un."`, `"+5 un."`, `"−3 un."` (sinal de menos verdadeiro `−`, U+2212).
- [ ] `formatarMoeda(valor)` → `"R$ 34,90"` (`Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' })`); `null` → `"—"`.
- [ ] `formatarDataHora(iso)` → `"31/10/2026 · 14:32"`; `formatarDataRelativa(iso)` → `"hoje 10:15"`, `"ontem 16:22"` ou a data.
- [ ] `normalizarTexto(s)` → minúsculas, sem acento (`'Espátula'` → `'espatula'`).
- [ ] `nomeProduto({ sku, nome })` → `"1020 – Parafuso sextavado 6 mm"` (travessão `–`).

**Pronto quando:** no console do navegador (importando pela `/vitrine`), cada função devolve os exemplos acima.

### Exercício 1.9 — Roteamento e layout autenticado

**Conceito:** **rotas aninhadas** e `<Outlet/>`: o layout desenha o que é comum e a rota filha desenha o miolo.
**Onde:** `app/router/`, `app/layouts/`, `pages/nao-encontrada/`

- [ ] `LayoutAutenticado`: área de conteúdo com margem `espaco-32` (desktop) / `espaco-16` (abaixo de 1024 px) e `<Outlet/>`. O cabeçalho entra no exercício 2.4.
- [ ] `TituloPagina` em `shared/ui`: botão voltar opcional (`BotaoIcone` "Voltar", seta `ArrowLeft`) + `<h1>` + selo ADMIN opcional + espaço para ações à direita.
- [ ] `NaoEncontradaPage`: "Página não encontrada." + link "Ir para o Início".
- [ ] Cada page placeholder usa `TituloPagina` e muda `document.title` para "Nome da tela · Portal Óttimos".

**Pronto quando:** navegar entre as rotas pelo endereço troca o título da aba e o layout se mantém.

---

## 6. Etapa 2 — Bloco A: jornada do balconista

> **A história:** Ronaldo entra, procura "martelo", abre o produto, registra a entrada de 10 un. que acabaram de chegar, confirma e vê "Entrada registrada. Quantidade agora: 12 un.".

### Exercício 2.1 — Entidade `sessao`

**Conceito:** estado **global** com Context; persistir no `localStorage` para sobreviver ao F5.
**Onde:** `entities/sessao/` (`model/SessaoProvider.jsx`, `model/useSessao.js`, `api/sessaoApi.js`, `index.js`)

- [ ] `api/sessaoApi.js`: `entrar({ email, senha })` → `POST /auth/login`, devolve o usuário (Apêndice A.2).
- [ ] `SessaoProvider` guarda `usuario` (ou `null`); lê do `localStorage` (chave `portal-ottimos:sessao`) ao iniciar, com `try/catch`.
- [ ] `useSessao()` devolve `{ usuario, ehAdmin, entrar, sair }`. `ehAdmin = usuario?.perfilAcesso?.nome === 'Administrador'`.
- [ ] Constante `PERFIS = { ADMIN: 'Administrador', OPERADOR: 'Operador' }` em `entities/sessao/config`.
- [ ] Registrar `SessaoProvider` em `AppProviders`.

**Pronto quando:** chamando `entrar` pela `/vitrine` com `marcia@ottimos.com / 123456`, o usuário aparece; após F5 continua lá; `sair()` limpa.

### Exercício 2.2 — Feature `autenticar` + página Login (TL01)

**Conceito:** formulário controlado com `onSubmit` (o `Enter` envia de graça); `useNavigate` para redirecionar.
**Onde:** `features/autenticar/` (`ui/FormLogin.jsx`), `pages/login/`
**Figma:** `3:40251`

- [ ] `FormLogin`: campos **E-MAIL** (placeholder "Ex.: marcia@ottimos.com") e **SENHA** (`CampoSenha`), erro acima do botão, botão primário "Entrar →" (carregando: "Entrando...") e o texto "Esqueceu a senha? Peça a um administrador para redefinir.".
- [ ] Erro 401 → "Usuário ou senha incorretos." (não diz qual dos dois errou). Erro de rede → mensagem de `obterMensagemErro`.
- [ ] Sucesso → navega para a rota de origem (se veio redirecionado) ou para `/`.
- [ ] `LoginPage`: duas colunas (~40% formulário em `--cor-branco`, ~60% painel `--cor-fundo` com a ilustração e a marca "PORTAL ÓTTIMOS"). Abaixo de 1024 px, o painel some.
- [ ] Usuário já logado que abre `/login` vai direto para `/`.

**Pronto quando:** login com senha errada mostra o erro; com `ronaldo@ottimos.com / 123456`, cai no Início; `Enter` no campo senha envia.

### Exercício 2.3 — Guardas de rota

**Conceito:** componentes que decidem **se** renderizam os filhos (`<Navigate/>`), protegendo rotas por perfil.
**Onde:** `app/router/rotasProtegidas.jsx`, `pages/acesso-negado/`

- [ ] `ExigeLogin`: sem sessão → `<Navigate to="/login" state={{ de: location }} replace />`.
- [ ] `ExigeAdmin`: logado sem ser Admin → renderiza `AcessoNegadoPage` ("Você não tem acesso a esta página." + link "Ir para o Início").
- [ ] No `router.jsx`: rotas autenticadas dentro de `ExigeLogin` + `LayoutAutenticado`; rotas Admin (seção 3.4) dentro de `ExigeAdmin`.

**Pronto quando:** sem login, `/produtos/1` leva ao Login e, depois de entrar, volta para `/produtos/1`; como Ronaldo, `/usuarios` mostra "Você não tem acesso a esta página.".

### Exercício 2.4 — Widget `cabecalho` (TL02) + sair

**Conceito:** renderização **condicional** por perfil; itens de menu a partir de um **array de configuração** (`.map`).
**Onde:** `widgets/cabecalho/`, `shared/ui/avatar/`, `features/autenticar/` (`ui/BotaoSair.jsx`), `app/layouts/LayoutAutenticado.jsx`
**Figma:** `3:40273` (Admin) e `3:40414` (Operador)

- [ ] `Avatar` em `shared/ui`: círculo com o ícone `UserRound`, tamanhos `pequeno` (40 px) e `grande` (80 px).
- [ ] Barra `--cor-primaria` + faixa de 4 px `--cor-destaque`. Esquerda: botão (`Avatar` + nome + perfil) com `aria-label="Abrir menu"`. Direita: "PORTAL ÓTTIMOS".
- [ ] Clique abre a `Gaveta`: avatar grande + nome, divisória, itens de menu, rodapé "Sair →" em `--cor-erro`.
- [ ] Itens num array `{ rotulo, rota, somenteAdmin, contador? }`, na ordem: Perfil · Dashboard `ADMIN` · Configurações `ADMIN` · Histórico `ADMIN` · Divergências `ADMIN` · Integração com o Bling `ADMIN` · Importar produtos `ADMIN` · Usuários `ADMIN` · Buscar. Itens Admin **não aparecem** para Operador.
- [ ] Item da rota atual marcado (`aria-current="page"`). Clicar num item fecha a gaveta.
- [ ] `BotaoSair`: chama `sair()` e navega para `/login`.
- [ ] Encaixar o `Cabecalho` no `LayoutAutenticado`.

**Pronto quando:** como Márcia, a gaveta mostra todos os itens com selo ADMIN; como Ronaldo, só Perfil e Buscar; "Sair" volta ao Login e o F5 não traz a sessão de volta.

### Exercício 2.5 — Entidade `produto`

**Conceito:** a entidade concentra **tudo o que é "produto"**: buscar no back, regras (situação do estoque) e como desenhar um produto.
**Onde:** `entities/produto/` (`api/`, `model/`, `lib/`, `ui/`)
**Figma:** cards da TL03 (`3:40527`) e TL04 (`3:40717`)

- [ ] `api/produtoApi.js`: `listarProdutos()` → `GET /produtos`; `buscarProduto(id)` → `GET /produtos/{id}`.
- [ ] `model/situacao.js`: `calcularSituacao(produto)` → `'ZERADO'` se `quantidade <= 0`; `'BAIXO'` se `quantidade <= estoqueMinimoEfetivo`; senão `'OK'`. `ordenarPorCriticidade(lista)` → zerados primeiro, depois menor `quantidade / estoqueMinimoEfetivo`. 🤝 O `estoqueMinimoEfetivo` ainda não existe no back (ponto D3 da seção 11.0).
- [ ] `lib/buscarPorTermo.js`: `filtrarProdutos(lista, termo)` → compara `normalizarTexto` com nome, sku e **cada item de `localizacoes`** (lista de textos, pode vir vazia); todas as palavras do termo precisam aparecer ("parafuso 6" acha "Parafuso sextavado 6 mm").
- [ ] `ui/FotoProduto` (imagem com `alt` = nome; sem foto, inclusive com `fotoUrl` ausente → bloco `--cor-primaria-suave` com ícone `Image`), `ui/SeloSituacao` ("⚠ Qtd 2" alerta; "Qtd 0" erro; nada quando OK; com `mostrarNome`, "Baixo" e "Zerado", usado na TL04 e na TL05), `ui/CardProdutoBaixo` (TL03), `ui/CardProdutoResultado` (TL04: "ID 1020" em `texto-codigo`, "Qtd 2 · R$ 34,90", "Local C3-P2" com as localizações separadas por " · ", ou "Sem localização" com a lista vazia).
- [ ] Os cards são `Link` para `/produtos/{id}` e aceitam `exibirFoto`.

**Pronto quando:** a `/vitrine` lista os produtos do mock com os dois tipos de card, e Serrote (0 un.) aparece com selo de erro.

### Exercício 2.6 — Feature `buscar-produto`

**Conceito:** `useSearchParams`: o termo da busca vive na **URL** (dá para voltar, recarregar e compartilhar).
**Onde:** `features/buscar-produto/` (`ui/CampoBusca.jsx`)

- [ ] `CampoBusca`: ícone `Search`, altura 56 px, placeholder "Pesquisar produto", rótulo acessível "Pesquisar produto" (classe `.visualmente-oculto`), prop `focoAutomatico`, prop `valorInicial`.
- [ ] `Enter` navega para `/busca?q=<termo>`. Menos de 2 caracteres: não navega e mostra a ajuda "Digite pelo menos 2 letras.".
- [ ] Variante com `BotaoIcone` lupa à direita (usada na TL04).

**Pronto quando:** digitar "mar" + `Enter` leva a `/busca?q=mar`.

### Exercício 2.7 — Página Início (TL03) + widget `painel-baixo-estoque`

**Conceito:** **estado derivado**: a lista de baixo estoque é *calculada* a partir dos produtos, não guardada num `useState` separado.
**Onde:** `widgets/painel-baixo-estoque/`, `pages/inicio/`
**Figma:** `3:40527` (Admin) e `3:40623` (Operador)

- [ ] Abas sublinhadas: "Início" (ativa) e "Cadastrar produto" (desabilitada, `title="Em breve"`). Componente `Abas` em `shared/ui`.
- [ ] `CampoBusca` com foco automático.
- [ ] Widget: título "Produtos com baixo estoque" + legenda "ABAIXO DA QTD. MÍNIMA"; grade de até 6 `CardProdutoBaixo` (3 colunas ≥ 1024 px, 2 colunas, 1 coluna em 360 px), do mais crítico.
- [ ] Estados: carregando (6 skeletons), vazio ("Nenhum produto com baixo estoque agora."), erro (mensagem + "Tentar de novo" → `recarregar`).
- [ ] Ler `exibirFotos` de `GET /configuracoes` (entidade `configuracao`, `buscarConfiguracoes()`) e passar aos cards. Enquanto o Bloco C não chega, a entidade só tem essa função.
- [ ] Espaço reservado (comentário) para o widget "Avisos de falta" (exercício 4.1).

**Pronto quando:** o Início mostra Serrote e Fita veda-rosca (0 un.) primeiro, depois Martelo, Parafuso sextavado e Chave de fenda; o cursor já está na busca.

### Exercício 2.8 — Página Busca (TL04)

**Conceito:** ler a URL com `useSearchParams`; **paginação no cliente** com "Carregar mais".
**Onde:** `pages/busca/`
**Figma:** `3:40717`

- [ ] Barra: `BotaoIcone` voltar (→ `/`) + `CampoBusca` (placeholder "Pesquisar produto por nome ou ID", termo mantido) com lupa.
- [ ] "RESULTADOS" + contagem ("4 produtos" / "1 produto").
- [ ] Grade de 2 colunas de `CardProdutoResultado` (1 coluna em tela estreita); mostra 25 e botão secundário "Carregar mais" quando houver mais.
- [ ] Vazio: `Nenhum produto encontrado para "parafuzo". Confira a escrita ou busque pelo código.`
- [ ] Sem termo (vindo do menu "Buscar"): só o campo, com foco.

**Pronto quando:** `/busca?q=c3` acha os produtos da localização C3; `/busca?q=martelo` acha Martelo (o inativo "Martelo de borracha" não aparece).

### Exercício 2.9 — Página Detalhe (TL05) + widget `cartao-produto`

**Conceito:** `useParams`; renderização condicional por perfil (`ehAdmin`).
**Onde:** `widgets/cartao-produto/`, `pages/produto-detalhe/`
**Figma:** `3:40778` (Admin) e `3:40890` (Operador)

- [ ] `TituloPagina`: voltar + "Detalhe do produto" + selo ADMIN (só Admin). A seta preserva a busca de origem: o card envia o endereço em `state={{ de: location.pathname + location.search }}`, e o Detalhe volta para `location.state?.de ?? ROTAS.busca`.
- [ ] `CartaoProduto` (modo leitura): foto à esquerda (~30%); campos bloqueados NOME · ID (sku) · QTD (`texto-saldo` + "un.", cadeado) · VALOR BASE (`preco` pode ser `null`: mostra "—") · DESCRIÇÃO · LOCALIZAÇÃO (itens de `localizacoes` separados por " · "; lista vazia: "Sem localização") · ESTOQUE MÍNIMO ("10 un." ou "Padrão: 5 un." quando `estoqueMinimo` é `null`). 🤝 Estoque mínimo e foto dependem dos pontos D3 e D4 da seção 11.0. Os campos `categoria`, `fornecedor`, `marca`, `peso` e `tamanho` existem no back, mas não entram nesta tela porque o Figma não os prevê.
- [ ] Selo de situação ao lado da QTD quando Baixo/Zerado.
- [ ] Rodapé do card (só Admin): "✎ Editar" e "Inativar produto". Por enquanto sem ação (Bloco C).
- [ ] Barra de movimentações: "Registrar entrada" (primário, `Link` para `/produtos/:id/entrada`), "Registrar saída", "Ajustar por contagem", "Avisar que está em falta" (secundários; os três últimos ficam `disabled` até os Blocos B/C).
- [ ] Carregando: skeleton do card. 404: `EstadoVazio` "Produto não encontrado." + link para o Início.

**Pronto quando:** `/produtos/1` mostra o Martelo com "Qtd 2" e selo de alerta; como Ronaldo, não há selo ADMIN nem Editar/Inativar.

### Exercício 2.10 — Entidade `movimentacao` + widget `ultimas-movimentacoes`

**Conceito:** funções de domínio que montam **texto** a partir de dados (a frase do histórico, o "antes → depois").
**Onde:** `entities/movimentacao/`, `widgets/ultimas-movimentacoes/`

- [ ] `api/movimentacaoApi.js`: `listarMovimentacoes(filtros)` → `GET /movimentacoes`; `criarMovimentacao(dados)` → `POST /movimentacoes`, só para `ENTRADA` e `SAIDA`. O ajuste usa `PATCH /produtos/{id}/quantidade` (exercício 3.2).
- [ ] `config/motivos.js`: `MOTIVOS_SAIDA = [{ valor: 'PERDA', rotulo: 'Perda' }, { valor: 'AVARIA', rotulo: 'Avaria' }, { valor: 'USO_INTERNO', rotulo: 'Uso interno' }, { valor: 'VENDA_BALCAO', rotulo: 'Venda de balcão não lançada' }, { valor: 'OUTRO', rotulo: 'Outro' }]`, usado só no front (ponto D1 da seção 11.0). `ROTULOS_TIPO` (Saldo inicial, Entrada, Saída, Ajuste, Estorno) e `ROTULOS_ORIGEM` (Manual, Bling, Importação).
- [ ] `lib/calcular.js`: `calcularQuantidadeDepois(tipo, atual, quantidade)` (entrada soma, saída subtrai, ajuste substitui).
- [ ] `lib/frases.js`: `fraseConfirmacao(...)` → "Entrada de 10 un. de Martelo. A quantidade vai de 2 para 12." (variações de saída e ajuste conforme o brief TL09); `fraseHistorico(mov)` (usada no 3.3).
- [ ] `ui/SeloOrigem`.
- [ ] Widget `UltimasMovimentacoes` (recebe `produtoId`): título "Últimas movimentações" + link "Ver histórico completo" (só Admin, → `/historico?produtoId=`); tabela com Data e hora (`criadoEm`) · Tipo · Qtd. (`variacao`, com sinal) · Quantidade depois (`saldoPosterior`) · Quem · Origem · Justificativa ("—" se vazio); `limite=10`. Vazio: "Nenhuma movimentação registrada para este produto." Componente `Tabela` em `shared/ui` (cabeçalho em legenda caixa alta, zebra discreta, hover).
- [ ] Encaixar o widget no Detalhe.

**Pronto quando:** o Detalhe do "Parafuso sextavado 6 mm" (`/produtos/7`) mostra a entrada de +10 com "NF 12345" na justificativa, de Ronaldo. 🤝 Depende do ponto D1 e do mock atualizado.

### Exercício 2.11 — Feature `registrar-movimentacao` (base + confirmação TL09)

**Conceito:** separar o **hook de ação** (`useRegistrarMovimentacao`: estado `enviando`, erro, chamada à API) da **interface** (formulário e modal).
**Onde:** `features/registrar-movimentacao/` (`model/useRegistrarMovimentacao.js`, `ui/ModalConfirmarMovimentacao.jsx`, `ui/ResumoProduto.jsx`)
**Figma:** `3:41114` (entrada), `3:41166` (saída), `3:41226` (ajuste)

- [ ] `useRegistrarMovimentacao()` devolve `{ registrar(dados), enviando, erro, limparErro }`. `registrar` acrescenta `usuarioId` da sessão e chama `criarMovimentacao`.
- [ ] `ModalConfirmarMovimentacao`: título "Confirmar entrada?" / "Confirmar saída?" / "Confirmar ajuste?"; texto de `fraseConfirmacao`; botões "Voltar" e "Confirmar alteração" (carregando: "Salvando..."). Erro de rede: "Não foi possível registrar. Seus dados foram mantidos." + "Tentar de novo".
- [ ] `ResumoProduto`: "1020 – Parafuso sextavado 6 mm · quantidade atual 10 un." (somente leitura).
- [ ] Sucesso: `navigate('/produtos/:id')` + toast "Entrada registrada. Quantidade agora: 12 un." (saída: "Saída registrada...", ajuste: "Ajuste registrado...").
- [ ] Nada é gravado antes de "Confirmar alteração".

**Pronto quando:** o modal abre e fecha pelo teclado, e o texto antes → depois está certo para os três tipos (teste chamando o modal com dados fixos na `/vitrine`).

### Exercício 2.12 — Página Registrar entrada (TL06)

**Conceito:** **validação no cliente** antes de enviar: o erro aparece no campo, em linguagem da loja.
**Onde:** `features/registrar-movimentacao/` (`ui/FormEntrada.jsx`), `pages/registrar-entrada/`
**Figma:** `3:40993`

- [ ] Página: `TituloPagina` "Registrar entrada" com voltar; card central com `ResumoProduto` e o `FormEntrada`.
- [ ] Campos: "Quantidade recebida (obrigatório)" (número; ajuda "Informe a quantidade que você contou, na mesma unidade do produto (un.)."), "Nota fiscal ou fornecedor (opcional)" (placeholder "Ex.: NF 12345 ou Distribuidora X"), "Observação (opcional)".
- [ ] Validação: inteiro maior que zero → senão "Informe um número inteiro maior que zero.".
- [ ] "Registrar entrada" abre o modal de confirmação; "Cancelar" volta ao Detalhe. Os dados digitados permanecem se o envio falhar.
- [ ] Campos enviados: `{ produtoId, usuarioId, tipo: 'ENTRADA', quantidade, justificativa }`. 🤝 Proposta D1: `justificativa` junta os dois campos da tela, por exemplo `"NF 12345 — Caixa amassada"`, e fica `null` com os dois vazios.

**Pronto quando:** a jornada inteira funciona com mock: Login (Ronaldo) → buscar "martelo" → Detalhe → Registrar entrada de 10 → Confirmar → Detalhe com "Qtd 12", toast correto e a nova linha em "Últimas movimentações".

### 🛑 Parada do back P1

Antes de integrar o Bloco A, o Spring precisa ter (detalhes na seção 11.0 e no Apêndice A):

- [ ] Pontos D1 a D7 da seção 11.0 acordados
- [ ] `@ControllerAdvice` devolvendo erros no formato `{ status, mensagem, erros }` (A.1)
- [ ] `POST /auth/login` (A.2)
- [ ] `estoqueMinimo`, `estoqueMinimoEfetivo` e `fotoUrl` em `Produto` (A.3)
- [ ] Tabela `configuracoes` + `GET /configuracoes` (A.6), porque o Início lê `exibirFotos` e o mínimo efetivo depende do mínimo global
- [ ] `POST /movimentacoes` e `GET /movimentacoes` com todos os filtros e `limite` (A.4), sobre a entidade `MovimentacaoEstoque` que já existe
- [ ] Swagger com descrição, exemplo e respostas de cada endpoint novo

### 🔌 Integração A

- [ ] Backend no ar (`docker compose up --build`) e Swagger abrindo
- [ ] `.env.local`: `ROTAS_NO_SPRING=auth,produtos,movimentacoes,configuracoes`
- [ ] Login com usuário do banco funciona, com as contas do seed do back (`ana.souza@portalblox.test` e `bruno.lima@portalblox.test`, senha `senha123`); senha errada mostra a mensagem certa
- [ ] Início mostra o baixo estoque calculado com dados do banco
- [ ] Busca, Detalhe e Últimas movimentações vêm do banco
- [ ] Registrar entrada grava no MySQL (confira no Swagger: `GET /movimentacoes?produtoId=`)
- [ ] Anotar aqui qualquer diferença de contrato encontrada e como foi resolvida

---

## 7. Etapa 3 — Bloco B: completa o fluxo + Dashboard

### Exercício 3.1 — Registrar saída (TL07)

**Conceito:** validação **enquanto digita** e componente de **grupo de opções** acessível (setas do teclado).
**Onde:** `shared/ui/grupo-opcoes/`, `features/registrar-movimentacao/` (`ui/FormSaida.jsx`), `pages/registrar-saida/`
**Figma:** `3:41032`

- [ ] `GrupoOpcoes` (radio): `<fieldset>` + `<legend>`; cada opção é uma linha grande inteira clicável (≥ 44 px) com `<input type="radio">` nativo (as setas funcionam sozinhas).
- [ ] Campos: "Quantidade (obrigatório)"; "Motivo (obrigatório)" com `MOTIVOS_SAIDA`; "Descreva o motivo" aparece só com "Outro" (aí é obrigatório).
- [ ] Quantidade maior que o saldo: erro "Não dá para tirar 5 un.: a quantidade atual é 2 un. Se o número na prateleira for outro, faça um ajuste por contagem." + link "Ajustar por contagem" (→ `/produtos/:id/ajuste`); botão desabilitado enquanto o erro existir.
- [ ] Se o back responder 422 (saldo mudou nesse meio-tempo): fechar o modal e mostrar a mensagem do back no campo.
- [ ] Habilitar o botão "Registrar saída" no Detalhe.
- [ ] Envio: `{ produtoId, usuarioId, tipo: 'SAIDA', quantidade, justificativa }`. 🤝 Proposta D1: `justificativa` recebe o rótulo do motivo; com "Outro", recebe `"Outro: <texto digitado>"`.

**Pronto quando:** no Martelo (2 un.), digitar 5 mostra o erro com o link; tirar 1 por "Avaria" leva a "Saída registrada. Quantidade agora: 1 un.".

### Exercício 3.2 — Ajustar por contagem (TL08)

**Conceito:** valor **calculado no render** (a diferença), sem `useState` extra.
**Onde:** `features/registrar-movimentacao/` (`ui/FormAjuste.jsx`), `pages/ajustar-contagem/`
**Figma:** `3:41079`

- [ ] "Quantidade no sistema: 2 un." (negativo em `--cor-erro`).
- [ ] "Quantidade contada na prateleira (obrigatório)" (inteiro ≥ 0).
- [ ] "Diferença: +8 un." em destaque: `--cor-sucesso` se positiva, `--cor-erro` se negativa, "Sem diferença" se zero; sempre com sinal e texto.
- [ ] "Justificativa (obrigatório)", placeholder "Ex.: contagem da prateleira C3-P2 na quarta-feira"; mínimo 10 caracteres → "Escreva uma justificativa com pelo menos 10 caracteres.".
- [ ] Botão "Registrar ajuste" → modal → toast "Ajuste registrado. Quantidade agora: N un.".
- [ ] Envio: `PATCH /produtos/{id}/quantidade` com `{ quantidade: <contada>, usuarioId, justificativa }`. O mínimo de 10 caracteres é regra do front, porque o back só exige texto não vazio. Com "Sem diferença", o back responde 200 e não grava movimentação. A resposta é o `Produto`, e o toast usa a `quantidade` dele. 🤝 Ponto D5.
- [ ] Habilitar "Ajustar por contagem" no Detalhe.

**Pronto quando:** no Serrote (0 un.), contar 6 mostra "Diferença: +6 un." em verde e, confirmado, o Detalhe mostra 6 un.

### Exercício 3.3 — Histórico (TL11)

**Conceito:** **filtros na URL** (persistem ao voltar); "carregar mais" pedindo um `limite` maior ao back.
**Onde:** `pages/historico/` (filtros em `pages/historico/ui/FiltrosHistorico.jsx`); entidade `usuario` (`api/usuarioApi.js` com `listarUsuarios()`); `Avatar` de `shared/ui`
**Figma:** `3:41512`

- [ ] `TituloPagina` "Histórico" + ADMIN.
- [ ] Filtros em linha: Produto (select com produtos), Usuário (select com usuários), Período (Hoje · Últimos 7 dias · Últimos 30 dias · Tudo → vira `de`/`ate`), Tipo (Entrada/Saída/Ajuste). Cada mudança atualiza a URL.
- [ ] Lista: avatar + frase + "data · hora" à direita. Frases (`fraseHistorico`): "Ronaldo adicionou +10 un. de Martelo" · "Valdir removeu −1 un. de Serrote (avaria)" · "Márcia ajustou Martelo para 6 un.". Nome do produto é `Link` sublinhado para o Detalhe.
- [ ] `fraseHistorico` também cobre `SALDO_INICIAL` ("Saldo inicial de 10 un. de Martelo") e `ESTORNO` ("Ronaldo estornou −1 un. de Serrote"), que vêm do back. O "(avaria)" da frase de saída sai da `justificativa`.
- [ ] 25 por vez; botão "Carregar mais" pede `limite=50`, `75`... e some quando vier menos que o pedido.
- [ ] Vazio: "Nenhuma movimentação encontrada.".

**Pronto quando:** filtrar por Ronaldo + Últimos 7 dias, abrir um produto e voltar mantém os filtros; "Ver histórico completo" no Detalhe abre o histórico já filtrado.

### Exercício 3.4 — Usuários (TL18)

**Conceito:** CRUD completo numa tela: **listar, criar, editar, desativar**; formulário reaproveitado para criar e editar.
**Onde:** entidade `usuario` (`api`: `criarUsuario`, `atualizarUsuario`, `desativarUsuario`, `listarPerfis`), `features/gerenciar-usuario/` (`ui/PainelUsuario.jsx`, `ui/BotaoDesativarUsuario.jsx`), `pages/usuarios/`
**Figma:** `3:42022` e `3:42126` (painel aberto)

- [ ] `TituloPagina` "Usuários" + ADMIN + botão primário "Cadastrar usuário".
- [ ] Tabela (`GET /usuarios?incluirInativos=true`): Nome · E-mail · Perfil · Situação ("Ativo" / "Desativado", com texto) · Ações ("Editar", "Desativar").
- [ ] `PainelUsuario` (na `Gaveta` à direita): Nome, E-mail, Perfil (`GrupoOpcoes` Operador/Administrador, ids de `GET /perfis-acesso`) + "Salvar".
- [ ] No cadastro, o front gera uma **senha provisória** de 8 caracteres (`gerarSenhaProvisoria()` em `features/gerenciar-usuario/lib`), envia no `POST` e mostra uma vez numa `Faixa`: "Usuário cadastrado. Senha provisória: Ab3k9xQ2. Anote e entregue ao funcionário.".
- [ ] Erro 409 → no campo E-mail: "Já existe um usuário com este e-mail.".
- [ ] "Desativar" → `Modal` "Desativar Ronaldo?" / "Ele não consegue mais entrar. O nome continua no histórico." / "Cancelar" + "Desativar" (destrutivo). 422 do último Admin → toast de erro "Não é possível desativar o último administrador ativo.".

**Pronto quando:** cadastrar "Ana" (Operador) mostra a senha provisória, e entrar como Ana com essa senha funciona; desativar Ana a impede de entrar.

### Exercício 3.5 — Dashboard (tela nova, sem Figma)

**Conceito:** biblioteca de gráfico (Recharts) com dados da API; **acessibilidade de gráfico** (tabela equivalente).
**Onde:** `pages/dashboard/` (`api/dashboardApi.js` com `buscarResumo()` → `GET /dashboard/resumo`; `ui/DashboardPage.jsx`, `ui/CardIndicador.jsx`, `ui/GraficoEntradasSaidas.jsx`, `ui/ListaMaisMovimentados.jsx`). Os dados só são usados aqui, por isso o `api/` mora na própria page.
**Figma:** nenhum. Seguir o padrão visual das telas Admin (TL13 `3:41655`, TL11 `3:41512`) e os tokens.

**Especificação**

| Área | Conteúdo |
|---|---|
| Título | `TituloPagina` "Dashboard" + selo ADMIN; legenda "Atualizado às 14:32" (hora da resposta) + botão secundário "Atualizar" |
| Linha de indicadores (4 cards, 4 colunas ≥ 1024 px, 2 colunas abaixo, 1 coluna em 360 px) | "Produtos ativos" · "Com estoque baixo" (ícone ⚠, número em `--cor-alerta`) · "Zerados" (número em `--cor-erro`) · "Movimentações hoje". Número em `texto-saldo`, rótulo em `texto-legenda` |
| Card "Entradas e saídas — últimos 7 dias" | `BarChart` agrupado: entradas em `--cor-primaria`, saídas em `--cor-borda`; eixo X com dia/mês ("31/10"); legenda com texto; tooltip "Entradas: +10 un." / "Saídas: −3 un."; abaixo, `<details>` "Ver dados em tabela" com a mesma informação |
| Card "Mais movimentados" | Lista de até 5: "1001 – Martelo" + total em un.; nome é `Link` para o Detalhe |
| Estados | Carregando (skeletons nos cards), erro (mensagem + "Tentar de novo"), sem movimentações ("Nenhuma movimentação nos últimos 7 dias.") |

- [ ] Recharts lendo as cores dos tokens: `getComputedStyle(document.documentElement).getPropertyValue('--cor-primaria')` (o SVG não lê `var()` em todas as props).
- [ ] `ResponsiveContainer` com altura fixa de 280 px.
- [ ] Item "Dashboard" já existe no menu (exercício 2.4).

**Pronto quando:** o Dashboard mostra os 4 números batendo com o Início (ex.: "Zerados" = quantidade de cards com "Qtd 0"), e registrar uma entrada e clicar em "Atualizar" muda o gráfico de hoje.

### 🛑 Parada do back P2

- [ ] `GET /perfis-acesso` (A.7)
- [ ] `PUT /usuarios/{id}` sem exigir `senha` (A.7, hoje o DTO exige)
- [ ] Regra "não desativar o último Admin" → 422 (A.7)
- [ ] `GET /dashboard/resumo` (A.8)

### 🔌 Integração B

- [ ] `.env.local`: `ROTAS_NO_SPRING=auth,produtos,movimentacoes,configuracoes,usuarios,perfis-acesso,dashboard`
- [ ] Saída com saldo insuficiente recebe 422 do Spring e mostra a mensagem
- [ ] Ajuste grava e aparece no Histórico com a frase certa
- [ ] Histórico filtra pelo back
- [ ] CRUD de usuários contra o banco; senha provisória funciona no login
- [ ] Dashboard com números do banco

---

## 8. Etapa 4 — Bloco C: acabamento

> **Antes de começar o bloco:** tire `produtos`, `usuarios` e `configuracoes` do `ROTAS_NO_SPRING` (ou deixe a lista vazia). Os endpoints novos deste bloco ainda não existem no Spring e têm o mesmo prefixo dos que já existem (seção 4). Recoloque na Integração C.

### Exercício 4.1 — Avisar falta (TL10) + widget `avisos-falta` + resolver

**Conceito:** um widget que **reage a uma ação** de outra parte da tela (recarregar a lista depois de resolver).
**Onde:** entidade `aviso-falta` (`api`: `listarAvisos`, `criarAviso`, `resolverAviso`), `features/avisar-falta/` (`ui/ModalAvisarFalta.jsx`), `features/resolver-aviso/` (`ui/BotaoResolverAviso.jsx`), `widgets/avisos-falta/`
**Figma:** `3:41399`

- [ ] Modal "Avisar que está em falta": produto (código – nome), "Observação (opcional)" (placeholder "Ex.: acabou na prateleira, cliente pediu 3"), "Cancelar" e "Enviar aviso".
- [ ] Toast: "Aviso enviado ao responsável." ou, se `somado: true`, "Já existe um aviso aberto para este produto. Seu aviso foi somado.".
- [ ] Habilitar "Avisar que está em falta" no Detalhe.
- [ ] Widget no Início, abaixo do baixo estoque: título "Avisos de falta"; cada linha "1543 – Fita veda-rosca 18 mm · avisado por Valdir, hoje 10:15 · "acabou na prateleira"" e "+1 pessoa avisou" quando `quantidadeAvisos > 1`; nome do produto leva ao Detalhe.
- [ ] Só Admin: botão "Marcar como resolvido" → `PATCH` → linha some + toast "Aviso marcado como resolvido.".
- [ ] Vazio: "Nenhum aviso de falta aberto.".

**Pronto quando:** Valdir avisa falta do Martelo; Márcia vê o aviso no Início e o resolve.

### Exercício 4.2 — Editar produto (TL12) e Inativar (TL09)

**Conceito:** o **mesmo componente** em dois modos (leitura/edição), controlado por um estado da página.
**Onde:** `features/editar-produto/` (`ui/FormEdicaoProduto.jsx`), `features/inativar-produto/` (`ui/BotaoInativarProduto.jsx`), `widgets/cartao-produto/`, entidade `produto` (`atualizarDadosLocais`, `inativarProduto`)
**Figma:** `3:41581` (edição) e `3:41274` (inativar)

- [ ] "✎ Editar" troca o `CartaoProduto` para edição: DESCRIÇÃO, LOCALIZAÇÕES (lista de textos, com placeholder "Ex.: C3-P2", converte para maiúsculas; máx. 120 caracteres por item, regra do back), ESTOQUE MÍNIMO (ajuda "Deixe vazio para usar o padrão de Configurações"; vazio → `null`), URL DA FOTO (opcional). NOME, ID, VALOR BASE com cadeado "Vem do Bling"; QTD com "Muda só por movimentação". 🤝 Estoque mínimo e foto dependem de D3 e D4.
- [ ] "Cancelar" descarta; "Salvar alterações" → `PUT /produtos/{id}` → toast "Alterações salvas." e volta à leitura com os dados novos. O `PUT` exige `sku`, `nome` e `categoriaId` e **limpa o que não for enviado**: mande sempre todos os campos do produto, inclusive `fornecedorId`, `localizacoes`, `peso`, `tamanho`, `preco` e `marca`.
- [ ] "Inativar produto" → `Modal` "Inativar Martelo?" / "O produto sai da busca. O histórico de movimentações é mantido." / "Cancelar" + "Inativar produto" (destrutivo) → `DELETE` → Início + toast "Produto inativado. O histórico foi mantido.".

**Pronto quando:** definir mínimo 3 para o Martelo e salvar muda o "ESTOQUE MÍNIMO" e a situação; inativar a Chave de fenda a tira da busca.

### Exercício 4.3 — Configurações (TL13)

**Onde:** entidade `configuracao` (`salvarConfiguracoes`), `features/salvar-configuracoes/`, `pages/configuracoes/`, `shared/ui/caixa-marcar/`
**Figma:** `3:41655`

- [ ] Card em lista: "Quantidade mínima de produtos" + ajuda ("Abaixo desse valor o produto aparece em "baixo estoque". Vale para produtos sem mínimo próprio.") + campo numérico; "N produtos com mínimo próprio · os demais usam o mínimo global"; "Exibir foto dos produtos" + `CaixaMarcar` (área 44 × 44).
- [ ] Botão "Salvar configurações" → toast "Configuração salva.". Valor inválido → erro no campo.
- [ ] Nota: "O mínimo próprio de um produto é definido em Editar, no detalhe do produto."

**Pronto quando:** desmarcar "Exibir foto" some com as fotos do Início e da Busca; mudar o mínimo global para 3 muda a lista de baixo estoque.

### Exercício 4.4 — Meu perfil (TL19)

**Onde:** `features/alterar-senha/`, `pages/meu-perfil/`, entidade `usuario` (`alterarSenha`)
**Figma:** `3:42247`

- [ ] Card de dados (Nome, E-mail, Perfil) somente leitura, vindo da sessão.
- [ ] Card "Alterar senha": Senha atual, Nova senha (ajuda "Mínimo de 8 caracteres"), Repetir nova senha + "Alterar senha".
- [ ] Senhas diferentes: erro no front ("As senhas não são iguais."); senha atual errada: erro de campo vindo do back.
- [ ] Sucesso: toast "Senha alterada." e limpa os campos.

**Pronto quando:** trocar a senha da Márcia e entrar de novo com a nova.

### 🛑 Parada do back P3 e 🔌 Integração C

- [ ] Back: avisos de falta (A.5), `PUT /configuracoes` (A.6), `PATCH /usuarios/{id}/senha` (A.7). A edição do produto usa o `PUT /produtos/{id}` que já existe.
- [ ] `.env.local`: recolocar `produtos`, `usuarios` e `configuracoes` e acrescentar `avisos-falta`
- [ ] Testar os quatro exercícios contra o banco

---

## 9. Etapa 5 — Bloco D: prévia das telas do Bling

> Estas telas **só usam o mock** (não há Bling nesta fase). Elas navegam e mostram dados, mas toda ação que falaria com o Bling fica **desabilitada**. Toda tela do bloco começa com a `Faixa` informativa: **"Prévia: a integração com o Bling ainda não está ativada. Os dados desta tela são de exemplo."**

### Exercício 5.1 — Divergências e negativos (TL14)

**Onde:** entidade `divergencia` (`listarDivergencias`, `buscarDivergencia`), `pages/divergencias/`
**Figma:** `3:41693`

- [ ] Texto: "Produtos com quantidade negativa ou diferente do Bling. Resolva do maior para o menor impacto."
- [ ] Filtros como etiquetas: "Negativos" · "Diferentes do Bling" · "Não encontrados no Bling" (ativo em `--cor-primaria-suave`).
- [ ] Tabela: Produto · Quantidade no Portal · Quantidade no Bling · Diferença (com sinal, `--cor-erro`) · Aberta há ("há 4 dias") · "Resolver" (→ `/divergencias/:id`).
- [ ] Contador de divergências no item do menu (o `Cabecalho` busca `GET /divergencias` só para Admin).

### Exercício 5.2 — Resolver divergência (TL15)

**Onde:** `pages/resolver-divergencia/`
**Figma:** `3:41750`

- [ ] Dois blocos lado a lado: "No Portal: 12 un." · "No Bling: −3 un." (`texto-saldo`, negativo em `--cor-erro`).
- [ ] `GrupoOpcoes`: "O Bling está certo (ajustar o Portal para −3)" · "O Portal está certo (enviar 12 ao Bling)" · "Contei agora" (mostra campo de quantidade).
- [ ] "Justificativa (obrigatório)".
- [ ] "Resolver divergência" **desabilitado**, com ajuda "Disponível quando a integração com o Bling for ativada."; "Cancelar" volta.

### Exercício 5.3 — Importar produtos (TL16)

**Conceito:** **máquina de estados simples** com `useState` (`passo: 'enviar' | 'previa' | 'resultado'`).
**Onde:** `shared/ui/stepper/`, `pages/importar-produtos/`
**Figma:** `3:41800`, `3:41839`, `3:41901`

- [ ] `Stepper`: Enviar arquivo → Conferir prévia → Resultado (atual em `--cor-primaria`, concluídos com ✓).
- [ ] Passo 1: "Exporte a planilha de produtos no Bling (CSV ou XLSX)."; área de arrastar (borda tracejada `--cor-borda`) com `<input type="file" accept=".csv,.xlsx">`; link "Baixar modelo de planilha" (desabilitado); "Ver prévia" habilita quando há arquivo. Arquivo com outra extensão: "Não conseguimos ler este arquivo. Use CSV ou XLSX exportado do Bling.".
- [ ] Passo 2: tabela com `GET /importacao/previa` (Código · Nome · Unidade · Preço · Quantidade · Localização) + "Confirmar importação" (simula 1,5 s de barra de progresso).
- [ ] Passo 3: "Simulação concluída: 3 produtos lidos. Nenhum dado foi gravado." + link "Ver produtos" (→ `/busca`).

### Exercício 5.4 — Integração com o Bling (TL17)

**Onde:** entidade `integracao` (`buscarEstado`, `listarEventos`), `pages/integracao-bling/`
**Figma:** `3:41935`

- [ ] Card de estado: "● Sem conexão desde 14:32" (`--cor-erro`, sempre com texto) ou "● Conectado"; "Estágio atual: Sombra (só leitura)"; "Última sincronização: hoje, 14:32"; "Última conciliação: hoje, 06:00"; "Eventos aguardando: 0 · Eventos com erro: 2".
- [ ] "Conciliar agora" desabilitado; "Ver divergências" → `/divergencias`.
- [ ] Faixa de alerta quando sem conexão: "Sem conexão com o Bling desde 14:32. O Portal continua funcionando normalmente."
- [ ] Tabela de log: Data e hora · Evento · Situação · Detalhe; "Tentar de novo" desabilitado nas linhas com erro.

**Pronto quando (bloco D):** como Márcia, dá para percorrer Divergências → Resolver → voltar, os 3 passos da Importação e a tela de Integração, sem erro no console e com a faixa de prévia em todas.

---

## 10. Etapa 6 — Fechamento e apresentação

### Exercício 6.1 — Revisão de acessibilidade

Use a seção "Acessibilidade (WCAG 2.1 AA)" do brief como checklist. No mínimo:

- [ ] Fazer a jornada do Bloco A **só com teclado**
- [ ] Todo `<img>` com `alt`; todo `BotaoIcone` com `rotulo`
- [ ] Nenhum estado só por cor (selos, diferença, situação)
- [ ] Zoom de 200% sem rolagem horizontal em 1366 px
- [ ] `lang="pt-BR"` e títulos de aba corretos

### Exercício 6.2 — Responsividade

- [ ] 1366 px (referência), 1024 px (sem rolagem horizontal) e 360 px (coluna única) em: Login, Início, Busca, Detalhe, formulários de movimentação
- [ ] Telas "somente desktop" (TL14–TL18): sem quebra a partir de 1024 px

### Exercício 6.3 — Limpeza e qualidade

- [ ] Apagar a página `/vitrine`
- [ ] `npm run lint` e `npm run build` sem erros; nenhum `console.log` esquecido
- [ ] ⭐ Rodar o linter oficial do FSD, `npx steiger src`, e corrigir as violações de camada apontadas
- [ ] README atualizado com o estado de cada rota (mock ou Spring)

### Exercício 6.4 — Roteiro da demonstração (5 min)

- [ ] `npm run mock:reset` e banco com o script de exemplo
- [ ] 1. Login como Ronaldo (Operador) → busca "martelo" → Detalhe → Registrar entrada → toast
- [ ] 2. Registrar saída com saldo insuficiente (mostra a regra) → Ajustar por contagem
- [ ] 3. Sair → login como Márcia (Admin) → menu com itens ADMIN → Histórico mostrando o que Ronaldo fez
- [ ] 4. Dashboard (refletindo as movimentações da demo) → Usuários (cadastrar alguém)
- [ ] 5. (Se pronto) Prévia do Bling: Divergências → Integração
- [ ] Mostrar o Swagger e a árvore FSD no VS Code (itens da avaliação)

---

## 11. O que falta no backend

Análise do back na `origin/main` (commit `0c7abe2`, 01/11/2026, schema v3) comparada com o que as telas precisam. Substitui a análise de 22/10.

### 11.0 Acordo de contrato entre front e back (02/11/2026)

**Regra do grupo:**
- **Diferença** (o back já tem, de outro jeito): vale o back codado. Front e mock se adaptam.
- **Falta** (o back ainda não tem): o back implementa, seguindo o Apêndice A.

Convenção do documento: 🤝 indica algo que depende de um ponto abaixo.

**Pontos a acordar:**

| # | Assunto | Situação no back | Proposta | Quem decide | Acordado? |
|---|---|---|---|---|---|
| D1 | Nota fiscal, motivo e observação | `MovimentacaoEstoque` só tem `justificativa` | Sem colunas novas: o front monta o texto. Entrada: `"NF 12345 — observação"`. Saída: rótulo do motivo. Consequência: não dá para filtrar o histórico por motivo nesta sprint | Back + front | [ ] |
| D2 | JSON de `Movimentacao` | Só a entidade existe, sem endpoint | Usar os nomes da entidade, como em A.4 | Back | [ ] |
| D3 | Estoque mínimo | Ausente | `estoqueMinimo` (pode ser `null`) e `estoqueMinimoEfetivo` calculado, como em A.3. Sem isso não há baixo estoque, selo nem indicador do Dashboard | Back | [ ] |
| D4 | Foto | Ausente | `fotoUrl` (pode ser `null`). Se não der tempo, o front trata a ausência como "sem foto" | Back | [ ] |
| D5 | Ajuste por contagem | `PATCH /produtos/{id}/quantidade` já grava `AJUSTE` | O front usa esse `PATCH`. `POST /movimentacoes` aceita só `ENTRADA` e `SAIDA` | Back | [ ] |
| D6 | Saída maior que o saldo | Regra ainda não existe | `POST` responde 422 com a mensagem de A.4 | Back | [ ] |
| D7 | Contas de teste | Seed com Ana, Bruno e Carla (`senha123`). O mock usa Márcia, Carlos, Ronaldo e Valdir (`123456`) | Mock e back podem ter contas diferentes. Na demonstração, usar as contas do seed do back e ajustar o roteiro do exercício 6.4 | Grupo | [ ] |

**Do lado do front:** mock e seed (Apêndices B e C) já atualizados para o formato de produto. Falta o formato de movimentação, depois de D1 e D2.

### 11.1 O que já existe

| Recurso | Endpoints | Observação |
|---|---|---|
| Produtos | `GET /produtos` (`?incluirInativos`), `GET /produtos/{id}`, `POST`, `PUT /{id}`, `PATCH /{id}/quantidade`, `DELETE /{id}` (inativa) | Categoria única, fornecedor e lista de localizações. `POST` exige `usuarioId`. `PUT` nunca altera saldo |
| Usuários | `GET /usuarios` (`?incluirInativos`), `GET /{id}`, `POST`, `PUT /{id}` (sem senha), `DELETE /{id}` (inativa) | Senha em texto puro e nunca nas respostas |
| Fornecedores | `GET /fornecedores` (`?incluirInativos`), `GET /{id}`, `POST`, `PUT /{id}`, `DELETE /{id}` (inativa) | Novo. Nenhuma tela usa nesta sprint |
| Movimentações | Só a entidade `MovimentacaoEstoque` | Registro imutável, com `saldoAnterior`, `saldoPosterior` e `variacao`. Tipos: `SALDO_INICIAL`, `AJUSTE`, `ENTRADA`, `SAIDA`, `ESTORNO`. Origens: `MANUAL`, `BLING`, `IMPORTACAO` |
| Erros | Exceções de domínio com `@ResponseStatus` (400, 404, 409, 422) | Sem o corpo padronizado de A.1 |
| Infra | Docker Compose (MySQL 8.4 + Spring na 8080), Swagger (SpringDoc), seed em `database/zz-seed-demo.sql` | Sem CORS (resolvido pelo proxy do Vite, não precisa mexer) |

### 11.2 O que falta, por prioridade

| # | Item | Telas que dependem | Parada | Detalhe |
|---|---|---|---|---|
| 1 | `@ControllerAdvice` com erro padronizado `{ status, mensagem, erros }` | Todas | P1 | Hoje os erros saem no formato padrão do Spring, sem `mensagem` nem `erros[].campo`, e o front cairia sempre na mensagem genérica |
| 2 | `POST /auth/login` | TL01 | P1 | Confere e-mail + senha de usuário ativo. **Sem Spring Security/JWT nesta fase** (decisão de POC; registrar no backlog) |
| 3 | `estoqueMinimo`, `estoqueMinimoEfetivo` e `fotoUrl` em `Produto` | TL03, TL04, TL05, TL12 | P1 | Pontos D3 e D4. Com `ddl-auto=validate`, alterar o script SQL **e** a entidade juntos |
| 4 | Tabela `configuracoes` (linha única) + `GET /configuracoes` | TL03, TL13 | P1 | O mínimo efetivo depende do mínimo global |
| 5 | `POST /movimentacoes` + `GET /movimentacoes` com filtros e `limite` | TL05, TL06, TL07, TL11, Dashboard | P1 | Pontos D1, D2, D5 e D6. A entidade já existe |
| 6 | `GET /perfis-acesso` | TL18 | P2 | |
| 7 | 422 ao inativar o último Admin | TL18 | P2 | |
| 8 | `GET /dashboard/resumo` | Dashboard | P2 | Consultas agregadas (pode ser JPQL ou cálculo no service) |
| 9 | `avisos_falta` (tabela + entidade + `GET`, `POST`, `PATCH /{id}/resolver`) | TL03, TL10 | P3 | `@ManyToOne` com `Produto` e `Usuario`; um aviso aberto por produto, o segundo soma |
| 10 | `PUT /configuracoes` | TL13 | P3 | |
| 11 | `PATCH /usuarios/{id}/senha` | TL19 (e redefinir senha na TL18, ⭐) | P3 | Com `senhaAtual`: confere; sem: redefinição pelo Admin |
| 12 | Swagger completo (descrição, exemplo, respostas) em todos os endpoints novos | — | Sempre | Item avaliado |

### 11.3 Observações

- **Saldo:** resolvido no schema v3; `produtos.quantidade` é o saldo único.
- **`PATCH /produtos/{id}/quantidade`:** deixou de ser uma porta dos fundos, porque agora grava `AJUSTE` auditado com `usuarioId` e `justificativa`.
- **Senha em texto puro:** aceitável para POC, mas registrar no backlog (BCrypt na próxima sprint).
- **Movimentos vindos do Bling** ficam para a fase com Bling. Nesta fase, as telas gravam só `origem = MANUAL`.

---

## Apêndice A — Contrato da API

Base: o front chama `/api/<rota>`; o proxy remove o `/api`. Datas em ISO local sem fuso (`"2026-10-31T14:32:00"`, padrão do `LocalDateTime`). Dinheiro como número (`34.9`). **Revisado em 02/11/2026 com o back do schema v3:** o que já existe segue o código; o que falta está marcado e depende da seção 11.0. O mock (Apêndice B) já segue o formato de produto; movimentação continua no formato anterior até D1 e D2.

### A.1 Formato de erro (todas as rotas)

⏳ **Falta no back** (11.2, item 1). Os status abaixo já são os das exceções atuais, mas o corpo ainda é o padrão do Spring.

```json
{ "status": 400, "mensagem": "Confira os campos destacados.", "erros": [ { "campo": "quantidade", "mensagem": "Informe um número inteiro maior que zero." } ] }
```

| Status | Quando |
|---|---|
| 400 | Payload inválido (com `erros` por campo) |
| 401 | Login inválido: `"Usuário ou senha incorretos."` |
| 404 | Recurso não encontrado |
| 409 | Duplicado (e-mail, SKU) |
| 422 | Regra de negócio (saldo insuficiente, já inativo, último Admin) |

### A.2 Autenticação

⏳ **Falta no back** (11.2, item 2). O formato de `Usuario` abaixo já é o do back.

| Método e rota | Corpo | Resposta |
|---|---|---|
| `POST /auth/login` | `{ "email", "senha" }` | 200 `Usuario` · 401 |

`Usuario` = `{ "id", "nome", "email", "perfilAcesso": { "id", "nome" }, "ativo" }`. A senha **nunca** volta.

### A.3 Produtos

| Método e rota | Corpo | Resposta |
|---|---|---|
| `GET /produtos` (`?incluirInativos=true`) | — | 200 `Produto[]` (só ativos por padrão) |
| `GET /produtos/{id}` | — | 200 `Produto` · 404 |
| `POST /produtos` | `{ "sku", "nome", "categoriaId", "quantidade", "usuarioId" }` + opcionais `peso`, `tamanho`, `descricao`, `preco`, `marca`, `fornecedorId`, `localizacoes` | 201 `Produto` · 400 · 404 · 409 |
| `PUT /produtos/{id}` | `{ "sku", "nome", "categoriaId" }` + os mesmos opcionais (e ⏳ `estoqueMinimo` e `fotoUrl`, D3 e D4), **sem** `quantidade` nem `usuarioId` | 200 `Produto` · 400 · 404 · 409 · 422 (inativo) |
| `PATCH /produtos/{id}/quantidade` | `{ "quantidade", "usuarioId", "justificativa" }` (valor absoluto, grava `AJUSTE`; sem mudança de valor, 200 sem movimentação) | 200 `Produto` · 400 · 404 · 422 (inativo) |
| `DELETE /produtos/{id}` | — | 204 · 404 · 422 (já inativo) |

No `PUT`, lista de localizações ausente ou `null` limpa as localizações, e `fornecedorId` nulo remove o vínculo.

```json
{ "id": 1, "sku": "1001", "nome": "Martelo", "peso": 0.5, "tamanho": "27 mm",
  "descricao": "Martelo de unha 27 mm, cabo de madeira.", "preco": 34.9, "marca": "Tramontina",
  "quantidade": 2, "ativo": true,
  "categoria": { "id": 1, "nome": "Ferragens" },
  "fornecedor": { "id": 1, "nome": "Ferragens Silva", "cnpj": "12345678000199" },
  "localizacoes": [ "C3-P2" ],
  "estoqueMinimo": 10, "estoqueMinimoEfetivo": 10, "fotoUrl": null }
```

- `peso`, `tamanho`, `descricao`, `preco`, `marca` e `fornecedor` podem ser `null`; `localizacoes` pode ser vazia. Os valores acima são só um exemplo.
- ⏳ `estoqueMinimo`, `estoqueMinimoEfetivo` e `fotoUrl` **faltam no back** (D3 e D4).
- `estoqueMinimoEfetivo = estoqueMinimo ?? configuracoes.estoqueMinimoGlobal` (calculado pelo back, não é coluna). A **situação** (OK/Baixo/Zerado) é calculada no front (exercício 2.5).

### A.4 Movimentações

⏳ **Falta no back:** só a entidade existe. O formato abaixo é a proposta D2, com os nomes da entidade `MovimentacaoEstoque`.

| Método e rota | Corpo | Resposta |
|---|---|---|
| `GET /movimentacoes?produtoId=&usuarioId=&tipo=&de=AAAA-MM-DD&ate=AAAA-MM-DD&limite=` | — | 200 `Movimentacao[]`, mais recente primeiro; todos os filtros opcionais |
| `POST /movimentacoes` | ver abaixo | 201 `Movimentacao` · 400 · 404 · 422 |

```json
// POST — só ENTRADA e SAIDA; "quantidade" é o quanto entra ou sai (> 0). O ajuste usa PATCH /produtos/{id}/quantidade (A.3).
{ "produtoId": 1, "usuarioId": 3, "tipo": "SAIDA", "quantidade": 1, "justificativa": "Avaria" }
```

- `justificativa`: opcional em `ENTRADA` e obrigatória em `SAIDA` (proposta D1: o front monta o texto)
- `SAIDA` maior que o saldo → **422** `"Não dá para tirar 5 un.: a quantidade atual é 2 un."` (D6)
- `usuarioId` vai no corpo porque ainda não há token (POC)

```json
// Resposta (Movimentacao): "quantidade" é sempre positiva; "variacao" tem sinal
{ "id": 5, "tipo": "SAIDA", "quantidade": 1, "variacao": -1, "saldoAnterior": 2, "saldoPosterior": 1,
  "justificativa": "Avaria", "origem": "MANUAL", "criadoEm": "2026-10-31T15:52:32",
  "produto": { "id": 1, "sku": "1001", "nome": "Martelo" },
  "usuario": { "id": 3, "nome": "Ronaldo" } }
```

- `tipo`: `SALDO_INICIAL` · `AJUSTE` · `ENTRADA` · `SAIDA` · `ESTORNO`
- `origem`: `MANUAL` · `BLING` · `IMPORTACAO`. As telas desta fase gravam só `MANUAL`.

### A.5 Avisos de falta

⏳ **Falta no back** (11.2, item 9).

| Método e rota | Corpo | Resposta |
|---|---|---|
| `GET /avisos-falta?status=ABERTO` | — | 200 `AvisoFalta[]`, mais recente primeiro |
| `POST /avisos-falta` | `{ "produtoId", "usuarioId", "observacao" }` | 201 (novo) ou 200 (somado) `AvisoFalta` com `"somado": true/false` |
| `PATCH /avisos-falta/{id}/resolver` | — | 200 `AvisoFalta` |

```json
{ "id": 1, "status": "ABERTO", "observacao": "acabou na prateleira", "quantidadeAvisos": 1,
  "criadoEm": "2026-10-31T10:15:00",
  "produto": { "id": 8, "sku": "1543", "nome": "Fita veda-rosca 18 mm" },
  "usuario": { "id": 4, "nome": "Valdir" } }
```

### A.6 Configurações

⏳ **Falta no back** (11.2, itens 4 e 10).

| Método e rota | Corpo | Resposta |
|---|---|---|
| `GET /configuracoes` | — | 200 `{ "estoqueMinimoGlobal": 5, "exibirFotos": true, "produtosComMinimoProprio": 4 }` |
| `PUT /configuracoes` | `{ "estoqueMinimoGlobal", "exibirFotos" }` | 200 (mesmo formato) · 400 |

### A.7 Usuários e perfis

Já existem no back: `GET`, `POST`, `PUT` e `DELETE /usuarios`. ⏳ Faltam `PATCH /usuarios/{id}/senha`, `GET /perfis-acesso` e o 422 do último Admin.

| Método e rota | Corpo | Resposta |
|---|---|---|
| `GET /usuarios` (`?incluirInativos=true`) | — | 200 `Usuario[]` |
| `GET /usuarios/{id}` | — | 200 `Usuario` · 404 |
| `POST /usuarios` | `{ "nome", "email", "senha", "perfilAcessoId" }` | 201 `Usuario` · 400 · 404 · 409 |
| `PUT /usuarios/{id}` | `{ "nome", "email", "perfilAcessoId" }` (**sem senha**) | 200 `Usuario` · 404 · 409 |
| `PATCH /usuarios/{id}/senha` | `{ "senhaAtual"?, "novaSenha" }` (≥ 8) | 204 · 400 |
| `DELETE /usuarios/{id}` | — | 204 · 404 · 422 (já inativo / último Admin) |
| `GET /perfis-acesso` | — | 200 `[ { "id": 1, "nome": "Administrador" }, { "id": 2, "nome": "Operador" } ]` |

### A.8 Dashboard

⏳ **Falta no back** (11.2, item 8).

| Método e rota | Resposta |
|---|---|
| `GET /dashboard/resumo` | 200 (abaixo) |

```json
{ "totalProdutosAtivos": 11, "produtosBaixoEstoque": 5, "produtosZerados": 2, "movimentacoesHoje": 1,
  "entradasSaidas7Dias": [ { "data": "2026-10-31", "entradas": 10, "saidas": 0 } ],
  "maisMovimentados": [ { "produto": { "id": 1, "sku": "1001", "nome": "Martelo" }, "totalMovimentado": 12 } ] }
```

`produtosBaixoEstoque` conta só os com `0 < quantidade <= estoqueMinimoEfetivo` (zerados contam à parte). `entradasSaidas7Dias` tem sempre 7 itens, do mais antigo para hoje; `saidas` é positivo.

### A.9 Prévia Bling (só mock, somente leitura)

| Rota | Resposta |
|---|---|
| `GET /divergencias` (`?tipo=NEGATIVO\|DIFERENTE\|NAO_ENCONTRADO`) | `[ { "id", "tipo", "quantidadePortal", "quantidadeBling", "diferenca", "abertaEm", "produto" } ]`, maior impacto primeiro |
| `GET /divergencias/{id}` | um item |
| `GET /integracao/estado` | `{ "conectado", "desconectadoDesde", "estagio", "ultimaSincronizacao", "ultimaConciliacao", "eventosAguardando", "eventosComErro" }` |
| `GET /integracao/eventos` | `[ { "id", "dataHora", "evento", "situacao", "detalhe" } ]` |
| `GET /importacao/previa` | `[ { "id", "codigo", "nome", "unidade", "preco", "quantidade", "localizacao" } ]` |

### A.10 Fornecedores (novo, no back desde o schema v3)

Nenhuma tela usa nesta sprint. Fica registrado para não haver surpresa no formato de `Produto`.

| Método e rota | Corpo | Resposta |
|---|---|---|
| `GET /fornecedores` (`?incluirInativos=true`) | — | 200 `Fornecedor[]` |
| `GET /fornecedores/{id}` | — | 200 `Fornecedor` · 404 |
| `POST /fornecedores` | `{ "nome", "cnpj" }` (14 dígitos, único) | 201 `Fornecedor` · 400 · 409 |
| `PUT /fornecedores/{id}` | `{ "nome", "cnpj" }` | 200 `Fornecedor` · 400 · 404 · 409 |
| `DELETE /fornecedores/{id}` | — | 204 · 404 · 422 (já inativo) |

`Fornecedor` = `{ "id", "nome", "cnpj", "ativo", "criadoEm", "atualizadoEm" }`.

---

## Apêndice B — `mock/server.cjs`

> Atualizado em 02/11/2026: **produto** segue o back do schema v3. **Movimentação** continua no formato anterior até os pontos D1 e D2 da seção 11.0. Depois de atualizar o seed, rode `npm run mock:reset` e reinicie o mock.

> Infraestrutura, não exercício. Testado com json-server 0.17.4 e Node 22: todas as rotas do Apêndice A responderam com os status esperados.

```js
/*
 * Servidor de mock do Portal Óttimos (json-server 0.17.4).
 *
 * Imita o CONTRATO do backend Spring (mesmas rotas, mesmos nomes de campo,
 * mesmo formato de erro). As rotas que têm regra de negócio (login,
 * movimentação, inativação...) são escritas à mão abaixo; o resto é o
 * CRUD automático do json-server sobre o mock/db.json.
 *
 * NÃO é exercício: é infraestrutura. Não precisa entender cada linha.
 * Rodar: npm run mock   (porta 3001)
 */
const fs = require('node:fs');
const path = require('node:path');
const jsonServer = require('json-server');

// db.json é a cópia de trabalho (muda a cada requisição de escrita).
// db.seed.json é o original. "npm run mock:reset" restaura.
const DB = path.join(__dirname, 'db.json');
const SEED = path.join(__dirname, 'db.seed.json');
if (!fs.existsSync(DB)) fs.copyFileSync(SEED, DB);

const server = jsonServer.create();
const router = jsonServer.router(DB);
const db = () => router.db; // lowdb (lodash chain)

server.use(jsonServer.defaults({ logger: true }));
server.use(jsonServer.bodyParser);

// Atraso artificial: deixa você ver os estados de "carregando".
server.use((req, res, next) => setTimeout(next, 400));

// ---------- utilitários ----------
const erro = (res, status, mensagem, erros = []) =>
  res.status(status).json({ status, mensagem, erros });

const agora = () => {
  const d = new Date();
  const p = (n) => String(n).padStart(2, '0');
  return `${d.getFullYear()}-${p(d.getMonth() + 1)}-${p(d.getDate())}T${p(d.getHours())}:${p(d.getMinutes())}:${p(d.getSeconds())}`;
};

const proximoId = (colecao) => (db().get(colecao).maxBy('id').value()?.id ?? 0) + 1;

const semSenha = ({ senha, ...resto }) => resto;

const comMinimoEfetivo = (produto) => ({
  ...produto,
  estoqueMinimoEfetivo:
    produto.estoqueMinimo ?? db().get('configuracoes.estoqueMinimoGlobal').value(),
});

const resumo = (p) => ({ id: p.id, sku: p.sku, nome: p.nome });

// ---------- autenticação ----------
server.post('/auth/login', (req, res) => {
  const { email, senha } = req.body;
  const usuario = db().get('usuarios').find({ email, senha, ativo: true }).value();
  if (!usuario) return erro(res, 401, 'Usuário ou senha incorretos.');
  res.json(semSenha(usuario));
});

// ---------- produtos ----------
server.get('/produtos', (req, res) => {
  const incluirInativos = req.query.incluirInativos === 'true';
  const lista = db().get('produtos').value()
    .filter((p) => incluirInativos || p.ativo)
    .map(comMinimoEfetivo);
  res.json(lista);
});

server.get('/produtos/:id', (req, res) => {
  const produto = db().get('produtos').find({ id: Number(req.params.id) }).value();
  if (!produto) return erro(res, 404, 'Produto não encontrado.');
  res.json(comMinimoEfetivo(produto));
});

// Lê e valida os campos comuns ao POST e ao PUT. Devolve { erroResposta } ou { campos }.
const lerCamposProduto = (req, res, idAtual = null, errosIniciais = []) => {
  const {
    sku, nome, categoriaId, peso = null, tamanho = null, descricao = null, preco = null,
    marca = null, fornecedorId = null, localizacoes = null, estoqueMinimo = null, fotoUrl = null,
  } = req.body;
  const erros = [...errosIniciais];
  if (!sku || !String(sku).trim()) erros.push({ campo: 'sku', mensagem: 'Informe o SKU.' });
  if (!nome || !String(nome).trim()) erros.push({ campo: 'nome', mensagem: 'Informe o nome.' });
  if (categoriaId === undefined || categoriaId === null) erros.push({ campo: 'categoriaId', mensagem: 'Informe a categoria.' });
  if (estoqueMinimo !== null && (!Number.isInteger(estoqueMinimo) || estoqueMinimo < 0)) {
    erros.push({ campo: 'estoqueMinimo', mensagem: 'Use um número inteiro igual ou maior que zero.' });
  }
  if (localizacoes !== null && (!Array.isArray(localizacoes) || localizacoes.some((l) => String(l).length > 120))) {
    erros.push({ campo: 'localizacoes', mensagem: 'Use uma lista de textos com até 120 caracteres.' });
  }
  if (erros.length) return { erroResposta: erro(res, 400, 'Confira os campos destacados.', erros) };

  const categoria = db().get('categorias').find({ id: Number(categoriaId) }).value();
  if (!categoria) return { erroResposta: erro(res, 404, 'Categoria não encontrada.') };

  let fornecedor = null;
  if (fornecedorId !== null) {
    const achado = db().get('fornecedores').find({ id: Number(fornecedorId) }).value();
    if (!achado) return { erroResposta: erro(res, 404, 'Fornecedor não encontrado.') };
    if (!achado.ativo) return { erroResposta: erro(res, 422, 'Este fornecedor está inativo.') };
    fornecedor = { id: achado.id, nome: achado.nome, cnpj: achado.cnpj };
  }

  const duplicado = db().get('produtos').find((p) => p.sku === sku && p.id !== idAtual).value();
  if (duplicado) return { erroResposta: erro(res, 409, 'Já existe um produto com este SKU.') };

  return {
    campos: {
      sku, nome, peso, tamanho, descricao, preco, marca, categoria, fornecedor,
      localizacoes: localizacoes ?? [], estoqueMinimo, fotoUrl,
    },
  };
};

const registrarMovimentacao = (produto, usuario, tipo, antes, depois, observacao) => {
  db().get('movimentacoes').push({
    id: proximoId('movimentacoes'),
    tipo,
    quantidade: depois - antes,
    quantidadeAntes: antes,
    quantidadeDepois: depois,
    motivo: null,
    observacao,
    documento: null,
    origem: 'MANUAL',
    dataMov: agora(),
    produto: resumo(produto),
    usuario: { id: usuario.id, nome: usuario.nome },
  }).write();
};

server.post('/produtos', (req, res) => {
  const { quantidade, usuarioId } = req.body;
  const erros = [];
  if (!Number.isInteger(quantidade) || quantidade < 0) erros.push({ campo: 'quantidade', mensagem: 'Use um número inteiro igual ou maior que zero.' });
  if (usuarioId === undefined || usuarioId === null) erros.push({ campo: 'usuarioId', mensagem: 'Informe o usuário.' });

  const { campos, erroResposta } = lerCamposProduto(req, res, null, erros);
  if (erroResposta) return erroResposta;

  const usuario = db().get('usuarios').find({ id: Number(usuarioId) }).value();
  if (!usuario) return erro(res, 404, 'Usuário não encontrado.');

  const produto = { id: proximoId('produtos'), ...campos, quantidade, ativo: true };
  db().get('produtos').push(produto).write();
  if (quantidade > 0) registrarMovimentacao(produto, usuario, 'SALDO_INICIAL', 0, quantidade, null);
  res.status(201).json(comMinimoEfetivo(produto));
});

// Nunca altera o saldo. O que não for enviado é limpo, como no back.
server.put('/produtos/:id', (req, res) => {
  const id = Number(req.params.id);
  const ref = db().get('produtos').find({ id });
  if (!ref.value()) return erro(res, 404, 'Produto não encontrado.');
  if (!ref.value().ativo) return erro(res, 422, 'Este produto está inativo.');
  const { campos, erroResposta } = lerCamposProduto(req, res, id);
  if (erroResposta) return erroResposta;
  ref.assign(campos).write();
  res.json(comMinimoEfetivo(ref.value()));
});

// Ajuste auditado: valor absoluto. Sem mudança de valor, não grava movimentação.
server.patch('/produtos/:id/quantidade', (req, res) => {
  const ref = db().get('produtos').find({ id: Number(req.params.id) });
  if (!ref.value()) return erro(res, 404, 'Produto não encontrado.');
  if (!ref.value().ativo) return erro(res, 422, 'Este produto está inativo.');

  const { quantidade, usuarioId, justificativa } = req.body;
  const erros = [];
  if (!Number.isInteger(quantidade) || quantidade < 0) erros.push({ campo: 'quantidade', mensagem: 'Use um número inteiro igual ou maior que zero.' });
  if (usuarioId === undefined || usuarioId === null) erros.push({ campo: 'usuarioId', mensagem: 'Informe o usuário.' });
  if (!justificativa || !String(justificativa).trim()) erros.push({ campo: 'justificativa', mensagem: 'Informe a justificativa.' });
  if (erros.length) return erro(res, 400, 'Confira os campos destacados.', erros);

  const usuario = db().get('usuarios').find({ id: Number(usuarioId) }).value();
  if (!usuario) return erro(res, 404, 'Usuário não encontrado.');

  const antes = ref.value().quantidade;
  if (quantidade !== antes) {
    ref.assign({ quantidade }).write();
    registrarMovimentacao(ref.value(), usuario, 'AJUSTE', antes, quantidade, justificativa);
  }
  res.json(comMinimoEfetivo(ref.value()));
});

server.delete('/produtos/:id', (req, res) => {
  const ref = db().get('produtos').find({ id: Number(req.params.id) });
  if (!ref.value()) return erro(res, 404, 'Produto não encontrado.');
  if (!ref.value().ativo) return erro(res, 422, 'Este produto já está inativo.');
  ref.assign({ ativo: false }).write();
  res.status(204).end();
});

// ---------- fornecedores ----------
// Nenhuma tela usa nesta sprint. POST, PUT e DELETE ficam no CRUD automático, sem as regras do back.
server.get('/fornecedores', (req, res) => {
  const incluirInativos = req.query.incluirInativos === 'true';
  res.json(db().get('fornecedores').value().filter((f) => incluirInativos || f.ativo));
});

// ---------- movimentações ----------
server.get('/movimentacoes', (req, res) => {
  const { produtoId, usuarioId, tipo, de, ate, limite } = req.query;
  let lista = db().get('movimentacoes').value().slice();
  if (produtoId) lista = lista.filter((m) => m.produto.id === Number(produtoId));
  if (usuarioId) lista = lista.filter((m) => m.usuario.id === Number(usuarioId));
  if (tipo) lista = lista.filter((m) => m.tipo === tipo);
  if (de) lista = lista.filter((m) => m.dataMov.slice(0, 10) >= de);
  if (ate) lista = lista.filter((m) => m.dataMov.slice(0, 10) <= ate);
  lista.sort((a, b) => b.dataMov.localeCompare(a.dataMov));
  if (limite) lista = lista.slice(0, Number(limite));
  res.json(lista);
});

server.post('/movimentacoes', (req, res) => {
  const { produtoId, usuarioId, tipo, quantidade, motivo = null, observacao = null, documento = null } = req.body;
  const refProduto = db().get('produtos').find({ id: Number(produtoId) });
  const produto = refProduto.value();
  if (!produto) return erro(res, 404, 'Produto não encontrado.');
  const usuario = db().get('usuarios').find({ id: Number(usuarioId) }).value();
  if (!usuario) return erro(res, 404, 'Usuário não encontrado.');

  const q = Number(quantidade);
  const quantidadeValida = Number.isInteger(q) && (tipo === 'AJUSTE' ? q >= 0 : q > 0);
  if (!quantidadeValida) {
    return erro(res, 400, 'Confira os campos destacados.', [
      { campo: 'quantidade', mensagem: 'Informe um número inteiro maior que zero.' },
    ]);
  }
  if (tipo === 'SAIDA' && !motivo) {
    return erro(res, 400, 'Confira os campos destacados.', [
      { campo: 'motivo', mensagem: 'Escolha o motivo da saída.' },
    ]);
  }
  if (tipo === 'AJUSTE' && (!observacao || observacao.trim().length < 10)) {
    return erro(res, 400, 'Confira os campos destacados.', [
      { campo: 'observacao', mensagem: 'Escreva uma justificativa com pelo menos 10 caracteres.' },
    ]);
  }

  const antes = produto.quantidade;
  let depois;
  if (tipo === 'ENTRADA') depois = antes + q;
  else if (tipo === 'SAIDA') {
    if (q > antes) {
      return erro(res, 422, `Não dá para tirar ${q} un.: a quantidade atual é ${antes} un.`);
    }
    depois = antes - q;
  } else if (tipo === 'AJUSTE') depois = q;
  else return erro(res, 400, 'Tipo de movimentação inválido.');

  refProduto.assign({ quantidade: depois }).write();
  const movimentacao = {
    id: proximoId('movimentacoes'),
    tipo,
    quantidade: depois - antes,
    quantidadeAntes: antes,
    quantidadeDepois: depois,
    motivo,
    observacao,
    documento,
    origem: 'MANUAL',
    dataMov: agora(),
    produto: resumo(produto),
    usuario: { id: usuario.id, nome: usuario.nome },
  };
  db().get('movimentacoes').push(movimentacao).write();
  res.status(201).json(movimentacao);
});

// ---------- avisos de falta ----------
server.get('/avisos-falta', (req, res) => {
  const { status } = req.query;
  const lista = db().get('avisosFalta').value()
    .filter((a) => !status || a.status === status)
    .sort((a, b) => b.criadoEm.localeCompare(a.criadoEm));
  res.json(lista);
});

server.post('/avisos-falta', (req, res) => {
  const { produtoId, usuarioId, observacao = null } = req.body;
  const produto = db().get('produtos').find({ id: Number(produtoId) }).value();
  const usuario = db().get('usuarios').find({ id: Number(usuarioId) }).value();
  if (!produto || !usuario) return erro(res, 404, 'Produto ou usuário não encontrado.');

  const refAberto = db().get('avisosFalta').find((a) => a.produto.id === produto.id && a.status === 'ABERTO');
  if (refAberto.value()) {
    refAberto.assign({ quantidadeAvisos: refAberto.value().quantidadeAvisos + 1 }).write();
    return res.status(200).json({ ...refAberto.value(), somado: true });
  }
  const aviso = {
    id: proximoId('avisosFalta'),
    status: 'ABERTO',
    observacao,
    quantidadeAvisos: 1,
    criadoEm: agora(),
    produto: resumo(produto),
    usuario: { id: usuario.id, nome: usuario.nome },
  };
  db().get('avisosFalta').push(aviso).write();
  res.status(201).json({ ...aviso, somado: false });
});

server.patch('/avisos-falta/:id/resolver', (req, res) => {
  const ref = db().get('avisosFalta').find({ id: Number(req.params.id) });
  if (!ref.value()) return erro(res, 404, 'Aviso não encontrado.');
  ref.assign({ status: 'RESOLVIDO' }).write();
  res.json(ref.value());
});

// ---------- configurações ----------
server.get('/configuracoes', (req, res) => {
  const config = db().get('configuracoes').value();
  const produtosComMinimoProprio = db().get('produtos').filter((p) => p.ativo && p.estoqueMinimo !== null).size().value();
  res.json({ ...config, produtosComMinimoProprio });
});

server.put('/configuracoes', (req, res) => {
  const { estoqueMinimoGlobal, exibirFotos } = req.body;
  if (!Number.isInteger(estoqueMinimoGlobal) || estoqueMinimoGlobal < 0) {
    return erro(res, 400, 'Confira os campos destacados.', [
      { campo: 'estoqueMinimoGlobal', mensagem: 'Use um número inteiro igual ou maior que zero.' },
    ]);
  }
  db().get('configuracoes').assign({ estoqueMinimoGlobal, exibirFotos: Boolean(exibirFotos) }).write();
  const produtosComMinimoProprio = db().get('produtos').filter((p) => p.ativo && p.estoqueMinimo !== null).size().value();
  res.json({ ...db().get('configuracoes').value(), produtosComMinimoProprio });
});

// ---------- usuários ----------
server.get('/perfis-acesso', (req, res) => res.json(db().get('perfisAcesso').value()));

const perfil = (id) => db().get('perfisAcesso').find({ id: Number(id) }).value();

server.get('/usuarios', (req, res) => {
  const incluirInativos = req.query.incluirInativos === 'true';
  res.json(db().get('usuarios').value().filter((u) => incluirInativos || u.ativo).map(semSenha));
});

server.get('/usuarios/:id', (req, res) => {
  const usuario = db().get('usuarios').find({ id: Number(req.params.id) }).value();
  if (!usuario) return erro(res, 404, 'Usuário não encontrado.');
  res.json(semSenha(usuario));
});

server.post('/usuarios', (req, res) => {
  const { nome, email, senha, perfilAcessoId } = req.body;
  if (!nome || !email || !senha) return erro(res, 400, 'Preencha nome, e-mail e senha.');
  if (!perfil(perfilAcessoId)) return erro(res, 404, 'Perfil de acesso não encontrado.');
  if (db().get('usuarios').find({ email }).value()) return erro(res, 409, 'Já existe um usuário com este e-mail.');
  const usuario = { id: proximoId('usuarios'), nome, email, senha, perfilAcesso: perfil(perfilAcessoId), ativo: true };
  db().get('usuarios').push(usuario).write();
  res.status(201).json(semSenha(usuario));
});

server.put('/usuarios/:id', (req, res) => {
  const ref = db().get('usuarios').find({ id: Number(req.params.id) });
  if (!ref.value()) return erro(res, 404, 'Usuário não encontrado.');
  const { nome, email, perfilAcessoId } = req.body;
  if (!perfil(perfilAcessoId)) return erro(res, 404, 'Perfil de acesso não encontrado.');
  const outro = db().get('usuarios').find((u) => u.email === email && u.id !== ref.value().id).value();
  if (outro) return erro(res, 409, 'Já existe um usuário com este e-mail.');
  ref.assign({ nome, email, perfilAcesso: perfil(perfilAcessoId) }).write();
  res.json(semSenha(ref.value()));
});

server.patch('/usuarios/:id/senha', (req, res) => {
  const ref = db().get('usuarios').find({ id: Number(req.params.id) });
  if (!ref.value()) return erro(res, 404, 'Usuário não encontrado.');
  const { senhaAtual, novaSenha } = req.body;
  if (senhaAtual !== undefined && senhaAtual !== ref.value().senha) {
    return erro(res, 400, 'Confira os campos destacados.', [
      { campo: 'senhaAtual', mensagem: 'A senha atual está incorreta.' },
    ]);
  }
  if (!novaSenha || novaSenha.length < 8) {
    return erro(res, 400, 'Confira os campos destacados.', [
      { campo: 'novaSenha', mensagem: 'Use pelo menos 8 caracteres.' },
    ]);
  }
  ref.assign({ senha: novaSenha }).write();
  res.status(204).end();
});

server.delete('/usuarios/:id', (req, res) => {
  const ref = db().get('usuarios').find({ id: Number(req.params.id) });
  if (!ref.value()) return erro(res, 404, 'Usuário não encontrado.');
  if (!ref.value().ativo) return erro(res, 422, 'Este usuário já está desativado.');
  const admins = db().get('usuarios').filter((u) => u.ativo && u.perfilAcesso.nome === 'Administrador').size().value();
  if (ref.value().perfilAcesso.nome === 'Administrador' && admins <= 1) {
    return erro(res, 422, 'Não é possível desativar o último administrador ativo.');
  }
  ref.assign({ ativo: false }).write();
  res.status(204).end();
});

// ---------- dashboard ----------
server.get('/dashboard/resumo', (req, res) => {
  const produtos = db().get('produtos').value().filter((p) => p.ativo).map(comMinimoEfetivo);
  const movs = db().get('movimentacoes').value();
  const hoje = agora().slice(0, 10);

  const dias = [];
  for (let i = 6; i >= 0; i--) {
    const d = new Date();
    d.setDate(d.getDate() - i);
    const p = (n) => String(n).padStart(2, '0');
    dias.push(`${d.getFullYear()}-${p(d.getMonth() + 1)}-${p(d.getDate())}`);
  }
  const entradasSaidas7Dias = dias.map((data) => {
    const doDia = movs.filter((m) => m.dataMov.slice(0, 10) === data);
    return {
      data,
      entradas: doDia.filter((m) => m.quantidade > 0).reduce((s, m) => s + m.quantidade, 0),
      saidas: doDia.filter((m) => m.quantidade < 0).reduce((s, m) => s - m.quantidade, 0),
    };
  });

  const totais = {};
  for (const m of movs) {
    totais[m.produto.id] ??= { produto: m.produto, totalMovimentado: 0 };
    totais[m.produto.id].totalMovimentado += Math.abs(m.quantidade);
  }

  res.json({
    totalProdutosAtivos: produtos.length,
    produtosBaixoEstoque: produtos.filter((p) => p.quantidade > 0 && p.quantidade <= p.estoqueMinimoEfetivo).length,
    produtosZerados: produtos.filter((p) => p.quantidade <= 0).length,
    movimentacoesHoje: movs.filter((m) => m.dataMov.slice(0, 10) === hoje).length,
    entradasSaidas7Dias,
    maisMovimentados: Object.values(totais).sort((a, b) => b.totalMovimentado - a.totalMovimentado).slice(0, 5),
  });
});

// ---------- prévia Bling (somente leitura, sem regra) ----------
server.get('/divergencias', (req, res) => {
  const { tipo } = req.query;
  const lista = db().get('divergencias').value()
    .filter((d) => !tipo || d.tipo === tipo)
    .map((d) => ({ ...d, diferenca: d.quantidadeBling === null ? null : d.quantidadePortal - d.quantidadeBling }))
    .sort((a, b) => Math.abs(b.diferenca ?? 0) - Math.abs(a.diferenca ?? 0));
  res.json(lista);
});
server.get('/divergencias/:id', (req, res) => {
  const d = db().get('divergencias').find({ id: Number(req.params.id) }).value();
  if (!d) return erro(res, 404, 'Divergência não encontrada.');
  res.json(d);
});
server.get('/integracao/estado', (req, res) => res.json(db().get('integracao').value()));
server.get('/integracao/eventos', (req, res) => res.json(db().get('eventosIntegracao').value()));
server.get('/importacao/previa', (req, res) => res.json(db().get('importacaoPrevia').value()));

server.use(router);
server.listen(3001, () => console.log('Mock do Portal Óttimos em http://localhost:3001'));
```

---

## Apêndice C — `mock/db.seed.json`

> Atualizado em 02/11/2026: **produto** segue o back do schema v3. **Movimentação** continua no formato anterior até os pontos D1 e D2 da seção 11.0. Depois de atualizar o seed, rode `npm run mock:reset` e reinicie o mock.

> Logins de teste (senha `123456` para todos): **marcia@ottimos.com** e **carlos@ottimos.com** (Admin) · **ronaldo@ottimos.com** e **valdir@ottimos.com** (Operador).
> O servidor copia este arquivo para `mock/db.json` na primeira vez; `npm run mock:reset` restaura.

```json
{
  "perfisAcesso": [
    {"id": 1, "nome": "Administrador"},
    {"id": 2, "nome": "Operador"}
  ],
  "usuarios": [
    {"id": 1, "nome": "Márcia", "email": "marcia@ottimos.com", "senha": "123456", "perfilAcesso": {"id": 1, "nome": "Administrador"}, "ativo": true},
    {"id": 2, "nome": "Carlos", "email": "carlos@ottimos.com", "senha": "123456", "perfilAcesso": {"id": 1, "nome": "Administrador"}, "ativo": true},
    {"id": 3, "nome": "Ronaldo", "email": "ronaldo@ottimos.com", "senha": "123456", "perfilAcesso": {"id": 2, "nome": "Operador"}, "ativo": true},
    {"id": 4, "nome": "Valdir", "email": "valdir@ottimos.com", "senha": "123456", "perfilAcesso": {"id": 2, "nome": "Operador"}, "ativo": true}
  ],
  "categorias": [
    {"id": 1, "nome": "Ferragens"},
    {"id": 2, "nome": "Fixacao"},
    {"id": 3, "nome": "Eletrica"}
  ],
  "fornecedores": [
    {"id": 1, "nome": "Ferragens Silva", "cnpj": "11222333000181", "ativo": true, "criadoEm": "2026-10-01T08:00:00", "atualizadoEm": "2026-10-01T08:00:00"},
    {"id": 2, "nome": "Distribuidora Central", "cnpj": "22333444000172", "ativo": true, "criadoEm": "2026-10-01T08:00:00", "atualizadoEm": "2026-10-01T08:00:00"},
    {"id": 3, "nome": "Aco Forte", "cnpj": "33444555000163", "ativo": false, "criadoEm": "2026-10-01T08:00:00", "atualizadoEm": "2026-10-01T08:00:00"}
  ],
  "produtos": [
    {"id": 1, "sku": "1001", "nome": "Martelo", "peso": null, "tamanho": null, "descricao": "Martelo de unha 27 mm, cabo de madeira.", "preco": 34.9, "marca": null, "quantidade": 2, "ativo": true, "categoria": {"id": 1, "nome": "Ferragens"}, "fornecedor": {"id": 1, "nome": "Ferragens Silva", "cnpj": "11222333000181"}, "localizacoes": ["C3-P2"], "estoqueMinimo": 10, "fotoUrl": null},
    {"id": 2, "sku": "1002", "nome": "Chave de fenda", "peso": null, "tamanho": null, "descricao": null, "preco": 12.5, "marca": null, "quantidade": 2, "ativo": true, "categoria": {"id": 1, "nome": "Ferragens"}, "fornecedor": {"id": 2, "nome": "Distribuidora Central", "cnpj": "22333444000172"}, "localizacoes": ["C3-P2"], "estoqueMinimo": null, "fotoUrl": null},
    {"id": 3, "sku": "1003", "nome": "Serrote", "peso": null, "tamanho": null, "descricao": null, "preco": 59.9, "marca": null, "quantidade": 0, "ativo": true, "categoria": {"id": 1, "nome": "Ferragens"}, "fornecedor": {"id": 1, "nome": "Ferragens Silva", "cnpj": "11222333000181"}, "localizacoes": ["C4-P1"], "estoqueMinimo": null, "fotoUrl": null},
    {"id": 4, "sku": "1004", "nome": "Alicate universal", "peso": null, "tamanho": null, "descricao": null, "preco": 42, "marca": null, "quantidade": 3, "ativo": true, "categoria": {"id": 1, "nome": "Ferragens"}, "fornecedor": {"id": 1, "nome": "Ferragens Silva", "cnpj": "11222333000181"}, "localizacoes": ["C3-P1"], "estoqueMinimo": null, "fotoUrl": null},
    {"id": 5, "sku": "1005", "nome": "Trena 5 m", "peso": null, "tamanho": null, "descricao": null, "preco": 28.9, "marca": null, "quantidade": 4, "ativo": true, "categoria": {"id": 1, "nome": "Ferragens"}, "fornecedor": {"id": 1, "nome": "Ferragens Silva", "cnpj": "11222333000181"}, "localizacoes": ["C2-P3"], "estoqueMinimo": null, "fotoUrl": null},
    {"id": 6, "sku": "1006", "nome": "Espátula 8 cm", "peso": null, "tamanho": null, "descricao": null, "preco": 9.9, "marca": null, "quantidade": 5, "ativo": true, "categoria": {"id": 1, "nome": "Ferragens"}, "fornecedor": null, "localizacoes": [], "estoqueMinimo": null, "fotoUrl": null},
    {"id": 7, "sku": "1020", "nome": "Parafuso sextavado 6 mm", "peso": null, "tamanho": null, "descricao": "Caixa avulsa, vendido por unidade.", "preco": 0.45, "marca": null, "quantidade": 10, "ativo": true, "categoria": {"id": 2, "nome": "Fixacao"}, "fornecedor": {"id": 2, "nome": "Distribuidora Central", "cnpj": "22333444000172"}, "localizacoes": ["C1-P4", "C1-P5"], "estoqueMinimo": 50, "fotoUrl": null},
    {"id": 8, "sku": "1543", "nome": "Fita veda-rosca 18 mm", "peso": null, "tamanho": null, "descricao": null, "preco": 4.9, "marca": null, "quantidade": 0, "ativo": true, "categoria": {"id": 2, "nome": "Fixacao"}, "fornecedor": {"id": 2, "nome": "Distribuidora Central", "cnpj": "22333444000172"}, "localizacoes": ["C5-P2"], "estoqueMinimo": null, "fotoUrl": null},
    {"id": 9, "sku": "2001", "nome": "Fio flexível 2,5 mm (metro)", "peso": null, "tamanho": null, "descricao": null, "preco": 3.2, "marca": null, "quantidade": 120, "ativo": true, "categoria": {"id": 3, "nome": "Eletrica"}, "fornecedor": {"id": 2, "nome": "Distribuidora Central", "cnpj": "22333444000172"}, "localizacoes": ["C6-P1"], "estoqueMinimo": 30, "fotoUrl": null},
    {"id": 10, "sku": "2002", "nome": "Tomada 2P+T 10 A", "peso": null, "tamanho": null, "descricao": null, "preco": 11.9, "marca": null, "quantidade": 35, "ativo": true, "categoria": {"id": 3, "nome": "Eletrica"}, "fornecedor": {"id": 2, "nome": "Distribuidora Central", "cnpj": "22333444000172"}, "localizacoes": ["C6-P2"], "estoqueMinimo": null, "fotoUrl": null},
    {"id": 11, "sku": "1030", "nome": "Bucha 8 mm", "peso": null, "tamanho": null, "descricao": null, "preco": 0.2, "marca": null, "quantidade": 400, "ativo": true, "categoria": {"id": 2, "nome": "Fixacao"}, "fornecedor": {"id": 2, "nome": "Distribuidora Central", "cnpj": "22333444000172"}, "localizacoes": ["C1-P3"], "estoqueMinimo": 100, "fotoUrl": null},
    {"id": 12, "sku": "1099", "nome": "Martelo de borracha", "peso": null, "tamanho": null, "descricao": null, "preco": 27.5, "marca": null, "quantidade": 8, "ativo": false, "categoria": {"id": 1, "nome": "Ferragens"}, "fornecedor": {"id": 1, "nome": "Ferragens Silva", "cnpj": "11222333000181"}, "localizacoes": ["C3-P2"], "estoqueMinimo": null, "fotoUrl": null}
  ],
  "movimentacoes": [
    {"id": 1, "tipo": "ENTRADA", "quantidade": 10, "quantidadeAntes": 0, "quantidadeDepois": 10, "motivo": null, "observacao": null, "documento": "NF 12345", "origem": "MANUAL", "dataMov": "2026-10-28T09:12:00", "produto": {"id": 7, "sku": "1020", "nome": "Parafuso sextavado 6 mm"}, "usuario": {"id": 3, "nome": "Ronaldo"}},
    {"id": 2, "tipo": "SAIDA", "quantidade": -1, "quantidadeAntes": 1, "quantidadeDepois": 0, "motivo": "AVARIA", "observacao": "Lâmina quebrada", "documento": null, "origem": "MANUAL", "dataMov": "2026-10-29T15:40:00", "produto": {"id": 3, "sku": "1003", "nome": "Serrote"}, "usuario": {"id": 4, "nome": "Valdir"}},
    {"id": 3, "tipo": "AJUSTE", "quantidade": 2, "quantidadeAntes": 0, "quantidadeDepois": 2, "motivo": null, "observacao": "Contagem da prateleira C3-P2 na quarta-feira", "documento": null, "origem": "MANUAL", "dataMov": "2026-10-30T10:05:00", "produto": {"id": 1, "sku": "1001", "nome": "Martelo"}, "usuario": {"id": 1, "nome": "Márcia"}},
    {"id": 4, "tipo": "SAIDA", "quantidade": -3, "quantidadeAntes": 38, "quantidadeDepois": 35, "motivo": "VENDA_BALCAO", "observacao": null, "documento": null, "origem": "MANUAL", "dataMov": "2026-10-30T16:22:00", "produto": {"id": 10, "sku": "2002", "nome": "Tomada 2P+T 10 A"}, "usuario": {"id": 3, "nome": "Ronaldo"}}
  ],
  "avisosFalta": [
    {"id": 1, "status": "ABERTO", "observacao": "acabou na prateleira", "quantidadeAvisos": 1, "criadoEm": "2026-10-31T10:15:00", "produto": {"id": 8, "sku": "1543", "nome": "Fita veda-rosca 18 mm"}, "usuario": {"id": 4, "nome": "Valdir"}}
  ],
  "configuracoes": {"estoqueMinimoGlobal": 5, "exibirFotos": true},
  "divergencias": [
    {"id": 1, "tipo": "NEGATIVO", "quantidadePortal": 12, "quantidadeBling": -3, "abertaEm": "2026-10-27T08:00:00", "produto": {"id": 1, "sku": "1001", "nome": "Martelo"}},
    {"id": 2, "tipo": "DIFERENTE", "quantidadePortal": 35, "quantidadeBling": 30, "abertaEm": "2026-10-29T08:00:00", "produto": {"id": 10, "sku": "2002", "nome": "Tomada 2P+T 10 A"}},
    {"id": 3, "tipo": "NAO_ENCONTRADO", "quantidadePortal": 400, "quantidadeBling": null, "abertaEm": "2026-10-30T08:00:00", "produto": {"id": 11, "sku": "1030", "nome": "Bucha 8 mm"}}
  ],
  "integracao": {"conectado": false, "desconectadoDesde": "2026-10-31T14:32:00", "estagio": "SOMBRA", "ultimaSincronizacao": "2026-10-31T14:32:00", "ultimaConciliacao": "2026-10-31T06:00:00", "eventosAguardando": 0, "eventosComErro": 2},
  "eventosIntegracao": [
    {"id": 1, "dataHora": "2026-10-31T14:35:00", "evento": "PEDIDO", "situacao": "COM_ERRO", "detalhe": "O Bling não respondeu. Nova tentativa às 14:40."},
    {"id": 2, "dataHora": "2026-10-31T14:20:00", "evento": "ESTOQUE", "situacao": "PROCESSADO", "detalhe": "Saldo de 1020 – Parafuso sextavado 6 mm atualizado."},
    {"id": 3, "dataHora": "2026-10-31T13:58:00", "evento": "NOTA", "situacao": "COM_ERRO", "detalhe": "Nota 4471 sem itens reconhecidos."}
  ],
  "importacaoPrevia": [
    {"id": 1, "codigo": "1001", "nome": "Martelo", "unidade": "un", "preco": 34.9, "quantidade": 2, "localizacao": "C3-P2"},
    {"id": 2, "codigo": "1002", "nome": "Chave de fenda", "unidade": "un", "preco": 12.5, "quantidade": 2, "localizacao": "C3-P2"},
    {"id": 3, "codigo": "1020", "nome": "Parafuso sextavado 6 mm", "unidade": "un", "preco": 0.45, "quantidade": 10, "localizacao": "C1-P4"}
  ]
}
```

---

## Apêndice D — Mapa das telas no Figma

Arquivo: `https://www.figma.com/design/403Y8A2xrycyIBl46F36x3/Untitled` · `fileKey = 403Y8A2xrycyIBl46F36x3`
Todos os frames têm 1366 px de largura. Para abrir um frame: `...?node-id=3-40251` (troque `:` por `-`).

| Tela | Frame | nodeId |
|---|---|---|
| TL01 Login | TL01 · Login | `3:40251` |
| TL02 Menu (Admin) | TL02 · Menu principal · Admin | `3:40273` |
| TL02 Menu (Operador) | TL02 · Menu principal · Operador | `3:40414` |
| TL03 Início (Admin) | TL03 · Início · Admin | `3:40527` |
| TL03 Início (Operador) | TL03 · Início · Operador | `3:40623` |
| TL04 Resultado da busca | TL04 · Resultado da busca | `3:40717` |
| TL05 Detalhe (Admin) | TL05 · Detalhe do produto · Admin | `3:40778` |
| TL05 Detalhe (Operador) | TL05 · Detalhe do produto · Operador | `3:40890` |
| TL06 Registrar entrada | TL06 · Registrar entrada | `3:40993` |
| TL07 Registrar saída | TL07 · Registrar saída | `3:41032` |
| TL08 Ajustar por contagem | TL08 · Ajustar por contagem | `3:41079` |
| TL09 Confirmar entrada | TL09 · Confirmar entrada? | `3:41114` |
| TL09 Confirmar saída | TL09 · Confirmar saída? | `3:41166` |
| TL09 Confirmar ajuste | TL09 · Confirmar ajuste? | `3:41226` |
| TL09 Inativar | TL09 · Inativar Martelo? | `3:41274` |
| TL10 Avisar falta | TL10 · Avisar que está em falta | `3:41399` |
| TL11 Histórico | TL11 · Histórico | `3:41512` |
| TL12 Editar produto | TL12 · Editar dados do produto | `3:41581` |
| TL13 Configurações | TL13 · Configurações | `3:41655` |
| TL14 Divergências | TL14 · Divergências e negativos | `3:41693` |
| TL15 Resolver divergência | TL15 · Resolver divergência | `3:41750` |
| TL16 Importar · passo 1 | TL16 · Importar produtos · Enviar arquivo | `3:41800` |
| TL16 Importar · passo 2 | TL16 · Importar produtos · Conferir prévia | `3:41839` |
| TL16 Importar · passo 3 | TL16 · Importar produtos · Resultado | `3:41901` |
| TL17 Integração com o Bling | TL17 · Integração com o Bling | `3:41935` |
| TL18 Usuários | TL18 · Usuários | `3:42022` |
| TL18 Usuários (painel aberto) | TL18 · Usuários · Formulário aberto | `3:42126` |
| TL19 Meu perfil | TL19 · Meu perfil | `3:42247` |
| TL20 Conferência (fora do escopo) | TL20 · Conferência | `3:42305` |
| Dashboard | — (sem frame; ver exercício 3.5) | — |

**Observações sobre o arquivo do Figma**

- Não há componentes nem variáveis do Figma: tudo são frames. A divisão em componentes React segue a seção 2, e os valores vêm dos tokens do brief.
- Ícones com nomes da família **Lucide** → `lucide-react`: `user-round` → `UserRound`, `arrow-left` → `ArrowLeft`, `lock-keyhole` → `LockKeyhole`, `triangle-alert` → `TriangleAlert`, `image` → `Image`, `search` → `Search`, `x` → `X`, `eye` → `Eye`, `info` → `Info`, `check` → `Check`.
- Altura do cabeçalho no Figma: 76 px de barra + 4 px de faixa amarela.

---

## Apêndice E — Decisões sobre as lacunas do brief

O brief lista 26 lacunas. Estas são as decisões adotadas para esta sprint (podem ser revistas com o cliente):

| # | Lacuna | Decisão |
|---|---|---|
| 1 | Aba "Cadastrar produto" | Aparece **desabilitada** com dica "Em breve" |
| 2 | Máscara da localização | Texto livre em maiúsculas, até 20 caracteres; exemplo "C3-P2" no placeholder |
| 3 | Faixa "perto do limite" | Fora |
| 4 | Quem resolve divergências | Admin (Bloco D é só prévia) |
| 5 | Texto base 16 × 18 px | **16 px** (tabela de tipografia) |
| 6 | Localização ≥ 24 px | 16 px nos cards; 18 px (`texto-h3`) no Detalhe |
| 7 | Desktop-first | Desktop-first (1366 px) |
| 8 | Grid e medidas | Do Figma: cabeçalho 80 px, margem 32 px, gaveta 360 px |
| 9 | Breakpoints intermediários | 3 colunas ≥ 1024 px · 2 colunas ≥ 640 px · 1 coluna abaixo; tabelas com rolagem horizontal **dentro do card** em telas estreitas |
| 10 | Contador de divergências no Início | Fora (só no menu) |
| 11 | Toast | Canto superior direito, 5 s, botão "Fechar" |
| 12 | Sombra 0.5 | Usar o valor que o Figma devolver em `get_design_context`; se vier 0.5, manter |
| 13 | Desabilitado e fundo escurecido | Opacidade 0.5; `::backdrop` `rgba(34, 34, 34, 0.5)` |
| 14 | Ilustração do login | Placeholder (bloco `--cor-primaria-suave` com ícone `Package`) até existir arquivo |
| 15 | Logotipo | Marca em texto "PORTAL ÓTTIMOS" |
| 16 | Ícones | `lucide-react` (o Figma já usa Lucide) |
| 17 | Avatar | Ícone `UserRound` em círculo (igual ao Figma) |
| 18 | Paginação | "Carregar mais" de 25 em 25 (Busca e Histórico) |
| 19 | Atalhos de teclado | Fora; só o padrão (Tab, Enter, Esc, setas) |
| 20 | Mensagens faltantes | E-mail duplicado: "Já existe um usuário com este e-mail." · Senha atual errada: "A senha atual está incorreta." · Senhas diferentes: "As senhas não são iguais." · Configuração inválida: "Use um número inteiro igual ou maior que zero." · Erro ao carregar: "Não foi possível carregar. Tente de novo." |
| 21 | "Outras configurações" | Fora |
| 22 | Lista de produtos com mínimo próprio | Só a contagem nesta sprint |
| 23 | Cards sem foto | Card sem a área da foto (texto ocupa a largura toda) |
| 24 | Selo "Divergente" | `--cor-erro` |
| 25 | Redefinir senha (TL18) | ⭐ opcional: gera nova senha provisória e mostra uma vez (mesmo padrão do cadastro) |
| 26 | Telas sem wireframe | Já desenhadas no Figma (Apêndice D) |
| — | Login por usuário × e-mail | **E-mail** (o backend usa e-mail como campo único) |
| — | Senha provisória | Gerada no front e enviada no `POST /usuarios` |
| — | Foto do produto | URL (sem upload nesta fase) |

---

## Apêndice F — Glossário

| Termo | Significado |
|---|---|
| **Componente controlado** | Campo cujo valor vive num `useState` e muda por `onChange` |
| **Estado derivado** | Valor calculado a partir de outros estados durante o render, sem `useState` próprio |
| **Context** | Forma do React de compartilhar dados com qualquer componente abaixo, sem passar props |
| **Hook personalizado** | Função `useAlgo` que junta outros hooks para reaproveitar lógica |
| **Mock** | Servidor de mentira que responde como o back de verdade |
| **Proxy** | O Vite recebe `/api/...` e repassa para o mock ou para o Spring |
| **Contrato** | Combinado de rotas, campos e erros entre front e back (Apêndice A) |
| **Slice / Segment / API pública** | Ver seção 2.3 e 2.4 |
| **POC** | Prova de conceito: funciona de ponta a ponta, mas sem Bling e sem segurança de produção |
