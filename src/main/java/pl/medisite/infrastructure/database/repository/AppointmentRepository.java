package pl.medisite.infrastructure.database.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import pl.medisite.infrastructure.database.entity.AppointmentEntity;
import pl.medisite.infrastructure.database.entity.DiseaseEntity;
import pl.medisite.infrastructure.database.entity.PatientEntity;

import java.util.Set;

public interface AppointmentRepository extends JpaRepository<AppointmentEntity, Long> {


    AppointmentEntity getById(Integer id);

    @Query("SELECT p FROM PatientEntity p" +
            " JOIN p.appointments a" +
            " WHERE a.id = :appointmentId")
    PatientEntity getPatientByAppointmentId(Integer appointmentId);
    @Query("SELECT a FROM AppointmentEntity a" +
            " JOIN a.doctor d" +
            " JOIN d.loginDetails u" +
            " WHERE u.email = :email")
    Set<AppointmentEntity> getDoctorAppointments(String email, Sort sort);

    @Query("SELECT a FROM AppointmentEntity a" +
            " JOIN a.doctor d" +
            " JOIN d.loginDetails u" +
            " WHERE u.email = :email AND a.appointmentStart < CURRENT_TIMESTAMP")
    Set<AppointmentEntity> getDoctorPastAppointments(String email, Sort sort);


    @Query("SELECT a FROM AppointmentEntity a" +
            " JOIN a.doctor d" +
            " JOIN d.loginDetails u" +
            " WHERE u.email = :email AND a.appointmentStart > CURRENT_TIMESTAMP")
    Set<AppointmentEntity> getDoctorFutureAppointments(String email, Sort sort);

    @Query("SELECT a FROM AppointmentEntity a" +
            " JOIN a.doctor d" +
            " JOIN d.loginDetails u" +
            " WHERE u.email = :email AND a.appointmentStart > CURRENT_TIMESTAMP AND a.patient IS NULL")
    Set<AppointmentEntity> getDoctorFutureFreeAppointments(String email, Sort sort);

    @Query("SELECT a FROM AppointmentEntity a" +
            " JOIN a.patient p" +
            " JOIN p.loginDetails u" +
            " WHERE u.email = :email")
    Set<AppointmentEntity> getPatientAppointments(String email, Sort sort);

    @Query("SELECT a FROM AppointmentEntity a" +
            " JOIN a.patient p" +
            " JOIN p.loginDetails u" +
            " WHERE u.email = :email AND a.appointmentStart < CURRENT_TIMESTAMP")
    Set<AppointmentEntity> getPatientPastAppointments(String email, Sort sort);


    @Query("SELECT a FROM AppointmentEntity a" +
            " JOIN a.patient p" +
            " JOIN p.loginDetails u" +
            " WHERE u.email = :email AND a.appointmentStart > CURRENT_TIMESTAMP")
    Set<AppointmentEntity> getPatientFutureAppointments(String email, Sort sort);

    @Modifying
    @Transactional
    @Query("DELETE FROM AppointmentEntity a" +
            " WHERE a.id = :appointmentId")
    void deleteAppointment(@Param("appointmentId") Integer appointmentId);


    @Query("SELECT d FROM DiseaseEntity d" +
            " JOIN d.patient p" +
            " JOIN p.appointments a" +
            " WHERE a.id = :id")
    Set<DiseaseEntity> getDiseases(@Param("id") Integer appointmentId);


}
