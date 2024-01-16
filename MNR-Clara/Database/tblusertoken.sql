DROP TABLE `MNRCLARA`.`tblusertoken`;

CREATE TABLE `MNRCLARA`.`tblusertoken` (
  `username` varchar(255) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `company` varchar(255) NULL
) COMMENT='User Token';

INSERT INTO `MNRCLARA`.`tblusertoken` (`username`,`password`, `company`)
VALUES ('test', '$2a$10$jX7TxpjI21RRv3eaP39YuO77tnrcRJyoOQ.DzCC9JVHriZq20Q8em','Test Company');

INSERT INTO `MNRCLARA`.`tblusertoken` (`username`,`password`, `company`)
VALUES ('mnrclara', '$2a$10$DkMStNQlwL3TlCllyxFsPejheDJ8/ZIg6m1SmmQJNmNi6O/d0dmku','MNRCLARA');

