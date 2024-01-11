package pl.medisite.infrastructure.database;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import pl.medisite.infrastructure.database.entity.AppointmentEntity;
import pl.medisite.infrastructure.database.entity.DiseaseEntity;
import pl.medisite.infrastructure.database.entity.DoctorEntity;
import pl.medisite.infrastructure.database.repository.AppointmentRepository;
import pl.medisite.infrastructure.database.repository.DoctorRepository;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.Assert.assertTrue;

@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AppointmentRepositoryJpaTest extends AbstractJpaIT  {

    AppointmentRepository appointmentRepository;
    DoctorRepository doctorRepository;
    @Test
    void shouldGetDoctorAppointments() {
        // given
        String doctorEmail = "doctor1@medisite.pl";

        // when
        Set<AppointmentEntity> doctorAppointments = appointmentRepository.getDoctorAppointments(doctorEmail);

        // then
        assertThat(doctorAppointments).isNotEmpty();
    }

    @Test
    void shouldGetDoctorAppointmentsWithPagination() {
        // given, doctor z dodanymi Appointments w migracjach flyway
        String doctorEmail = "doctor1@medisite.pl";


        // when
        Page<AppointmentEntity> doctorAppointments =
                appointmentRepository.getDoctorAppointments(doctorEmail, PageRequest.of(0, 10));

        // then
        assertThat(doctorAppointments.getContent()).isNotEmpty();
    }

    // Similarly, write tests for other methods in the AppointmentRepository
    @Test
    void shouldGetPatientAppointmentsWithPagination() {
        // given, doctor z dodanymi Appointments w migracjach flyway
        String patientEmail = "user1@medisite.pl";


        // when
        Page<AppointmentEntity> doctorAppointments =
                appointmentRepository.getPatientAppointments(patientEmail, PageRequest.of(0, 10));

        // then
        assertThat(doctorAppointments.getContent()).isNotEmpty();
    }
    // ...

    @Test
    void shouldDeleteAppointment() {
        // given
        AppointmentEntity appointment = createAppointmentEntity();
        appointmentRepository.saveAndFlush(appointment);

        // when
        appointmentRepository.deleteAppointment(appointment.getAppointmentId());

        List<AppointmentEntity> all = appointmentRepository.findAll();
        // then
        assertThat(all).doesNotContain(appointment);
    }

    private AppointmentEntity createAppointmentEntity() {
        DoctorEntity doctor = doctorRepository.findByEmail("doctor1@medisite.pl");
        return AppointmentEntity.builder()
                .doctor(doctor)
                .appointmentStart(ZonedDateTime.of(2025,1,20,8,0,0,0, ZoneId.of("Europe/Warsaw")))
                .appointmentEnd(ZonedDateTime.of(2025,1,20,9,0,0,0, ZoneId.of("Europe/Warsaw")))
                .note("")
                .build();
    }
}

