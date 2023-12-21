package pl.medisite.infrastructure.database.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import pl.medisite.infrastructure.database.entity.AppointmentEntity;

import java.util.Set;

public interface AppointmentRepository extends JpaRepository<AppointmentEntity,Long> {


    AppointmentEntity getById(Integer id);

    @Query("SELECT a FROM AppointmentEntity a" +
            " JOIN a.patient p" +
            " JOIN p.loginDetails u" +
            " WHERE u.email = :email")
    Set<AppointmentEntity> getAppointments(String email, Sort sort);

    @Query("SELECT a FROM AppointmentEntity a" +
            " JOIN a.patient p" +
            " JOIN p.loginDetails u" +
            " WHERE u.email = :email AND a.appointmentStart < CURRENT_TIMESTAMP")
    Set<AppointmentEntity> getPastAppointments(String email, Sort sort);


    @Query("SELECT a FROM AppointmentEntity a" +
            " JOIN a.patient p" +
            " JOIN p.loginDetails u" +
            " WHERE u.email = :email AND a.appointmentStart > CURRENT_TIMESTAMP")
    Set<AppointmentEntity> getFutureAppointments(String email, Sort sort);

    @Modifying
    @Transactional
    @Query("DELETE FROM AppointmentEntity a" +
            " WHERE a.patient.loginDetails.email = :email AND a.id = :appointmentId")
    void deleteAppointment(@Param("email") String email, @Param("appointmentId") Integer appointmentId);
}
