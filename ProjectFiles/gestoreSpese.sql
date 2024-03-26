DROP SCHEMA IF EXISTS gestorespese;
CREATE SCHEMA gestorespese;
USE gestorespese;

DROP TABLE IF EXISTS spese;
CREATE TABLE spese (
    id_spesa INT UNSIGNED AUTO_INCREMENT NOT NULL,
    nickname VARCHAR(50) NOT NULL,
    data DATE NOT NULL,
    categoria VARCHAR(50) NOT NULL,
    spesa DOUBLE UNSIGNED,

    PRIMARY KEY(id_spesa)
) ENGINE = InnoDB DEFAULT CHARSET = latin1;
