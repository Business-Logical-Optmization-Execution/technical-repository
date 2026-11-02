-- =====================================================================
-- Portal Blox - dados de demonstracao para testar a API
--
-- Executado automaticamente pelo container do MySQL depois do
-- script-banco-v3.sql, quando o volume e inicializado (primeira subida
-- ou apos "docker compose down -v").
--
-- O script e idempotente: pode ser reaplicado em um banco ja populado
-- sem duplicar registros. Para reaplicar manualmente:
--   docker compose exec -T db sh -c 'mysql -uroot -p"$MYSQL_ROOT_PASSWORD" controle_estoque' < database/zz-seed-demo.sql
--
-- Dados puramente locais de desenvolvimento. Nao use em producao.
-- =====================================================================

USE `controle_estoque`;

-- -----------------------------------------------------
-- Categorias complementares (as tres do v3 ja existem)
-- -----------------------------------------------------
INSERT INTO `categorias` (`nome`, `descricao`)
SELECT 'Parafusos', 'Parafusos, chumbadores e similares' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `categorias` WHERE `nome` = 'Parafusos');

INSERT INTO `categorias` (`nome`, `descricao`)
SELECT 'Porcas e Arruelas', 'Porcas, arruelas e contraporcas' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `categorias` WHERE `nome` = 'Porcas e Arruelas');


-- -----------------------------------------------------
-- Fornecedores (tres ativos e um inativo para testar filtros)
-- -----------------------------------------------------
INSERT INTO `fornecedores` (`nome`, `cnpj`, `ativo`, `criado_em`, `atualizado_em`)
SELECT 'Ferragens Silva', '11222333000181', 1, NOW(), NOW() FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `fornecedores` WHERE `cnpj` = '11222333000181');

INSERT INTO `fornecedores` (`nome`, `cnpj`, `ativo`, `criado_em`, `atualizado_em`)
SELECT 'Distribuidora Central', '22333444000102', 1, NOW(), NOW() FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `fornecedores` WHERE `cnpj` = '22333444000102');

INSERT INTO `fornecedores` (`nome`, `cnpj`, `ativo`, `criado_em`, `atualizado_em`)
SELECT 'Aco Forte', '33444555000113', 1, NOW(), NOW() FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `fornecedores` WHERE `cnpj` = '33444555000113');

INSERT INTO `fornecedores` (`nome`, `cnpj`, `ativo`, `criado_em`, `atualizado_em`)
SELECT 'Fornecedor Desativado', '44555666000124', 0, NOW(), NOW() FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `fornecedores` WHERE `cnpj` = '44555666000124');


-- -----------------------------------------------------
-- Usuarios (dois ativos e um inativo; senha em texto puro no MVP)
-- -----------------------------------------------------
INSERT INTO `usuarios` (`nome`, `email`, `perfil_acesso_id`, `criado_em`, `atualizado_em`, `senha`, `ativo`)
SELECT 'Ana Souza', 'ana.souza@portalblox.test',
       (SELECT `id` FROM `perfis_acesso` WHERE `nome` = 'Administrador'), NOW(), NOW(), 'senha123', 1
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `usuarios` WHERE `email` = 'ana.souza@portalblox.test');

INSERT INTO `usuarios` (`nome`, `email`, `perfil_acesso_id`, `criado_em`, `atualizado_em`, `senha`, `ativo`)
SELECT 'Bruno Lima', 'bruno.lima@portalblox.test',
       (SELECT `id` FROM `perfis_acesso` WHERE `nome` = 'Operador'), NOW(), NOW(), 'senha123', 1
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `usuarios` WHERE `email` = 'bruno.lima@portalblox.test');

INSERT INTO `usuarios` (`nome`, `email`, `perfil_acesso_id`, `criado_em`, `atualizado_em`, `senha`, `ativo`)
SELECT 'Carla Dias', 'carla.dias@portalblox.test',
       (SELECT `id` FROM `perfis_acesso` WHERE `nome` = 'Operador'), NOW(), NOW(), 'senha123', 0
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `usuarios` WHERE `email` = 'carla.dias@portalblox.test');


