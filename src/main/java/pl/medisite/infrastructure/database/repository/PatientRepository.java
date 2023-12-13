package pl.medisite.infrastructure.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.medisite.infrastructure.database.entity.PatientEntity;

public interface PatientRepository extends JpaRepository<PatientEntity,Long> {

    @Query("SELECT p FROM PatientEntity p WHERE p.loginDetails.email = :email")
    PatientEntity findByEmail(@Param("email") String email);
}
