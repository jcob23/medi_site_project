package pl.medisite.infrastructure.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import pl.medisite.infrastructure.database.entity.DoctorEntity;
import pl.medisite.infrastructure.database.entity.PatientEntity;

import java.util.Set;


public interface DoctorRepository extends JpaRepository<DoctorEntity, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM DoctorEntity d" +
            " WHERE d.loginDetails.email = :email")
    void deleteByMail(String email);


    @Query("SELECT d FROM DoctorEntity d" +
            " JOIN FETCH d.loginDetails u" +
            " WHERE u.email = :email")
    DoctorEntity findByEmail(@Param("email") String email);

    @Query("SELECT d FROM DoctorEntity d " +
            "JOIN FETCH d.loginDetails u  "
    )
    Set<DoctorEntity> getAllDoctors();

    @Query("SELECT DISTINCT a.patient " +
            "FROM AppointmentEntity a " +
            "JOIN DoctorEntity d ON a.doctor.id = d.id " +
            "WHERE d.loginDetails.email = :doctorEmail")
    Set<PatientEntity> getPatientsForDoctor(@Param("doctorEmail") String doctorEmail);




}