-- -----------------------------------------------------
-- Produtos
-- Um tamanho vendavel = um produto, com SKU e saldo proprios.
-- Inclui um produto com saldo zero e um inativo para cobrir os filtros.
-- -----------------------------------------------------
INSERT INTO `produtos` (`sku`, `nome`, `categoria_id`, `fornecedor_id`, `quantidade`, `preco`, `peso`, `tamanho`, `descricao`, `marca`, `ativo`, `criado_em`, `atualizado_em`)
SELECT 'PF-M6-40', 'Parafuso Sextavado M6 x 40',
       (SELECT `id` FROM `categorias` WHERE `nome` = 'Parafusos'),
       (SELECT `id` FROM `fornecedores` WHERE `cnpj` = '11222333000181'),
       120, 0.85, 0.012, 'M6 x 40 mm', 'Parafuso sextavado com porca', 'Silva', 1, NOW(), NOW()
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `produtos` WHERE `sku` = 'PF-M6-40');

INSERT INTO `produtos` (`sku`, `nome`, `categoria_id`, `fornecedor_id`, `quantidade`, `preco`, `peso`, `tamanho`, `descricao`, `marca`, `ativo`, `criado_em`, `atualizado_em`)
SELECT 'PF-M8-50', 'Parafuso Sextavado M8 x 50',
       (SELECT `id` FROM `categorias` WHERE `nome` = 'Parafusos'),
       (SELECT `id` FROM `fornecedores` WHERE `cnpj` = '11222333000181'),
       80, 1.20, 0.024, 'M8 x 50 mm', 'Parafuso sextavado com porca', 'Silva', 1, NOW(), NOW()
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `produtos` WHERE `sku` = 'PF-M8-50');

INSERT INTO `produtos` (`sku`, `nome`, `categoria_id`, `fornecedor_id`, `quantidade`, `preco`, `peso`, `tamanho`, `descricao`, `marca`, `ativo`, `criado_em`, `atualizado_em`)
SELECT 'AR-3-8', 'Arruela Lisa 3/8',
       (SELECT `id` FROM `categorias` WHERE `nome` = 'Porcas e Arruelas'),
       (SELECT `id` FROM `fornecedores` WHERE `cnpj` = '22333444000102'),
       200, 0.15, 0.004, '3/8 polegadas', 'Arruela lisa zincada', 'Central', 1, NOW(), NOW()
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `produtos` WHERE `sku` = 'AR-3-8');

INSERT INTO `produtos` (`sku`, `nome`, `categoria_id`, `fornecedor_id`, `quantidade`, `preco`, `peso`, `tamanho`, `descricao`, `marca`, `ativo`, `criado_em`, `atualizado_em`)
SELECT 'POR-M6', 'Porca Sextavada M6',
       (SELECT `id` FROM `categorias` WHERE `nome` = 'Porcas e Arruelas'),
       (SELECT `id` FROM `fornecedores` WHERE `cnpj` = '22333444000102'),
       150, 0.10, 0.003, 'M6', 'Porca sextavada zincada', 'Central', 1, NOW(), NOW()
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `produtos` WHERE `sku` = 'POR-M6');

INSERT INTO `produtos` (`sku`, `nome`, `categoria_id`, `fornecedor_id`, `quantidade`, `preco`, `peso`, `tamanho`, `descricao`, `marca`, `ativo`, `criado_em`, `atualizado_em`)
SELECT 'DOB-3', 'Dobradica 3 polegadas',
       (SELECT `id` FROM `categorias` WHERE `nome` = 'Ferragens'),
       (SELECT `id` FROM `fornecedores` WHERE `cnpj` = '33444555000113'),
       40, 6.50, 0.080, '3 polegadas', 'Dobradica de aco', 'Aco Forte', 1, NOW(), NOW()
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `produtos` WHERE `sku` = 'DOB-3');

INSERT INTO `produtos` (`sku`, `nome`, `categoria_id`, `fornecedor_id`, `quantidade`, `preco`, `peso`, `tamanho`, `descricao`, `marca`, `ativo`, `criado_em`, `atualizado_em`)
SELECT 'FITA-20', 'Fita Isolante 20 m',
       (SELECT `id` FROM `categorias` WHERE `nome` = 'Eletrica'),
       NULL,
       25, 4.90, 0.045, '20 m', 'Fita isolante preta', 'Volt', 1, NOW(), NOW()
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `produtos` WHERE `sku` = 'FITA-20');

INSERT INTO `produtos` (`sku`, `nome`, `categoria_id`, `fornecedor_id`, `quantidade`, `preco`, `peso`, `tamanho`, `descricao`, `marca`, `ativo`, `criado_em`, `atualizado_em`)
SELECT 'BROCA-6', 'Broca 6 mm',
       (SELECT `id` FROM `categorias` WHERE `nome` = 'Ferragens'),
       (SELECT `id` FROM `fornecedores` WHERE `cnpj` = '33444555000113'),
       30, 7.25, 0.035, '6 mm', 'Broca para concreto', 'Aco Forte', 1, NOW(), NOW()
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `produtos` WHERE `sku` = 'BROCA-6');

