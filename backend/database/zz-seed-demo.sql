-- =====================================================================
-- Portal Blox - dados de demonstracao para testar a API
--
-- Executado automaticamente pelo container do MySQL depois do
-- script-banco-v4.sql, quando o volume e inicializado (primeira subida
-- ou apos "docker compose down -v").
--
-- O script e idempotente: pode ser reaplicado em um banco ja populado
-- sem duplicar registros. Para reaplicar manualmente:
--   docker compose exec -T db sh -c 'mysql -uroot -p"$MYSQL_ROOT_PASSWORD" controle_estoque' < database/zz-seed-demo.sql
--
-- Os ids e valores replicam planejamento/Mocks/db.seed.json, que e o
-- contrato executavel usado pelo front-end.
-- Dados puramente locais de desenvolvimento. Nao use em producao.
-- =====================================================================

-- Este arquivo e UTF-8; sem isto o cliente mysql do entrypoint le como latin1
-- e grava os acentos com codificacao dupla ("Marcia" acentuada vira "MÃ¡rcia").
SET NAMES utf8mb4;

USE `controle_estoque`;

-- -----------------------------------------------------
-- Perfis de acesso (ids fixos usados pelos usuarios do seed)
-- -----------------------------------------------------
INSERT INTO `perfis_acesso` (`id`, `nome`, `descricao`) VALUES
  (1, 'Administrador', 'Acesso total ao sistema'),
  (2, 'Operador', 'Acesso as operacoes de estoque')
ON DUPLICATE KEY UPDATE `nome` = VALUES(`nome`), `descricao` = VALUES(`descricao`);


-- -----------------------------------------------------
-- Categorias
-- -----------------------------------------------------
INSERT INTO `categorias` (`id`, `nome`, `descricao`) VALUES
  (1, 'Ferragens', 'Parafusos, porcas e similares'),
  (2, 'Fixacao', 'Itens de fixacao em geral'),
  (3, 'Eletrica', 'Componentes e materiais eletricos')
ON DUPLICATE KEY UPDATE `nome` = VALUES(`nome`);


-- -----------------------------------------------------
-- Fornecedores (dois ativos e um inativo para testar filtros)
-- -----------------------------------------------------
INSERT INTO `fornecedores` (`id`, `nome`, `cnpj`, `ativo`, `criado_em`, `atualizado_em`) VALUES
  (1, 'Ferragens Silva', '11222333000181', 1, '2026-10-01 08:00:00', '2026-10-01 08:00:00'),
  (2, 'Distribuidora Central', '22333444000172', 1, '2026-10-01 08:00:00', '2026-10-01 08:00:00'),
  (3, 'Aco Forte', '33444555000163', 0, '2026-10-01 08:00:00', '2026-10-01 08:00:00')
ON DUPLICATE KEY UPDATE
  `nome` = VALUES(`nome`),
  `ativo` = VALUES(`ativo`),
  `criado_em` = VALUES(`criado_em`),
  `atualizado_em` = VALUES(`atualizado_em`);


-- -----------------------------------------------------
-- Usuarios (senha em texto puro no MVP)
-- -----------------------------------------------------
INSERT INTO `usuarios` (`id`, `nome`, `email`, `perfil_acesso_id`, `criado_em`, `atualizado_em`, `senha`, `ativo`) VALUES
  (1, 'Márcia', 'marcia@ottimos.com', 1, '2026-10-01 08:00:00', '2026-10-01 08:00:00', '123456', 1),
  (2, 'Carlos', 'carlos@ottimos.com', 1, '2026-10-01 08:00:00', '2026-10-01 08:00:00', '123456', 1),
  (3, 'Ronaldo', 'ronaldo@ottimos.com', 2, '2026-10-01 08:00:00', '2026-10-01 08:00:00', '123456', 1),
  (4, 'Valdir', 'valdir@ottimos.com', 2, '2026-10-01 08:00:00', '2026-10-01 08:00:00', '123456', 1)
ON DUPLICATE KEY UPDATE
  `nome` = VALUES(`nome`),
  `perfil_acesso_id` = VALUES(`perfil_acesso_id`),
  `senha` = VALUES(`senha`),
  `ativo` = VALUES(`ativo`);


