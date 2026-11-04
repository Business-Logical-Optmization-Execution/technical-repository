# Portal Blox — Technical Repository

API REST de **controle de estoque e produtos** do projeto Portal Blox: cadastro de produtos,
fornecedores, usuários e perfis de acesso, com saldo global por produto e histórico auditável de
movimentações. Este repositório contém o **backend Spring Boot** e o **banco MySQL**, dockerizados
em duas imagens independentes orquestradas por Docker Compose — qualquer máquina com Docker sobe o
ambiente completo sem instalar Java, Maven ou MySQL localmente.

---

## Stack

| Camada | Tecnologia | Versão |
|---|---|---|
| Linguagem | Java (LTS) | 21 |
| Framework | Spring Boot (`webmvc`, `data-jpa`, `validation`) | 4.1.1 |
| Documentação da API | SpringDoc OpenAPI (Swagger UI) | 3.1.0 |
| Banco de dados | MySQL (LTS) | 8.4 |
| Build | Maven (wrapper incluído) | 3.9 |
| Container build | `maven:3.9-eclipse-temurin-21` → `eclipse-temurin:21-jre` | multi-stage |
| Orquestração | Docker Compose | v2 |

---

## Arquitetura

O compose sobe **dois containers** em uma rede privada (`blox-net`), com um **volume nomeado**
(`db-data`) que preserva os dados do banco entre subidas:

```
                 Docker Compose
┌──────────────────────────────────────────────────────────────────┐
│                                                                  │
│  ┌────────────────────────┐        blox-net        ┌───────────┐ │
│  │ backend                │   jdbc:mysql://db:3306 │ db        │ │
│  │ Spring Boot 4 (JRE 21) │ ─────────────────────► │ MySQL 8.4 │ │
│  │ porta 8080 publicada   │   (após healthcheck)   │ :3306     │ │
│  │ no host  →  :8080      │                        │ interno   │ │
│  └────────────────────────┘                        └─────┬─────┘ │
│                                                          │       │
│                                              volume db-data     │
│                                              (init 1ª subida)   │
└──────────────────────────────────────────────────────────────────┘
```

Pontos-chave da orquestração:

- **Inicialização automática**: na primeira subida (volume novo), o container do banco executa
  `database/script-banco-v4.sql` (criação idempotente do schema `controle_estoque`, 9 tabelas e
  seed de perfis/categorias) e, em seguida, `database/zz-seed-demo.sql` (dados de demonstração).
  Em volumes já inicializados os scripts **não** re-executam.
- **Ordem de subida garantida**: o `db` tem `healthcheck` (mysqladmin ping) e o `backend` usa
  `depends_on: condition: service_healthy` — só sobe depois que o banco responde.
- **Conexão por variáveis de ambiente**: o `backend` resolve o banco pelo hostname `db` (rede do
  compose) via `DB_URL`/`DB_USERNAME`/`DB_PASSWORD`; o `application.properties` guarda defaults
  locais para rodar fora do Docker.
- **Sem conflito com MySQL local**: a porta 3306 do container **não** é publicada no host.

### Estrutura do repositório

```
backend/                        # raiz deste README
├── docker-compose.yml          # orquestração dos 2 serviços
├── .env.example                # modelo de variáveis de ambiente (versionado)
├── .env                        # valores reais (git ignorado)
├── backend/blox/
│   ├── Dockerfile              # multi-stage: Maven build → JRE runtime
│   ├── .dockerignore
│   ├── mvnw / mvnw.cmd         # Maven wrapper
│   └── src/main/java/com/portalblox/
│       ├── controller/         # endpoints REST
│       ├── service/            # regras de negócio
│       ├── repository/         # Spring Data JPA
│       ├── entity/             # entidades JPA
│       ├── dto/ + mapper/      # contratos de entrada/saída
│       └── exception/          # tratamento de erros
├── database/
│   ├── Dockerfile              # mysql:8.4 + scripts de init (v4 + seed)
│   ├── .dockerignore
│   ├── script-banco-v4.sql     # schema + seeds mínimos (fonte canônica)
│   ├── zz-seed-demo.sql        # dados de demonstração para testar a API
│   ├── script-banco-v3.sql     # histórico da modelagem anterior
│   └── script-banco-v2.sql     # histórico da modelagem anterior

```

### Banco de dados

