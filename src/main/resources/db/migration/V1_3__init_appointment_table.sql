CREATE TABLE appointment
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
               REFERENCES doctor(doctor_id),
    CONSTRAINT fk_user
           FOREIGN KEY(patient_email)
               REFERENCES patient(email)
);



