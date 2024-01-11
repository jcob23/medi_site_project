package pl.medisite.infrastructure.database;

import lombok.AllArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import pl.medisite.infrastructure.database.entity.AppointmentEntity;
import pl.medisite.infrastructure.database.entity.DoctorEntity;
import pl.medisite.infrastructure.database.entity.PatientEntity;
import pl.medisite.infrastructure.database.repository.AppointmentRepository;
import pl.medisite.infrastructure.database.repository.DoctorRepository;
import pl.medisite.infrastructure.database.repository.PatientRepository;
import pl.medisite.util.DoctorFixtures;
import pl.medisite.util.PatientFixtures;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DoctorRepositoryJpaTest extends AbstractJpaIT {

    private DoctorRepository doctorRepository;
    private AppointmentRepository appointmentRepository;
    private PatientRepository patientRepository;

    @Test
    void shouldDeleteDoctorByEmail() {
        // given
        DoctorEntity doctor = DoctorFixtures.doctorEntity1();
        doctorRepository.save(doctor);

        // when
        doctorRepository.deleteByMail("test4@email.com");

        // then
        assertThat(doctorRepository.findByEmail("test4@email.com")).isNull();
    }

    @Test
    void shouldFindByEmail() {
        // given
        DoctorEntity doctor = DoctorFixtures.doctorEntity1();
        doctorRepository.save(doctor);

        // when
        DoctorEntity foundDoctor = doctorRepository.findByEmail("test4@email.com");

        // then
        assertThat(foundDoctor).isNotNull();
        assertThat(foundDoctor.getLoginDetails().getEmail()).isEqualTo("test4@email.com");
    }

    @Test
    void shouldGetPatientsForDoctor() {
        // given
        DoctorEntity doctor = DoctorFixtures.doctorEntity1();
        doctorRepository.save(doctor);
        PatientEntity patient = patientRepository.findByEmail("user1@medisite.pl");
        AppointmentEntity appointmentEntity = AppointmentEntity.builder()
                .doctor(doctor)
                .appointmentStart(ZonedDateTime.of(2025,1,20,8,0,0,0, ZoneId.of("Europe/Warsaw")))
                .appointmentEnd(ZonedDateTime.of(2025,1,20,9,0,0,0, ZoneId.of("Europe/Warsaw")))
                .patient(patient)
                .note("")
                .build();
        appointmentRepository.save(appointmentEntity);
        Set<PatientEntity> patients = doctorRepository.getPatientsForDoctor("test4@email.com");

        // then
        Assertions.assertThat(patients).isEqualTo(Set.of(patient));
    }


}