Schema `controle_estoque` com 9 tabelas: `categorias`, `fornecedores`, `produtos`,
`produto_localizacoes`, `perfis_acesso`, `usuarios`, `configuracoes`, `movimentacoes_estoque` e
`campanhas`. O Hibernate roda com `ddl-auto=validate` — o schema é gerenciado **somente** pelo
`database/script-banco-v4.sql`, que é a fonte canônica. O `script-banco-v3.sql` e o
`script-banco-v2.sql` permanecem no repositório como histórico e não são executados; o
`database/Dockerfile` copia apenas o v4 e o seed. Volumes antigos precisam de
`docker compose down -v` para receber o schema novo.

Pontos do modelo:

- `produtos.quantidade` é o **saldo global único** do produto; não existe saldo por localização.
- Cada produto pertence a **exatamente uma** categoria (`produtos.categoria_id`); a antiga
  tabela `produtos_categorias` não existe mais.
- `produtos.foto_url` (até 500 caracteres) e `produtos.estoque_minimo` (inteiro maior ou igual a
  zero ou nulo) são opcionais. O mínimo efetivo é calculado: usa `estoque_minimo` quando
  preenchido e, quando nulo, o `estoque_minimo_global` de `configuracoes`.
- `produto_localizacoes` guarda apenas rótulos textuais (sem quantidade), com unicidade por
  produto e comparação case-insensitive herdada da collation `utf8mb4_unicode_ci`.
- `configuracoes` tem **linha única** (`id = 1`) com `estoque_minimo_global` e `exibir_fotos`.
  Não há endpoint de escrita nesta fase; alterações são feitas por seed ou SQL.
- `movimentacoes_estoque` é um **ledger imutável**: cada linha guarda produto, autor, tipo,
  quantidade absoluta, variação assinada, `quantidade_antes`, `quantidade_depois`, `motivo`,
  `observacao`, `documento`, `origem` e data. `justificativa` é opcional e `observacao` é o
  campo auditável.
- `fornecedores` guarda `nome` e `cnpj` (somente dígitos, 14 caracteres, único), sem validação
  de dígitos verificadores.
- A tabela `campanhas` foi preservada sem alterações.

### Dados de demonstração

`database/zz-seed-demo.sql` roda automaticamente logo após o schema e deixa a base pronta para
exercitar a API:

| Entidade | Conteúdo |
|---|---|
| Perfis | `Administrador` (1) e `Operador` (2) |
| Categorias | `Ferragens` (1), `Fixacao` (2) e `Eletrica` (3) |
| Fornecedores | `Ferragens Silva` (1), `Distribuidora Central` (2) e `Aco Forte` (3, inativo) |
| Usuários | `marcia@ottimos.com` e `carlos@ottimos.com` (Administrador), `ronaldo@ottimos.com` e `valdir@ottimos.com` (Operador) — senha `123456` |
| Produtos | 12 produtos (ids 1 a 12), sendo 11 ativos e o 12 inativo; ids 1, 7, 9 e 11 têm `estoqueMinimo` próprio |
| Configuração | `estoque_minimo_global = 5` e `exibir_fotos = 1` |
| Movimentações | 4 registros históricos com `quantidade` absoluta, `variacao` assinada e `justificativa` nula |

Os ids e valores replicam `planejamento/Mocks/db.seed.json`, que é o contrato executável usado
pelo front-end. O script é **idempotente**: reaplicá-lo não duplica registros. Como o entrypoint
do MySQL só executa scripts de init em volume vazio, use um dos comandos abaixo quando precisar:

```powershell
# reaplicar o seed em um banco já existente (não duplica nada)
cmd /c "docker compose exec -T -e MYSQL_PWD=root_password db mysql -uroot controle_estoque < database\zz-seed-demo.sql"

# recriar o banco e rodar schema + seed do zero
docker compose down -v
docker compose up --build
```

> Dados apenas para desenvolvimento local. Para desativar, remova a linha
> `COPY zz-seed-demo.sql` do `database/Dockerfile`.

### API (resumo)