-- Produto ativo com saldo zero: testa o cadastro sem SALDO_INICIAL.
INSERT INTO `produtos` (`sku`, `nome`, `categoria_id`, `fornecedor_id`, `quantidade`, `preco`, `peso`, `tamanho`, `descricao`, `marca`, `ativo`, `criado_em`, `atualizado_em`)
SELECT 'CHV-1-4', 'Chave de Fenda 1/4',
       (SELECT `id` FROM `categorias` WHERE `nome` = 'Fixacao'),
       NULL,
       0, 12.00, 0.150, '1/4 polegadas', 'Chave de fenda simples', 'Tramontina', 1, NOW(), NOW()
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `produtos` WHERE `sku` = 'CHV-1-4');

-- Produto inativo: testa a listagem padrao e ?incluirInativos=true.
INSERT INTO `produtos` (`sku`, `nome`, `categoria_id`, `fornecedor_id`, `quantidade`, `preco`, `peso`, `tamanho`, `descricao`, `marca`, `ativo`, `criado_em`, `atualizado_em`)
SELECT 'PROD-ANTIGO', 'Produto Descontinuado',
       (SELECT `id` FROM `categorias` WHERE `nome` = 'Ferragens'),
       NULL,
       0, 3.00, 0.010, 'unico', 'Item fora de linha', 'Antiga', 0, NOW(), NOW()
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `produtos` WHERE `sku` = 'PROD-ANTIGO');


-- -----------------------------------------------------
-- Localizacoes fisicas (rotulos livres, sem saldo)
-- -----------------------------------------------------
INSERT INTO `produto_localizacoes` (`produto_id`, `nome`)
SELECT p.`id`, v.`localizacao`
FROM `produtos` p
JOIN (
  SELECT 'PF-M6-40' AS `sku`, 'Corredor A - Prateleira 1' AS `localizacao`
  UNION ALL SELECT 'PF-M6-40', 'Balcao'
  UNION ALL SELECT 'PF-M8-50', 'Corredor A - Prateleira 1'
  UNION ALL SELECT 'AR-3-8', 'Corredor B - Gaveta 3'
  UNION ALL SELECT 'POR-M6', 'Corredor B - Gaveta 3'
  UNION ALL SELECT 'POR-M6', 'Corredor B - Gaveta 4'
  UNION ALL SELECT 'DOB-3', 'Corredor C - Prateleira 2'
  UNION ALL SELECT 'FITA-20', 'Balcao'
  UNION ALL SELECT 'BROCA-6', 'Corredor C - Prateleira 3'
) v ON v.`sku` = p.`sku`
WHERE NOT EXISTS (
  SELECT 1 FROM `produto_localizacoes` l
  WHERE l.`produto_id` = p.`id` AND l.`nome` = v.`localizacao`
);


-- -----------------------------------------------------
-- Saldo inicial das cargas de demonstracao
-- Mesma justificativa usada pelo POST /produtos da API, para o ledger
-- ficar coerente com o que a aplicacao grava.
-- -----------------------------------------------------
INSERT INTO `movimentacoes_estoque`
  (`produto_id`, `usuario_id`, `tipo`, `quantidade`, `variacao`,
   `saldo_anterior`, `saldo_posterior`, `justificativa`, `origem`, `criado_em`)
SELECT p.`id`, u.`id`, 'SALDO_INICIAL', p.`quantidade`, p.`quantidade`,
       0, p.`quantidade`,
       'Saldo inicial informado no cadastro do produto', 'MANUAL', NOW()
FROM `produtos` p
JOIN `usuarios` u ON u.`email` = 'ana.souza@portalblox.test'
JOIN (
  SELECT 'PF-M6-40' AS `sku`
  UNION ALL SELECT 'PF-M8-50'
  UNION ALL SELECT 'AR-3-8'
  UNION ALL SELECT 'POR-M6'
  UNION ALL SELECT 'DOB-3'
  UNION ALL SELECT 'FITA-20'
  UNION ALL SELECT 'BROCA-6'
) v ON v.`sku` = p.`sku`
WHERE p.`quantidade` > 0
  AND NOT EXISTS (
    SELECT 1 FROM `movimentacoes_estoque` m
    WHERE m.`produto_id` = p.`id` AND m.`tipo` = 'SALDO_INICIAL'
  );
