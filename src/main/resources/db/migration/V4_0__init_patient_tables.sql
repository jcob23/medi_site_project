CREATE Table medisite_patient_diseases
(
    disease_id          SERIAL              NOT NULL,
    name                VARCHAR(20)         NOT NULL,
    description         VARCHAR(20)         NOT NULL,
    cured               BOOLEAN             NOT NULL,
    since               DATE                NOT NULL,
    PRIMARY KEY (disease_id)
);

CREATE TABLE medisite_patient
(
    patient_id           SERIAL             NOT NULL,
    name                VARCHAR(20)         NOT NULL,
    surname             VARCHAR(20)         NOT NULL,
    phone               VARCHAR(32)         NOT NULL,
    email               VARCHAR(32)         NOT NULL,
    disease_id          int,
    PRIMARY KEY (patient_id),
    CONSTRAINT fk_diseases
           FOREIGN KEY(disease_id)
               REFERENCES medisite_patient_diseases(disease_id),
    CONSTRAINT fk_user
        FOREIGN KEY(email)
              REFERENCES medisite_user(email)
);


--
INSERT INTO medisite_patient (name,surname,email,phone)
VALUES ('Romek','Księżycowy', 'user1@medisite.pl','+48 234 873 981');

INSERT INTO medisite_patient (name,surname,email,phone)
VALUES ('Tomek','Giwazdowy', 'user2@medisite.pl','+48 234 873 982');

