-- =====================================================================
-- Portal Blox - script de banco v5
-- Fonte canonica do schema `controle_estoque` (MySQL 8.4).
--
-- Diferencas em relacao ao v4 (mantido como historico):
--   * nova tabela avisos_falta: no maximo um aviso aberto por produto,
--     garantido pela aplicacao com bloqueio do produto
--
-- O Hibernate roda com ddl-auto=validate: este script e a unica fonte
-- de verdade do schema.
-- =====================================================================

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema controle_estoque
-- -----------------------------------------------------
CREATE DATABASE IF NOT EXISTS `controle_estoque` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci ;
CREATE SCHEMA IF NOT EXISTS `controle_estoque` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci ;
USE `controle_estoque` ;

-- -----------------------------------------------------
-- Table `controle_estoque`.`categorias`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `controle_estoque`.`categorias` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(60) NOT NULL,
  `descricao` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `controle_estoque`.`fornecedores`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `controle_estoque`.`fornecedores` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(180) NOT NULL,
  `cnpj` VARCHAR(14) NOT NULL,
  `ativo` TINYINT(1) NOT NULL,
  `criado_em` DATETIME NOT NULL,
  `atualizado_em` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `cnpj_UNIQUE` (`cnpj` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `controle_estoque`.`produtos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `controle_estoque`.`produtos` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `sku` VARCHAR(60) NOT NULL,
  `nome` VARCHAR(180) NOT NULL,
  `categoria_id` INT NOT NULL,
  `fornecedor_id` INT NULL DEFAULT NULL,
  `quantidade` INT NOT NULL,
  `preco` DECIMAL(14,2) NULL DEFAULT NULL,
  `peso` DECIMAL(10,3) NULL DEFAULT NULL,
  `tamanho` VARCHAR(60) NULL DEFAULT NULL,
  `descricao` VARCHAR(500) NULL DEFAULT NULL,
  `marca` VARCHAR(120) NULL DEFAULT NULL,
  `foto_url` VARCHAR(500) NULL DEFAULT NULL,
  `estoque_minimo` INT NULL DEFAULT NULL,
  `ativo` TINYINT(1) NOT NULL,
  `criado_em` DATETIME NOT NULL,
  `atualizado_em` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `sku_UNIQUE` (`sku` ASC) VISIBLE,
  INDEX `fk_produtos_categoria` (`categoria_id` ASC) VISIBLE,
  INDEX `fk_produtos_fornecedor` (`fornecedor_id` ASC) VISIBLE,
  CONSTRAINT `fk_produtos_categoria`
    FOREIGN KEY (`categoria_id`)
    REFERENCES `controle_estoque`.`categorias` (`id`),
  CONSTRAINT `fk_produtos_fornecedor`
    FOREIGN KEY (`fornecedor_id`)
    REFERENCES `controle_estoque`.`fornecedores` (`id`),
  CONSTRAINT `ck_produtos_quantidade`
    CHECK (`quantidade` >= 0),
  CONSTRAINT `ck_produtos_estoque_minimo`
    CHECK (`estoque_minimo` IS NULL OR `estoque_minimo` >= 0))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `controle_estoque`.`produto_localizacoes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `controle_estoque`.`produto_localizacoes` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `produto_id` INT NOT NULL,
  `nome` VARCHAR(120) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `produto_localizacao_UNIQUE` (`produto_id` ASC, `nome` ASC) VISIBLE,
  INDEX `fk_produto_localizacoes_produto` (`produto_id` ASC) VISIBLE,
  CONSTRAINT `fk_produto_localizacoes_produto`
    FOREIGN KEY (`produto_id`)
    REFERENCES `controle_estoque`.`produtos` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `controle_estoque`.`perfis_acesso`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `controle_estoque`.`perfis_acesso` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(60) NOT NULL,
  `descricao` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `controle_estoque`.`usuarios`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `controle_estoque`.`usuarios` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(120) NOT NULL,
  `email` VARCHAR(180) NOT NULL,
  `perfil_acesso_id` INT NOT NULL,
  `criado_em` DATETIME NOT NULL,
  `atualizado_em` DATETIME NOT NULL,
  `senha` VARCHAR(150) NULL DEFAULT NULL,
  `ativo` TINYINT(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`),
  INDEX `fk_usuarios_perfil_acesso` (`perfil_acesso_id` ASC) VISIBLE,
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE,
  CONSTRAINT `fk_usuarios_perfil_acesso`
    FOREIGN KEY (`perfil_acesso_id`)
    REFERENCES `controle_estoque`.`perfis_acesso` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `controle_estoque`.`configuracoes`
-- Linha unica (id = 1) com os parametros globais da loja.
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `controle_estoque`.`configuracoes` (
  `id` INT NOT NULL,
  `estoque_minimo_global` INT NOT NULL,
  `exibir_fotos` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `ck_configuracoes_id`
    CHECK (`id` = 1),
  CONSTRAINT `ck_configuracoes_estoque_minimo`
    CHECK (`estoque_minimo_global` >= 0))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `controle_estoque`.`movimentacoes_estoque`
-- Ledger imutavel: cada linha registra uma alteracao de saldo.
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `controle_estoque`.`movimentacoes_estoque` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `produto_id` INT NOT NULL,
  `usuario_id` INT NOT NULL,
  `tipo` VARCHAR(20) NOT NULL,
  `quantidade` INT NOT NULL,
  `variacao` INT NOT NULL,
  `quantidade_antes` INT NOT NULL,
  `quantidade_depois` INT NOT NULL,
  `motivo` VARCHAR(20) NULL DEFAULT NULL,
  `observacao` VARCHAR(255) NULL DEFAULT NULL,
  `documento` VARCHAR(120) NULL DEFAULT NULL,
  `justificativa` VARCHAR(255) NULL DEFAULT NULL,
  `origem` VARCHAR(20) NOT NULL,
  `estorno_de_id` INT NULL DEFAULT NULL,
  `criado_em` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_mov_estoque_produto` (`produto_id` ASC) VISIBLE,
  INDEX `fk_mov_estoque_usuario` (`usuario_id` ASC) VISIBLE,
  INDEX `fk_mov_estoque_estorno` (`estorno_de_id` ASC) VISIBLE,
  CONSTRAINT `fk_mov_estoque_produto`
    FOREIGN KEY (`produto_id`)
    REFERENCES `controle_estoque`.`produtos` (`id`),
  CONSTRAINT `fk_mov_estoque_usuario`
    FOREIGN KEY (`usuario_id`)
    REFERENCES `controle_estoque`.`usuarios` (`id`),
  CONSTRAINT `fk_mov_estoque_estorno`
    FOREIGN KEY (`estorno_de_id`)
    REFERENCES `controle_estoque`.`movimentacoes_estoque` (`id`),
  CONSTRAINT `ck_mov_estoque_tipo`
    CHECK (`tipo` IN ('SALDO_INICIAL', 'AJUSTE', 'ENTRADA', 'SAIDA', 'ESTORNO')),
  CONSTRAINT `ck_mov_estoque_origem`
    CHECK (`origem` IN ('MANUAL', 'BLING', 'IMPORTACAO')),
  CONSTRAINT `ck_mov_estoque_motivo`
    CHECK (`motivo` IS NULL OR `motivo` IN ('PERDA', 'AVARIA', 'USO_INTERNO', 'VENDA_BALCAO', 'OUTRO')),
  CONSTRAINT `ck_mov_estoque_quantidade`
    CHECK (`quantidade` >= 0),
  CONSTRAINT `ck_mov_estoque_quantidade_antes`
    CHECK (`quantidade_antes` >= 0),
  CONSTRAINT `ck_mov_estoque_quantidade_depois`
    CHECK (`quantidade_depois` >= 0))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `controle_estoque`.`avisos_falta`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `controle_estoque`.`avisos_falta` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `produto_id` INT NOT NULL,
  `usuario_id` INT NOT NULL,
  `status` VARCHAR(20) NOT NULL,
  `observacao` VARCHAR(255) NULL DEFAULT NULL,
  `quantidade_avisos` INT NOT NULL,
  `criado_em` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_avisos_falta_produto_status` (`produto_id` ASC, `status` ASC) VISIBLE,
  INDEX `fk_avisos_falta_usuario` (`usuario_id` ASC) VISIBLE,
  CONSTRAINT `fk_avisos_falta_produto`
    FOREIGN KEY (`produto_id`)
    REFERENCES `controle_estoque`.`produtos` (`id`),
  CONSTRAINT `fk_avisos_falta_usuario`
    FOREIGN KEY (`usuario_id`)
    REFERENCES `controle_estoque`.`usuarios` (`id`),
  CONSTRAINT `ck_avisos_falta_status`
    CHECK (`status` IN ('ABERTO', 'RESOLVIDO')),
  CONSTRAINT `ck_avisos_falta_quantidade`
    CHECK (`quantidade_avisos` >= 1))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `controle_estoque`.`campanhas`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `controle_estoque`.`campanhas` (
  `id` INT NOT NULL,
  `titulo` VARCHAR(45) NULL,
  `mensagem` VARCHAR(255) NULL,
  `imgUrl` VARCHAR(150) NULL,
  `usuarios_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_campanhas_usuarios1_idx` (`usuarios_id` ASC) VISIBLE,
  CONSTRAINT `fk_campanhas_usuarios1`
    FOREIGN KEY (`usuarios_id`)
    REFERENCES `controle_estoque`.`usuarios` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Carga inicial: perfis de acesso e categorias.
-- Todo usuario precisa de um perfil existente e todo produto de uma
-- categoria existente. Ids fixos para casar com o seed de demonstracao.
-- -----------------------------------------------------
INSERT INTO `controle_estoque`.`perfis_acesso` (`id`, `nome`, `descricao`) VALUES
  (1, 'Administrador', 'Acesso total ao sistema'),
  (2, 'Operador', 'Acesso as operacoes de estoque')
ON DUPLICATE KEY UPDATE `nome` = VALUES(`nome`), `descricao` = VALUES(`descricao`);

INSERT INTO `controle_estoque`.`categorias` (`id`, `nome`, `descricao`) VALUES
  (1, 'Ferragens', 'Parafusos, porcas e similares'),
  (2, 'Fixacao', 'Itens de fixacao em geral'),
  (3, 'Eletrica', 'Componentes e materiais eletricos')
ON DUPLICATE KEY UPDATE `nome` = VALUES(`nome`), `descricao` = VALUES(`descricao`);
