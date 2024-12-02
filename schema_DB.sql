-- MySQL Script generated by MySQL Workbench
-- Mon Dec  2 15:19:58 2024
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
DROP TABLE IF EXISTS `shortURL`.`users` ;

CREATE TABLE IF NOT EXISTS `shortURL`.`users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `UUID` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE,
  UNIQUE INDEX `UUID_UNIQUE` (`UUID` ASC) VISIBLE)
ENGINE = InnoDB;


"CREATE TABLE users (\n" +
            "    id        INTEGER PRIMARY KEY AUTOINCREMENT\n" +
            "                          NOT NULL,\n" +
            "    name      TEXT        NOT NULL\n" +
            "                          UNIQUE,\n" +
            "    UUID      TEXT        NOT NULL\n" +
            "                          UNIQUE\n" +
            ");"

-- -----------------------------------------------------
-- Table `shortURL`.`links`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `shortURL`.`links` ;

CREATE TABLE IF NOT EXISTS `shortURL`.`links` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `id_user` INT NOT NULL,
  `long` VARCHAR(2000) NOT NULL,
  `short` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  INDEX `id_user_idx` (`id_user` ASC) VISIBLE,
  CONSTRAINT `id_user`
    FOREIGN KEY (`id_user`)
    REFERENCES `shortURL`.`users` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

"CREATE TABLE links (\n" +
            "    id        INTEGER PRIMARY KEY AUTOINCREMENT\n" +
            "                          NOT NULL,\n" +
            "    id_user   INTEGER     NOT NULL,\n" +
            "    long      TEXT        NOT NULL,\n" +
            "    short     TEXT        NOT NULL\n" +
            "                          UNIQUE,\n" +
            "    FOREIGN KEY (id_user)\n" +
            "    REFERENCES users (id) ON DELETE CASCADE\n" +
            ");"

-- -----------------------------------------------------
-- Table `shortURL`.`properties_link`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `shortURL`.`properties_link` ;

CREATE TABLE IF NOT EXISTS `shortURL`.`properties_link` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `id_link` INT NOT NULL,
  `date_start` DATE NOT NULL,
  `date_stop` DATE NOT NULL,
  `limit` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  INDEX `id_link_idx` (`id_link` ASC) VISIBLE,
  UNIQUE INDEX `id_link_UNIQUE` (`id_link` ASC) VISIBLE,
  CONSTRAINT `id_link`
    FOREIGN KEY (`id_link`)
    REFERENCES `shortURL`.`links` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

"CREATE TABLE properties_link (\n" +
            "    id          INTEGER PRIMARY KEY AUTOINCREMENT\n" +
            "                          NOT NULL,\n" +
            "    id_link     INTEGER     NOT NULL\n" +
            "                          UNIQUE,\n" +
            "    date_start  DATE        NOT NULL,\n" +
            "    date_stop   DATE        NOT NULL,\n" +
            "    limit       INTEGER     NOT NULL,\n" +
            "    FOREIGN KEY (id_link)\n" +
            "    REFERENCES links (id) ON DELETE CASCADE\n" +
            ");"

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
