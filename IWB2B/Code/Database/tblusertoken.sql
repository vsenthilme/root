USE IWE;
GO

DROP TABLE tblusertoken;
GO

CREATE TABLE tblusertoken (
  username varchar(255) NOT NULL,
  password varchar(255) DEFAULT NULL,
  company varchar(255) NULL
) COMMENT='User Token';
GO

INSERT INTO tblusertoken (username,password, company)
VALUES ('test', '$2a$10$g5oPErljOZSJq8M.uq3l1eyhQNzST4/TpvFTsd81s3kZrYH.CXMq2','Test Company');
GO

INSERT INTO tblusertoken (username,password, company)
VALUES ('b2b', '$2a$10$m7IeMAZVuc6fJxFA8iscCu15KXKW/4jDLaafl7RcbhViMY4PGEZiy','B2B');
GO
