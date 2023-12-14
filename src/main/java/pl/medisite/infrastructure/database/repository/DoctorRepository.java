package pl.medisite.infrastructure.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import pl.medisite.infrastructure.database.entity.DoctorEntity;
import pl.medisite.infrastructure.database.entity.PatientEntity;


public interface DoctorRepository extends JpaRepository<DoctorEntity,Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM DoctorEntity d WHERE d.loginDetails.email = :email")
    void deleteByMail (String email);


    @Query("SELECT p FROM DoctorEntity p JOIN FETCH p.loginDetails u WHERE u.email = :email")
    DoctorEntity findByEmail(@Param("email") String email);
}
