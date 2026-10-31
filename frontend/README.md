# Portal Óttimos — Front-end

React 19 + Vite, organizado em Feature-Sliced Design (FSD).
Plano completo do projeto: [`../docs/PLANO-FRONTEND.md`](../docs/PLANO-FRONTEND.md).

## Instalar

Pré-requisito: Node.js 22 LTS e npm 10+.

```bash
cd frontend
npm install
cp .env.example .env.local   # só na primeira vez; o .env.local não vai para o Git
```

## Rodar

```bash
npm run dev:mock
```

Sobe dois processos juntos:

| Processo | Endereço | O que é |
|---|---|---|
| `mock` | http://localhost:3001 | API falsa (json-server 0.17.4) que imita o backend Spring |
| `web` | http://localhost:5173 | O Portal (Vite) |

O front sempre chama `/api/...`. O Vite repassa para o mock ou para o Spring, conforme o `.env.local`.

Outros scripts:

| Comando | Para quê |
|---|---|
| `npm run dev` | Só o front (sem mock) |
| `npm run mock` | Só o mock |
| `npm run build` | Gera a versão de produção em `dist/` |
| `npm run lint` | Confere o código com o oxlint |
| `npm run preview` | Serve o `dist/` localmente |

## Resetar o mock

O mock grava as alterações em `mock/db.json` (cópia de trabalho, fora do Git). Para voltar aos dados originais de `mock/db.seed.json`:

```bash
npm run mock:reset
```

Rode com o mock parado, ou reinicie o `npm run dev:mock` depois.

## Apontar rotas para o Spring

Quando uma rota ficar pronta no backend, liste-a em `ROTAS_NO_SPRING` no `.env.local` (separadas por vírgula, sem `/api`):

```bash
# .env.local
MOCK_API_URL=http://localhost:3001
SPRING_API_URL=http://localhost:8080
ROTAS_NO_SPRING=auth,produtos,movimentacoes
```

- Rotas listadas → Spring (8080). Todo o resto → mock (3001).
- Vazio = tudo no mock.
- Reinicie o `npm run dev:mock` depois de mudar o `.env.local`.
- O backend sobe com `docker compose up` na pasta `backend/`.

## Logins de teste (mock)

Senha `123456` para todos.

| E-mail | Perfil |
|---|---|
| marcia@ottimos.com | Administrador |
| carlos@ottimos.com | Administrador |
| ronaldo@ottimos.com | Operador |
| valdir@ottimos.com | Operador |

## Estrutura

```
src/
├── app/       # liga tudo: rotas, providers, estilos globais
├── pages/     # uma tela inteira (uma rota)
├── widgets/   # blocos grandes e autossuficientes de uma tela
├── features/  # uma ação do usuário (registrar entrada, avisar falta)
├── entities/  # os "substantivos" do negócio (produto, movimentação)
└── shared/    # peças sem regra de negócio (botão, axios, formatadores)
```

Regra de ouro: um arquivo só importa de camadas **abaixo** da sua, sempre pelo `index.js` do slice e com o alias `@/`.