| Método | Rota | Descrição |
|---|---|---|
| POST | `/auth/login` | Autenticar usuário ativo por e-mail e senha (sem token) |
| GET | `/configuracoes` | Consultar mínimo global, exibição de fotos e contagem de mínimos próprios |
| GET | `/movimentacoes` | Listar movimentações (`produtoId`, `usuarioId`, `tipo`, `de`, `ate`, `limite`) |
| POST | `/movimentacoes` | Registrar `ENTRADA`, `SAIDA` ou `AJUSTE` |
| GET | `/usuarios` | Listar usuários (`?incluirInativos=true` opcional) |
| GET | `/usuarios/{id}` | Buscar usuário por id |
| POST | `/usuarios` | Cadastrar usuário (nome, e-mail, senha, perfil) |
| PUT | `/usuarios/{id}` | Atualizar nome, e-mail e perfil — sem senha |
| DELETE | `/usuarios/{id}` | Inativar usuário (exclusão lógica) |
| GET | `/fornecedores` | Listar fornecedores (`?incluirInativos=true` opcional) |
| GET | `/fornecedores/{id}` | Buscar fornecedor por id |
| POST | `/fornecedores` | Cadastrar fornecedor (nome + CNPJ) |
| PUT | `/fornecedores/{id}` | Atualizar fornecedor |
| DELETE | `/fornecedores/{id}` | Inativar fornecedor |
| GET | `/produtos` | Listar produtos (`?incluirInativos=true` opcional) |
| GET | `/produtos/{id}` | Buscar produto por id |
| POST | `/produtos` | Cadastrar produto (exige `usuarioId`) |
| PUT | `/produtos/{id}` | Atualizar dados cadastrais — não altera quantidade |
| PATCH | `/produtos/{id}/quantidade` | Ajuste de saldo por compatibilidade (`usuarioId` + `justificativa`) |
| DELETE | `/produtos/{id}` | Inativar produto (exclusão lógica) |

Regras de contrato relevantes:

- **Formato de erro** — toda falha responde `{ "status", "mensagem", "erros": [{ "campo", "mensagem" }] }`.
  `status` repete o código HTTP; `erros` é vazio quando a falha não é de campo. Validação responde
  `400`, login inválido `401`, recurso inexistente `404`, conflito de unicidade `409`, regra de
  negócio `422` e falha inesperada `500` sem stack trace, SQL ou nome de tabela.
- **`POST /auth/login`** — recebe `email` e `senha`, exige usuário ativo e devolve `id`, `nome`,
  `email`, `perfilAcesso` e `ativo`, sem `senha`. Credencial inválida responde `401` com
  `Usuário ou senha incorretos.`. Não há JWT nesta fase (decisão de POC).
- **Produto** — `sku`, `nome`, `categoriaId`, `quantidade` e `usuarioId` são obrigatórios no
  `POST` (o `usuarioId` é exigido inclusive com quantidade zero). `peso` (kg, até 3 casas),
  `tamanho`, `descricao`, `preco`, `marca`, `fornecedorId`, `localizacoes`, `fotoUrl` (até 500
  caracteres) e `estoqueMinimo` (inteiro maior ou igual a zero ou nulo) são opcionais.
  `quantidade > 0` gera uma movimentação `SALDO_INICIAL` com `observacao` fixa; `quantidade = 0`
  não gera nada. A resposta inclui `estoqueMinimoEfetivo`, que usa o mínimo próprio quando
  preenchido e o `estoqueMinimoGlobal` quando nulo.
- **`PUT /produtos/{id}`** não recebe `quantidade` nem `usuarioId` e nunca altera o saldo.
  Lista de localizações ausente ou `null` limpa as localizações e `fornecedorId` nulo remove
  o vínculo. Produto inativo responde `422`.
- **`PATCH /produtos/{id}/quantidade`** é o caminho de compatibilidade: exige `quantidade`,
  `usuarioId` e `justificativa`, grava a justificativa em `observacao` e registra `AJUSTE` na
  mesma transação, **inclusive quando o valor não muda** (variação zero). Para entradas, saídas
  e motivos, prefira `POST /movimentacoes`. Produto ou usuário inativo responde `422`.
- **`POST /movimentacoes`** — aceita `ENTRADA` (soma), `SAIDA` (subtrai) e `AJUSTE` (substitui).
  `ENTRADA`/`SAIDA` exigem quantidade maior que zero; `AJUSTE` aceita zero ou mais e exige
  `observacao` com pelo menos 10 caracteres. `SAIDA` exige `motivo`; `motivo: OUTRO` exige
  `observacao` em qualquer tipo. `observacao` tem no máximo 255 caracteres e `documento` no
  máximo 120. Saída acima do saldo responde `422` com a mensagem contextual.
- **`GET /movimentacoes`** — ordena da mais recente para a mais antiga e aceita `produtoId`,
  `usuarioId`, `tipo`, `de`, `ate` (datas inclusivas em `AAAA-MM-DD`) e `limite`. Parâmetros
  inválidos respondem `400` com `erros[].campo`.
- **`GET /configuracoes`** — devolve `estoqueMinimoGlobal`, `exibirFotos` e
  `produtosComMinimoProprio`; sem linha persistida usa `5` e `true`. Nesta fase não há escrita
  da configuração.
