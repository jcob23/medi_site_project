INSERT INTO medisite_role (role_id,role) values (1,'ADMIN');

INSERT INTO medisite_role (role_id,role) values (2,'USER');
--
--
INSERT INTO medisite_user (email,password,role_id)
VALUES ('admin@medisite.pl','$2a$12$XcjIA2Yj5OPKIkr4b1/vRejSfIWMwd.fFETrBR6d2w4kcbppMOApS',1);
--
--INSERT INTO medisite_user (user_id,user_name,email,password,role_id)
--VALUES (2,'user1', 'user1@medisite.pl','$2a$12$XcjIA2Yj5OPKIkr4b1/vRejSfIWMwd.fFETrBR6d2w4kcbppMOApS',2);
--
--INSERT INTO medisite_user (user_id,user_name,email,password,active,role_id)
--VALUES (3,'user2', 'user2@medisite.pl','$2a$12$XcjIA2Yj5OPKIkr4b1/vRejSfIWMwd.fFETrBR6d2w4kcbppMOApS',2);


