INSERT INTO medisite_role (role_id,role) values (1,'ADMIN');

INSERT INTO medisite_role (role_id,role) values (2,'USER');

INSERT INTO medisite_role (role_id,role) values (3,'DOCTOR');
--
--
INSERT INTO medisite_user (email,password,role_id)
VALUES ('admin@medisite.pl','$2a$12$XcjIA2Yj5OPKIkr4b1/vRejSfIWMwd.fFETrBR6d2w4kcbppMOApS',1);
--
INSERT INTO medisite_user (email,password,role_id)
VALUES ( 'user1@medisite.pl','$2a$12$XcjIA2Yj5OPKIkr4b1/vRejSfIWMwd.fFETrBR6d2w4kcbppMOApS',2);

INSERT INTO medisite_user (email,password,role_id)
VALUES ( 'user2@medisite.pl','$2a$12$XcjIA2Yj5OPKIkr4b1/vRejSfIWMwd.fFETrBR6d2w4kcbppMOApS',2);

INSERT INTO medisite_user (email,password,role_id)
VALUES ( 'doctor1@medisite.pl','$2a$12$XcjIA2Yj5OPKIkr4b1/vRejSfIWMwd.fFETrBR6d2w4kcbppMOApS',3);

INSERT INTO medisite_user (email,password,role_id)
VALUES ( 'doctor2@medisite.pl','$2a$12$XcjIA2Yj5OPKIkr4b1/vRejSfIWMwd.fFETrBR6d2w4kcbppMOApS',3);

--DROP TABLE medisite_patient;
--DROP TABLE medisite_patient_diseases;
--DROP TABLE medisite_doctor;
--DROP TABLE medisite_user;
--DROP TABLE medisite_role;
--DROP TABLE flyway_schema_history;


