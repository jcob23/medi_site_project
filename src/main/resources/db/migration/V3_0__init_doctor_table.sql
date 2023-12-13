CREATE TABLE medisite_doctor
(
    doctor_id           SERIAL              NOT NULL,
    name                VARCHAR(20)         NOT NULL,
    surname             VARCHAR(20)         NOT NULL,
    specialization      VARCHAR(32)         NOT NULL,
    phone               VARCHAR(32)         NOT NULL,
    email               VARCHAR(32)         NOT NULL,
    description         VARCHAR(256),
    PRIMARY KEY (doctor_id),
    CONSTRAINT fk_user
           FOREIGN KEY(email)
               REFERENCES medisite_user(email)
);

INSERT INTO medisite_doctor (name,surname,email,phone,specialization)
VALUES ('Agata','Lekarka', 'doctor1@medisite.pl','+48 534 873 981','bezsenność');

INSERT INTO medisite_doctor (name,surname,email,phone,specialization)
VALUES ('Krystyna','Doktorka','doctor2@medisite.pl','+48 634 873 982','strach');



