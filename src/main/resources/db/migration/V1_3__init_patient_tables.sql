CREATE TABLE patient
(
    patient_id           SERIAL             NOT NULL,
    name                VARCHAR(20)         NOT NULL,
    surname             VARCHAR(20)         NOT NULL,
    phone               VARCHAR(32)         NOT NULL,
    email               VARCHAR(32)         NOT NULL,
    disease_id          INT,
    PRIMARY KEY (patient_id),
    UNIQUE(email),
    CONSTRAINT fk_user
        FOREIGN KEY(email)
              REFERENCES medisite_user(email)
);

CREATE TABLE patient_diseases
(
    disease_id          SERIAL              NOT NULL,
    name                VARCHAR(20)         NOT NULL,
    description         VARCHAR(20)         NOT NULL,
    cured               BOOLEAN             NOT NULL,
    since               DATE                NOT NULL,
    patient_id          INT                 NOT NULL,
    PRIMARY KEY (disease_id),
    CONSTRAINT fk_patient
        FOREIGN KEY(patient_id)
            REFERENCES patient(patient_id)
);
INSERT INTO patient (patient_id,name,surname,email,phone)
VALUES (1,'Romek','Księżycowy', 'user1@medisite.pl','+48 234 873 981');

INSERT INTO patient (patient_id,name,surname,email,phone)
VALUES (2,'Tomek','Gwiazdowy', 'user2@medisite.pl','+48 234 873 982');

INSERT INTO patient_diseases (disease_id,name,description,cured,since,patient_id)
VALUES (1,'choroba A', 'opis choroby A',false,'2020.01.01',1);

INSERT INTO patient_diseases (disease_id,name,description,cured,since,patient_id)
VALUES (2,'choroba B', 'opis choroby B',false,'2019.01.01',1);

INSERT INTO patient_diseases (disease_id,name,description,cured,since,patient_id)
VALUES (3,'choroba C', 'opis choroby C',true,'2021.01.01',1);


