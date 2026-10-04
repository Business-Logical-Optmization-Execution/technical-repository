

SHOW TABLES;
describe usuarios;

-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema controle_estoque
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema controle_estoque
-- -----------------------------------------------------
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
  `razao_social` VARCHAR(180) NOT NULL,
  `documento` VARCHAR(14) NULL DEFAULT NULL,
  `email` VARCHAR(180) NULL DEFAULT NULL,
  `telefone` VARCHAR(30) NULL DEFAULT NULL,
  `ativo` TINYINT(1) NOT NULL,
  `criado_em` DATETIME NOT NULL,
  `atualizado_em` DATETIME NOT NULL,
  PRIMARY KEY (`id`))
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
  `quantidade` INT NOT NULL,
  `preco` DECIMAL(14,2) NULL DEFAULT NULL,
  `ativo` TINYINT(1) NOT NULL,
  `criado_em` DATETIME NOT NULL,
  `atualizado_em` DATETIME NOT NULL,
  `fornecedores_id` INT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_produtos_fornecedores1` (`fornecedores_id` ASC) VISIBLE,
  UNIQUE INDEX `sku_UNIQUE` (`sku` ASC) VISIBLE,
  CONSTRAINT `fk_produtos_fornecedores1`
    FOREIGN KEY (`fornecedores_id`)
    REFERENCES `controle_estoque`.`fornecedores` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `controle_estoque`.`estoque_saldos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `controle_estoque`.`estoque_saldos` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `produto_id` INT NOT NULL,
  `quantidade` DECIMAL(14,3) NOT NULL,
  `atualizado_em` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_estoque_saldos_produto` (`produto_id` ASC) VISIBLE,
  CONSTRAINT `fk_estoque_saldos_produto`
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
-- Table `controle_estoque`.`movimentacoes_estoque`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `controle_estoque`.`movimentacoes_estoque` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `produto_id` INT NOT NULL,
  `tipo_mov` ENUM('ENTRADA', 'SAIDA', 'AJUSTE') NOT NULL,
  `quantidade` INT NOT NULL,
  `data_mov` DATETIME NOT NULL,
  `usuario_id` INT NOT NULL,
  `fornecedor_id` INT NULL DEFAULT NULL,
  `observacao` VARCHAR(255) NULL DEFAULT NULL,
  `criado_em` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_mov_estoque_produto` (`produto_id` ASC) VISIBLE,
  INDEX `fk_mov_estoque_usuario` (`usuario_id` ASC) VISIBLE,
  INDEX `fk_mov_estoque_fornecedor` (`fornecedor_id` ASC) VISIBLE,
  CONSTRAINT `fk_mov_estoque_fornecedor`
    FOREIGN KEY (`fornecedor_id`)
    REFERENCES `controle_estoque`.`fornecedores` (`id`),
  CONSTRAINT `fk_mov_estoque_produto`
    FOREIGN KEY (`produto_id`)
    REFERENCES `controle_estoque`.`produtos` (`id`),
  CONSTRAINT `fk_mov_estoque_usuario`
    FOREIGN KEY (`usuario_id`)
    REFERENCES `controle_estoque`.`usuarios` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `controle_estoque`.`produtos_categorias`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `controle_estoque`.`produtos_categorias` (
  `produtos_id` INT NOT NULL,
  `categorias_id` INT NOT NULL,
  `id` INT NOT NULL AUTO_INCREMENT,
  INDEX `fk_produtos_has_categorias_categorias1` (`categorias_id` ASC) VISIBLE,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_produtos_has_categorias_categorias1`
    FOREIGN KEY (`categorias_id`)
    REFERENCES `controle_estoque`.`categorias` (`id`),
  CONSTRAINT `fk_produtos_has_categorias_produtos1`
    FOREIGN KEY (`produtos_id`)
    REFERENCES `controle_estoque`.`produtos` (`id`))
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
-- Carga inicial (decisão em aberto #15): perfis de acesso e categorias
-- Registros mínimos exigidos pela v1: todo usuário precisa de um perfil
-- existente e todo produto de categorias existentes (RN-08, RN-10).
-- -----------------------------------------------------
INSERT INTO `controle_estoque`.`perfis_acesso` (`nome`, `descricao`) VALUES
  ('Administrador', 'Acesso total ao sistema'),
  ('Operador', 'Acesso as operacoes de estoque');

INSERT INTO `controle_estoque`.`categorias` (`nome`, `descricao`) VALUES
  ('Ferragens', 'Parafusos, porcas e similares'),
  ('Fixacao', 'Itens de fixacao em geral'),
  ('Eletrica', 'Componentes e materiais eletricos');