- **Fornecedor** — `cnpj` é armazenado somente com dígitos, deve ter exatamente 14 caracteres
  e é único; os dígitos verificadores não são validados.
- **Usuário** — `POST` exige `senha`; `PUT` não a exige nem a altera. A senha é gravada em
  texto puro neste MVP e nunca aparece nas respostas.
- Campos desconhecidos no JSON são ignorados pelo Jackson: `quantidade` no `PUT` de produto e
  `senha` no `PUT` de usuário não alteram estado.

Documentação interativa completa em **`http://localhost:8080/swagger-ui.html`**.

---

## Rodar com Docker (banco + backend em containers separados)

### Pré-requisitos

- Docker Desktop (ou Docker Engine + Compose v2) com o daemon ligado.
- Porta **8080** livre no host.
- Não é preciso ter Java, Maven ou MySQL instalados.

### Passo a passo

```powershell
# 1. Criar o arquivo de ambiente (nunca versionado)
Copy-Item .env.example .env    # no Linux/macOS: cp .env.example .env

# 2. Subir banco + backend (build das duas imagens)
docker compose up --build

# 3. Smoke: o backend responde na porta 8080
curl http://localhost:8080/swagger-ui.html
```

O que acontece nessa subida:

1. Build das imagens `blox-db` (banco + script de init) e `blox-backend` (jar compilado em
   multi-stage) — na primeira vez demora (downloads do Maven); depois usa cache.
2. O container `db` sobe, inicializa o volume com o script SQL e passa no healthcheck.
3. O container `backend` só então inicia, conecta em `jdbc:mysql://db:3306/controle_estoque`
   com `ddl-auto=validate` e publica a API na porta 8080.

### Variáveis de ambiente (`.env`)

Copie `.env.example` para `.env` e ajuste os valores:

| Variável | Modelo | Descrição |
|---|---|---|
| `DB_USERNAME` | `blox_user` | Usuário MySQL dedicado criado no container do banco |
| `DB_PASSWORD` | `blox_password` | Senha do usuário dedicado (usado pelo backend) |
| `DB_ROOT_PASSWORD` | `root_password` | Senha do root **interna ao container** (apenas init/healthcheck) |
| `DB_URL` | `jdbc:mysql://db:3306/controle_estoque` | URL JDBC usada pelo backend em Docker |

> O compose usa `${VAR:?...}` nas credenciais: **sem o `.env`, a subida falha de propósito**
> pedindo para copiar o `.env.example` — nenhum segredo é versionado.

### Comandos úteis

```powershell
docker compose logs -f backend      # acompanhar logs
docker compose ps                   # status dos containers
docker compose down                 # parar (preserva os dados do banco)
docker compose down -v              # parar e APAGAR o volume (reinicializa o schema)
docker compose up --build           # subir de novo (rebuild se mudou código/Dockerfile)
```

### Solução de problemas

| Sintoma | Causa / solução |
|---|---|
| Falha `Defina DB_USERNAME no arquivo .env` | Falta o `.env` — rode `Copy-Item .env.example .env` |
| Init do banco falhou, volume meio-criado | Volume em estado inconsistente — `docker compose down -v` e suba de novo |
| `Port 8080 was already in use` | Outro processo na 8080 — libere a porta ou pare o outro backend |
| Quer acessar o MySQL de fora (DBeaver) | Descomente o bloco `ports` do serviço `db` no `docker-compose.yml` (ex.: `3307:3306`) |
| Mudou o script SQL e quer re-executar o init | `docker compose down -v` (o init só roda em volume novo) |

---

## Rodar sem Docker (desenvolvimento local)

Requisitos: Java 21, Maven (ou use o wrapper) e um MySQL em `localhost:3306` com o schema
`controle_estoque` criado (aplique `database/script-banco-v4.sql`).

Sem nenhuma variável `DB_*` definida, o backend usa os defaults do `application.properties`
(`localhost:3306/controle_estoque`, usuário `root`, senha vazia):

```powershell
cd backend/blox
./mvnw.cmd spring-boot:run     # Linux/macOS: ./mvnw spring-boot:run
```

Se o seu MySQL local usa outra senha/usuário, sobrescreva por variáveis de ambiente:

```powershell
$env:DB_USERNAME="root"
$env:DB_PASSWORD="sua_senha"
./mvnw.cmd spring-boot:run
```

Também é possível sobrescrever o URL completo: `DB_URL`.

---

## Build do backend sem subir a API

```powershell
cd backend/blox
./mvnw.cmd package             # gera target/blox-0.0.1-SNAPSHOT.jar
```
