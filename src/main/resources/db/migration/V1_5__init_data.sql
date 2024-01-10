INSERT INTO role (role_id,role) values (1,'ADMIN');

INSERT INTO role (role_id,role) values (2,'PATIENT');

INSERT INTO role (role_id,role) values (3,'DOCTOR');

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


INSERT INTO doctor (name,surname,email,phone,specialization,description)
VALUES ('Agata','Lekarka', 'doctor1@medisite.pl','+48 534 873 981','bezsenność','');

INSERT INTO doctor (name,surname,email,phone,specialization,description)
VALUES ('Krystyna','Doktorka','doctor2@medisite.pl','+48 634 873 982','strach','');

INSERT INTO patient (name,surname,email,phone)
VALUES ('Romek','Księżycowy', 'user1@medisite.pl','+48 234 873 981');

INSERT INTO patient (name,surname,email,phone)
VALUES ('Tomek','Gwiazdowy', 'user2@medisite.pl','+48 234 873 982');

INSERT INTO patient_diseases (name,description,cured,since,patient_id)
VALUES ('choroba A', 'opis choroby A',false,'2020.01.01',1);

INSERT INTO patient_diseases (name,description,cured,since,patient_id)
VALUES ('choroba B', 'opis choroby B',false,'2019.01.01',1);

INSERT INTO patient_diseases (name,description,cured,since,patient_id)
VALUES ('choroba C', 'opis choroby C',true,'2021.01.01',1);


INSERT INTO appointment (appointment_start, appointment_end, patient_email, doctor_id, note)
VALUES ('2023-12-25 10:00:00', '2023-12-25 11:00:00', 'user1@medisite.pl', 1, 'Dodatkowe informacje o wizycie 1');

INSERT INTO appointment (appointment_start, appointment_end, patient_email, doctor_id, note)
VALUES ('2023-12-22 10:00:00', '2023-12-27 11:00:00', 'user1@medisite.pl', 1, 'Dodatkowe informacje o wizycie 2');

INSERT INTO appointment (appointment_start, appointment_end, patient_email, doctor_id, note)
VALUES ('2023-12-10 10:00:00', '2023-12-10 11:00:00', 'user1@medisite.pl', 1, 'Dodatkowe informacje o wizycie 3');

--DROP TABLE appointment;
--DROP TABLE patient_diseases;
--DROP TABLE patient;
--DROP TABLE doctor;
--DROP TABLE medisite_user;
--DROP TABLE user_token;
--DROP TABLE role;
--DROP TABLE flyway_schema_history;

