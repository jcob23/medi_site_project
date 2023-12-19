package pl.medisite.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "medisite_appointment")
public class AppointmentEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id")
    private int id;

    @Column(name = "appointment_start")
    private ZonedDateTime appointmentStart;

    @Column(name = "appointment_end")
    private ZonedDateTime appointmentEnd;

    @Column(name = "note")
    private String note;

    @ManyToOne
    @JoinColumn(name = "patient_email", referencedColumnName = "email")
    private PatientEntity patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private DoctorEntity doctor;
}

