# Portal Blox — Technical Repository

API REST de **controle de estoque e produtos** do projeto Portal Blox: cadastro de produtos,
usuários e perfis de acesso, com movimentações e saldos de estoque. Este repositório contém o
**backend Spring Boot** e o **banco MySQL**, dockerizados em duas imagens independentes
orquestradas por Docker Compose — qualquer máquina com Docker sobe o ambiente completo sem
instalar Java, Maven ou MySQL localmente.

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
  `database/script-banco-v2.sql` (criação idempotente do schema `controle_estoque`, 9 tabelas e
  seed de perfis/categorias). Em volumes já inicializados o script **não** re-executa.
- **Ordem de subida garantida**: o `db` tem `healthcheck` (mysqladmin ping) e o `backend` usa
  `depends_on: condition: service_healthy` — só sobe depois que o banco responde.
- **Conexão por variáveis de ambiente**: o `backend` resolve o banco pelo hostname `db` (rede do
  compose) via `DB_URL`/`DB_USERNAME`/`DB_PASSWORD`; o `application.properties` guarda defaults
  locais para rodar fora do Docker.
- **Sem conflito com MySQL local**: a porta 3306 do container **não** é publicada no host.

### Estrutura do repositório

```
technical-repository/
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
│   ├── Dockerfile              # mysql:8.4 + script de init
│   ├── .dockerignore
│   └── script-banco-v2.sql     # schema + seed

```

### Banco de dados

Schema `controle_estoque` com as tabelas: `usuarios`, `perfis_acesso`, `produtos`,
`produtos_categorias`, `categorias`, `fornecedores`, `estoque_saldos`,
`movimentacoes_estoque` e `campanhas`. O Hibernate roda com `ddl-auto=validate` — o schema é
gerenciado **somente** pelo script SQL.

### API (resumo)

| Método | Rota | Descrição |
|---|---|---|
| GET | `/usuarios` | Listar usuários |
| GET | `/usuarios/{id}` | Buscar usuário por id |
| POST | `/usuarios` | Cadastrar usuário |
| PUT | `/usuarios/{id}` | Atualizar usuário |
| DELETE | `/usuarios/{id}` | Excluir usuário |
| GET | `/produtos` | Listar produtos |
| GET | `/produtos/{id}` | Buscar produto por id |
| POST | `/produtos` | Cadastrar produto |
| PUT | `/produtos/{id}` | Atualizar produto |
| PATCH | `/produtos/{id}/quantidade` | Ajustar quantidade em estoque |
| DELETE | `/produtos/{id}` | Excluir produto |

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
`controle_estoque` criado (aplique `database/script-banco-v2.sql`).

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
