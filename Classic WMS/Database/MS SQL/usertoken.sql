USE WMS;
GO

CREATE TABLE tblusertoken (
  username varchar(255) NOT NULL,
  password varchar(255) DEFAULT NULL
)
GO

INSERT INTO tblusertoken (username,password) VALUES ('test', '$2a$10$jX7TxpjI21RRv3eaP39YuO77tnrcRJyoOQ.DzCC9JVHriZq20Q8em');
GO

INSERT INTO tblusertoken (username,password) VALUES ('wms', '$2a$10$lE0Zz6qn.uslWHDBqsmfn.VIcn7.upjqxht/9fcXcekxrwKDQAdUi');
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
