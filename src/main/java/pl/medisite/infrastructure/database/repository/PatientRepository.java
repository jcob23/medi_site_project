package pl.medisite.infrastructure.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import pl.medisite.infrastructure.database.entity.PatientEntity;

public interface PatientRepository extends JpaRepository<PatientEntity, Integer> {

    @Query("SELECT p FROM PatientEntity p" +
            " JOIN FETCH p.loginDetails u" +
            " WHERE u.email = :email")
    PatientEntity findByEmail(@Param("email") String email);

    @Modifying
    @Transactional
    @Query("DELETE FROM PatientEntity p WHERE p.loginDetails.email = :email")
    void deleteByMail(String email);


}
