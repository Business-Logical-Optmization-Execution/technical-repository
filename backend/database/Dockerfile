# Imagem do banco do Portal Blox (MySQL 8.4)
# O entrypoint oficial executa /docker-entrypoint-initdb.d/*.sql
# apenas na PRIMEIRA subida com o volume vazio.
FROM mysql:8.4

COPY script-banco-v4.sql /docker-entrypoint-initdb.d/
# Dados de demonstracao (idempotentes) para testar a API localmente.
COPY zz-seed-demo.sql /docker-entrypoint-initdb.d/

# Saúde do container: usada pelo compose (depends_on: service_healthy)
# e visível também em "docker run" isolado.
HEALTHCHECK --interval=5s --timeout=3s --start-period=30s --retries=20 \
  CMD mysqladmin ping -h localhost -uroot -p"$MYSQL_ROOT_PASSWORD" --silent || exit 1
