-- MySQL Script generated by MySQL Workbench
-- Tue Dec  3 14:09:03 2024
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema shortURL
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema shortURL
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `shortURL` DEFAULT CHARACTER SET utf8 ;
USE `shortURL` ;

-- -----------------------------------------------------
-- Table `shortURL`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `shortURL`.`users` (
  `UUID` VARCHAR(45) NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE,
  UNIQUE INDEX `UUID_UNIQUE` (`UUID` ASC) VISIBLE,
  PRIMARY KEY (`UUID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `shortURL`.`links`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `shortURL`.`links` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `UUID_user` VARCHAR(45) NOT NULL,
  `long_url` VARCHAR(2000) NOT NULL,
  `short_url` VARCHAR(45) NOT NULL,
  `date_start` DATE NOT NULL,
  `date_end` DATE NOT NULL,
  `transition_limit` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  INDEX `UUID_User_idx` (`UUID_user` ASC) VISIBLE,
  CONSTRAINT `UUID_User`
    FOREIGN KEY (`UUID_user`)
    REFERENCES `shortURL`.`users` (`UUID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
