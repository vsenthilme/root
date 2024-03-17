USE WMS;
GO

CREATE TABLE tblusertoken (
  username varchar(255) NOT NULL,
  password varchar(255) DEFAULT NULL
)
GO

INSERT INTO tblusertoken (username,password) VALUES ('test', '$2a$10$jX7TxpjI21RRv3eaP39YuO77tnrcRJyoOQ.DzCC9JVHriZq20Q8em');
GO

INSERT INTO tblusertoken (username,password) VALUES ('wms', '$2a$10$iY.gN7AXVvbe9JH8XJHafu.5dg/UmcgLUfjtQI2ce.tHb3sNWKjwK');
GO

USE WMS;
GO

INSERT INTO tblusertoken (username,password)
VALUES ('muru', '$2a$10$Jz4wCdrQhfTNDcoNFazJ7u0OeTnn//lSVFh00Cszwqy/FBrWwescC');
GO

CREATE TABLE tblusertoken (username varchar(255) NOT NULL,password varchar(255) DEFAULT NULL)
GO

-----------------------------------------------------------------------------------------------------------------------------------

CREATE TABLE tblusertoken ( username varchar(255) NOT NULL, password varchar(255) DEFAULT NULL)
GO

INSERT INTO tblusertoken (username,password) VALUES ('almailem', '$2a$10$8JI5e2B654Phl7LFgXc8aO1HhpCL1ey1VOoART/.rXms9B2Y9yRau');
