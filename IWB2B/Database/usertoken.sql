USE WMS;

CREATE TABLE tblusertoken ( username varchar(255) NOT NULL, password varchar(255) DEFAULT NULL );

 COMMENT='User Credentials';
 
INSERT INTO tblusertoken (username,password) VALUES ('test', '$2a$10$jX7TxpjI21RRv3eaP39YuO77tnrcRJyoOQ.DzCC9JVHriZq20Q8em');

INSERT INTO tblusertoken (username,password) VALUES ('IWMVP', '$2a$10$6npQYHbiZYPYwvaLsNULPuaDboLNdpbdYw1P4PLWYf0hGM/zQcrIG');

INSERT INTO tblusertoken (username,password) VALUES ('IWMVP', '$2a$10$V7cZjgAiVpRs46D43AvUbuwXgGi5/oa4ts7kEx4cbCv3qa2PHVa1W');

