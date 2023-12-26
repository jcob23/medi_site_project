CREATE TABLE medisite_appointment
(
    appointment_id       SERIAL                          NOT NULL,
    appointment_start    TIMESTAMP WITH TIME ZONE        NOT NULL,
    appointment_end      TIMESTAMP WITH TIME ZONE        NOT NULL,
    patient_email        VARCHAR(32),
    note                 TEXT,
    doctor_id            int                             NOT NULL,
    PRIMARY KEY (appointment_id),
    CONSTRAINT fk_doctor
           FOREIGN KEY(doctor_id)
               REFERENCES medisite_doctor(doctor_id),
    CONSTRAINT fk_user
           FOREIGN KEY(patient_email)
               REFERENCES medisite_patient(email)
);


INSERT INTO medisite_appointment (appointment_start, appointment_end, patient_email, doctor_id, note)
VALUES ('2023-12-25 10:00:00', '2024-01-15 11:00:00', 'user1@medisite.pl', 1, 'Dodatkowe informacje o wizycie 1');

INSERT INTO medisite_appointment (appointment_start, appointment_end, patient_email, doctor_id, note)
VALUES ('2023-12-22 10:00:00', '2023-12-27 11:00:00', 'user1@medisite.pl', 1, 'Dodatkowe informacje o wizycie 2');

INSERT INTO medisite_appointment (appointment_start, appointment_end, patient_email, doctor_id, note)
VALUES ('2023-12-10 10:00:00', '2023-12-10 11:00:00', 'user1@medisite.pl', 1, 'Dodatkowe informacje o wizycie 3');