-- -----------------------------------------------------
-- Produtos
-- Um tamanho vendavel = um produto, com SKU e saldo proprios.
-- Inclui um produto com saldo zero e um inativo para cobrir os filtros.
-- -----------------------------------------------------
INSERT INTO `produtos` (`id`, `sku`, `nome`, `categoria_id`, `fornecedor_id`, `quantidade`, `preco`, `peso`, `tamanho`, `descricao`, `marca`, `foto_url`, `estoque_minimo`, `ativo`, `criado_em`, `atualizado_em`) VALUES
  (1, '1001', 'Martelo', 1, 1, 2, 34.90, NULL, NULL, 'Martelo de unha 27 mm, cabo de madeira.', NULL, NULL, 10, 1, '2026-10-01 08:00:00', '2026-10-01 08:00:00'),
  (2, '1002', 'Chave de fenda', 1, 2, 2, 12.50, NULL, NULL, NULL, NULL, NULL, NULL, 1, '2026-10-01 08:00:00', '2026-10-01 08:00:00'),
  (3, '1003', 'Serrote', 1, 1, 0, 59.90, NULL, NULL, NULL, NULL, NULL, NULL, 1, '2026-10-01 08:00:00', '2026-10-01 08:00:00'),
  (4, '1004', 'Alicate universal', 1, 1, 3, 42.00, NULL, NULL, NULL, NULL, NULL, NULL, 1, '2026-10-01 08:00:00', '2026-10-01 08:00:00'),
  (5, '1005', 'Trena 5 m', 1, 1, 4, 28.90, NULL, NULL, NULL, NULL, NULL, NULL, 1, '2026-10-01 08:00:00', '2026-10-01 08:00:00'),
  (6, '1006', 'Espátula 8 cm', 1, NULL, 5, 9.90, NULL, NULL, NULL, NULL, NULL, NULL, 1, '2026-10-01 08:00:00', '2026-10-01 08:00:00'),
  (7, '1020', 'Parafuso sextavado 6 mm', 2, 2, 10, 0.45, NULL, NULL, 'Caixa avulsa, vendido por unidade.', NULL, NULL, 50, 1, '2026-10-01 08:00:00', '2026-10-01 08:00:00'),
  (8, '1543', 'Fita veda-rosca 18 mm', 2, 2, 0, 4.90, NULL, NULL, NULL, NULL, NULL, NULL, 1, '2026-10-01 08:00:00', '2026-10-01 08:00:00'),
  (9, '2001', 'Fio flexível 2,5 mm (metro)', 3, 2, 120, 3.20, NULL, NULL, NULL, NULL, NULL, 30, 1, '2026-10-01 08:00:00', '2026-10-01 08:00:00'),
  (10, '2002', 'Tomada 2P+T 10 A', 3, 2, 35, 11.90, NULL, NULL, NULL, NULL, NULL, NULL, 1, '2026-10-01 08:00:00', '2026-10-01 08:00:00'),
  (11, '1030', 'Bucha 8 mm', 2, 2, 400, 0.20, NULL, NULL, NULL, NULL, NULL, 100, 1, '2026-10-01 08:00:00', '2026-10-01 08:00:00'),
  (12, '1099', 'Martelo de borracha', 1, 1, 8, 27.50, NULL, NULL, NULL, NULL, NULL, NULL, 0, '2026-10-01 08:00:00', '2026-10-01 08:00:00')
ON DUPLICATE KEY UPDATE
  `sku` = VALUES(`sku`),
  `nome` = VALUES(`nome`),
  `categoria_id` = VALUES(`categoria_id`),
  `fornecedor_id` = VALUES(`fornecedor_id`),
  `quantidade` = VALUES(`quantidade`),
  `preco` = VALUES(`preco`),
  `peso` = VALUES(`peso`),
  `tamanho` = VALUES(`tamanho`),
  `descricao` = VALUES(`descricao`),
  `marca` = VALUES(`marca`),
  `foto_url` = VALUES(`foto_url`),
  `estoque_minimo` = VALUES(`estoque_minimo`),
  `ativo` = VALUES(`ativo`);


-- -----------------------------------------------------
-- Localizacoes fisicas (rotulos livres, sem saldo)
-- -----------------------------------------------------
INSERT INTO `produto_localizacoes` (`produto_id`, `nome`) VALUES
  (1, 'C3-P2'),
  (2, 'C3-P2'),
  (3, 'C4-P1'),
  (4, 'C3-P1'),
  (5, 'C2-P3'),
  (7, 'C1-P4'),
  (7, 'C1-P5'),
  (8, 'C5-P2'),
  (9, 'C6-P1'),
  (10, 'C6-P2'),
  (11, 'C1-P3'),
  (12, 'C3-P2')
ON DUPLICATE KEY UPDATE `nome` = VALUES(`nome`);


-- -----------------------------------------------------
-- Configuracao global da loja (linha unica id = 1)
-- -----------------------------------------------------
INSERT INTO `configuracoes` (`id`, `estoque_minimo_global`, `exibir_fotos`) VALUES
  (1, 5, 1)
ON DUPLICATE KEY UPDATE
  `estoque_minimo_global` = VALUES(`estoque_minimo_global`),
  `exibir_fotos` = VALUES(`exibir_fotos`);


-- -----------------------------------------------------
-- Movimentacoes de demonstracao
-- `quantidade` guarda o valor absoluto e `variacao` o sinal.
-- `justificativa` fica nula; a auditoria usa `observacao`.
-- -----------------------------------------------------
INSERT INTO `movimentacoes_estoque`
  (`id`, `produto_id`, `usuario_id`, `tipo`, `quantidade`, `variacao`,
   `quantidade_antes`, `quantidade_depois`, `motivo`, `observacao`, `documento`,
   `justificativa`, `origem`, `criado_em`) VALUES
  (1, 7, 3, 'ENTRADA', 10, 10, 0, 10, NULL, NULL, 'NF 12345', NULL, 'MANUAL', '2026-10-28 09:12:00'),
  (2, 3, 4, 'SAIDA', 1, -1, 1, 0, 'AVARIA', 'Lâmina quebrada', NULL, NULL, 'MANUAL', '2026-10-29 15:40:00'),
  (3, 1, 1, 'AJUSTE', 2, 2, 0, 2, NULL, 'Contagem da prateleira C3-P2 na quarta-feira', NULL, NULL, 'MANUAL', '2026-10-30 10:05:00'),
  (4, 10, 3, 'SAIDA', 3, -3, 38, 35, 'VENDA_BALCAO', NULL, NULL, NULL, 'MANUAL', '2026-10-30 16:22:00')
ON DUPLICATE KEY UPDATE
  `produto_id` = VALUES(`produto_id`),
  `usuario_id` = VALUES(`usuario_id`),
  `tipo` = VALUES(`tipo`),
  `quantidade` = VALUES(`quantidade`),
  `variacao` = VALUES(`variacao`),
  `quantidade_antes` = VALUES(`quantidade_antes`),
  `quantidade_depois` = VALUES(`quantidade_depois`),
  `motivo` = VALUES(`motivo`),
  `observacao` = VALUES(`observacao`),
  `documento` = VALUES(`documento`),
  `justificativa` = VALUES(`justificativa`),
  `origem` = VALUES(`origem`),
  `criado_em` = VALUES(`criado_em`);
