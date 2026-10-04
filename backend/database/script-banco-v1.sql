-- MySQL Workbench Forward Engineering (VERSÃO SIMPLIFICADA)
-- Apenas validações básicas: PK NOT NULL, FK e REFERENCES.
-- Sem UNSIGNED, sem UNIQUE INDEX, sem DEFAULT/ON UPDATE, sem ON DELETE/ON UPDATE em FK.

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8 ;

-- -----------------------------------------------------
-- Schema controle_estoque
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `controle_estoque` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci ;

USE `mydb` ;

-- -----------------------------------------------------
-- Table `mydb`.`perfil_acesso`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`perfil_acesso` (
  `id` INT NOT NULL,
  `nome` VARCHAR(150) NULL,
  `descricao` VARCHAR(150) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`usuario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`usuario` (
  `id` INT NOT NULL,
  `nome` VARCHAR(150) NULL,
  `email` VARCHAR(150) NULL,
  `criado_em` DATETIME NULL,
  `atualizado_em` DATETIME NULL,
  `perfil_acesso_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_usuario_perfil_acesso1`
    FOREIGN KEY (`perfil_acesso_id`)
    REFERENCES `mydb`.`perfil_acesso` (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`produtos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`produtos` (
  `id` INT NOT NULL,
  `nome` VARCHAR(150) NULL,
  `sku` VARCHAR(50) NULL,
  `categoria_id` INT NULL,
  `atualizado_em` DATETIME NULL,
  `fornecedor_id` INT NULL,
  `unidade` INT NULL,
  `preco` DECIMAL NULL,
  `ativo` TINYINT NULL,
  `produtocol` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`categorias`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`categorias` (
  `id` INT NOT NULL,
  `nome` VARCHAR(150) NULL,
  `descricao` VARCHAR(150) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`produtos_categorias`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`produtos_categorias` (
  `produto_id` INT NOT NULL,
  `categorias_id` INT NOT NULL,
  PRIMARY KEY (`produto_id`, `categorias_id`),
  CONSTRAINT `fk_produto_has_categorias_produto`
    FOREIGN KEY (`produto_id`)
    REFERENCES `mydb`.`produtos` (`id`),
  CONSTRAINT `fk_produto_has_categorias_categorias1`
    FOREIGN KEY (`categorias_id`)
    REFERENCES `mydb`.`categorias` (`id`))
ENGINE = InnoDB;

USE `controle_estoque` ;

-- -----------------------------------------------------
-- Table `controle_estoque`.`categorias`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `controle_estoque`.`categorias` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(120) NOT NULL,
  `descricao` VARCHAR(255) NULL,
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
  `documento` VARCHAR(14) NULL,
  `email` VARCHAR(180) NULL,
  `telefone` VARCHAR(30) NULL,
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
  `preco` DECIMAL(14,2) NULL,
  `ativo` TINYINT(1) NOT NULL,
  `criado_em` DATETIME NOT NULL,
  `atualizado_em` DATETIME NOT NULL,
  `fornecedores_id` INT NOT NULL,
  PRIMARY KEY (`id`),
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
  `descricao` VARCHAR(255) NULL,
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
  `senha` VARCHAR(150) NULL,
  PRIMARY KEY (`id`),
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
  `fornecedor_id` INT NULL,
  `observacao` VARCHAR(255) NULL,
  `criado_em` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_mov_estoque_produto`
    FOREIGN KEY (`produto_id`)
    REFERENCES `controle_estoque`.`produtos` (`id`),
  CONSTRAINT `fk_mov_estoque_usuario`
    FOREIGN KEY (`usuario_id`)
    REFERENCES `controle_estoque`.`usuarios` (`id`),
  CONSTRAINT `fk_mov_estoque_fornecedor`
    FOREIGN KEY (`fornecedor_id`)
    REFERENCES `controle_estoque`.`fornecedores` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `controle_estoque`.`produtos_categorias`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `controle_estoque`.`produtos_categorias` (
  `produtos_id` INT NOT NULL,
  `categorias_id` INT NOT NULL,
  PRIMARY KEY (`produtos_id`, `categorias_id`),
  CONSTRAINT `fk_produtos_has_categorias_produtos1`
    FOREIGN KEY (`produtos_id`)
    REFERENCES `controle_estoque`.`produtos` (`id`),
  CONSTRAINT `fk_produtos_has_categorias_categorias1`
    FOREIGN KEY (`categorias_id`)
    REFERENCES `controle_estoque`.`categorias` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;