/*Criação da DB*/
CREATE DATABASE kumoridb;
USE kumoridb;

/*Criação das tabelas*/
CREATE TABLE Movimentos(
    id INT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    INDEX (nome)
);

CREATE TABLE Autores(
    id INT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL,
    data_nascimento DATE,
    data_falecimento DATE,
    movimentos_id INT NOT NULL,
    biografia VARCHAR(1500) NOT NULL,
    foto MEDIUMBLOB,
    PRIMARY KEY (id),
    CONSTRAINT fk_moviemntos_autores FOREIGN KEY (movimentos_id) REFERENCES Movimentos (id) ON DELETE CASCADE,
    INDEX (nome)
);

CREATE TABLE Categorias(
    id INT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    INDEX (nome)
);

CREATE TABLE Editoras(
    id INT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    INDEX (nome)
);

CREATE TABLE Livros(
    id INT NOT NULL AUTO_INCREMENT,
    titulo VARCHAR(255) NOT NULL,
    ano INT NOT NULL, 
    descricao VARCHAR(1500),
    capa MEDIUMBLOB,
    autores_id INT NOT NULL,
    editoras_id INT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_autores_livros FOREIGN KEY (autores_id) REFERENCES Autores (id) ON DELETE CASCADE,
    CONSTRAINT fk_editoras_livros FOREIGN KEY (editoras_id) REFERENCES Editoras (id) ON DELETE CASCADE,
    INDEX (titulo),
    INDEX (ano)
);

CREATE TABLE Categorias_Livros(
    categorias_id INT NOT NULL,
    livros_id INT NOT NULL,
    PRIMARY KEY (categorias_id, livros_id),
    CONSTRAINT fk_categorias_cl FOREIGN KEY (categorias_id) REFERENCES Categorias (id) ON DELETE CASCADE,
    CONSTRAINT fk_livros_cl FOREIGN KEY (livros_id) REFERENCES Livros (id) ON DELETE CASCADE
);

CREATE TABLE Exemplares(
    id INT NOT NULL AUTO_INCREMENT,
    livros_id INT NOT NULL,
    localizacao VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_livros_exemplares FOREIGN KEY (livros_id) REFERENCES Livros (id) ON DELETE CASCADE
);

CREATE TABLE Logins(
    id INT NOT NULL AUTO_INCREMENT,
    login VARCHAR(100) NOT NULL,
    senha CHAR(32) NOT NULL,
    acesso CHAR(1) NOT NULL,
    PRIMARY KEY (id),
    INDEX (login)
);

CREATE USER 'kumori'@'localhost' IDENTIFIED BY 'a7X@pL#9zWq1!Km$Tf8&nB2^RsE0*VhY';
GRANT ALL ON kumoridb.* TO 'kumori'@'localhost';
FLUSH PRIVILEGES;

INSERT INTO Logins(login, senha, acesso) VALUES ('Kumori', MD5('a7X@pL#9zWq1!Km$Tf8&nB2^RsE0*VhY'), 'a');