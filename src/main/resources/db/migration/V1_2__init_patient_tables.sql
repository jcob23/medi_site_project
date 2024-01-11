CREATE TABLE patient
(
    patient_id          SERIAL             NOT NULL,
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



