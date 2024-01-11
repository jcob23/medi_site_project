package pl.medisite.infrastructure.database.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import pl.medisite.infrastructure.database.entity.AppointmentEntity;

import java.util.List;
import java.util.Set;

public interface AppointmentRepository extends JpaRepository<AppointmentEntity, Integer> {


    AppointmentEntity getByAppointmentId(Integer appointmentId);

    @Query("SELECT a FROM AppointmentEntity a" +
            " JOIN a.doctor d" +
            " JOIN d.loginDetails u" +
            " WHERE u.email = :email")
    Set<AppointmentEntity> getDoctorAppointments(String email);

    @Query("SELECT a FROM AppointmentEntity a" +
            " JOIN a.doctor d" +
            " JOIN d.loginDetails u" +
            " WHERE u.email = :email")
    Page<AppointmentEntity> getDoctorAppointments(String email, PageRequest pageAble);


    @Query("SELECT a FROM AppointmentEntity a" +
            " JOIN a.doctor d" +
            " JOIN d.loginDetails u" +
            " WHERE u.email = :email AND a.appointmentStart < CURRENT_TIMESTAMP")
    Page<AppointmentEntity> getDoctorPastAppointments(String email, PageRequest pageAble);


    @Query("SELECT a FROM AppointmentEntity a" +
            " JOIN a.doctor d" +
            " JOIN d.loginDetails u" +
            " WHERE u.email = :email AND a.appointmentStart > CURRENT_TIMESTAMP")
    Page<AppointmentEntity> getDoctorFutureAppointments(String email, PageRequest pageAble);

    @Query("SELECT a FROM AppointmentEntity a" +
            " JOIN a.patient p" +
            " JOIN p.loginDetails u" +
            " WHERE u.email = :email")
    Page<AppointmentEntity> getPatientAppointments(String email, PageRequest pageable);

    @Query("SELECT a FROM AppointmentEntity a" +
            " JOIN a.patient p" +
            " JOIN p.loginDetails u" +
            " WHERE u.email = :email")
    List<AppointmentEntity> getPatientAppointments(String email);

    @Query("SELECT a FROM AppointmentEntity a" +
            " JOIN a.patient p" +
            " JOIN p.loginDetails u" +
            " WHERE u.email = :email AND a.appointmentStart < CURRENT_TIMESTAMP")
    Page<AppointmentEntity> getPatientPastAppointments(String email, PageRequest pageable);


    @Query("SELECT a FROM AppointmentEntity a" +
            " JOIN a.patient p" +
            " JOIN p.loginDetails u" +
            " WHERE u.email = :email AND a.appointmentStart > CURRENT_TIMESTAMP")
    Page<AppointmentEntity> getPatientFutureAppointments(String email, PageRequest pageable);

    @Query("SELECT a FROM AppointmentEntity a" +
            " JOIN a.doctor d" +
            " JOIN d.loginDetails u" +
            " WHERE u.email = :email AND a.appointmentStart > CURRENT_TIMESTAMP AND a.patient IS NULL")
    Set<AppointmentEntity> getDoctorFutureFreeAppointments(String email, Sort sort);

    @Query("SELECT a FROM AppointmentEntity a" +
            " JOIN a.patient p" +
            " JOIN a.doctor d" +
            " JOIN p.loginDetails up" +
            " JOIN d.loginDetails ud" +
            " WHERE up.email = :patientEmail AND ud.email = :doctorEmail AND a.appointmentStart > CURRENT_TIMESTAMP")
    Set<AppointmentEntity> getPatientFutureAppointmentsForDoctor(String patientEmail, String doctorEmail, Sort sort);

    @Modifying
    @Transactional
    @Query("DELETE FROM AppointmentEntity a" +
            " WHERE a.id = :appointmentId")
    void deleteAppointment(@Param("appointmentId") Integer appointmentId);


    @Modifying
    @Transactional
    @Query("DELETE FROM AppointmentEntity a" +
            " WHERE a.doctor IN (SELECT d FROM DoctorEntity d JOIN d.loginDetails ld WHERE ld.email = :doctorEmail)")
    void deleteAllByDoctorEmail(@Param("doctorEmail") String doctorEmail);

}
