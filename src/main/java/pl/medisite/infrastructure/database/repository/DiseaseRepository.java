package pl.medisite.infrastructure.database.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import pl.medisite.infrastructure.database.entity.DiseaseEntity;

import java.util.Set;

public interface DiseaseRepository extends JpaRepository<DiseaseEntity, Integer> {
    @Query("SELECT d FROM DiseaseEntity d" +
            " JOIN d.patient p" +
            " JOIN p.loginDetails u" +
            " WHERE u.email = :email")
    Set<DiseaseEntity> getDiseases(@Param("email") String email, Sort sort);


    DiseaseEntity getByDiseaseId(Integer diseaseId);

    @Modifying
    @Transactional
    @Query("DELETE FROM DiseaseEntity d" +
            " WHERE d.patient IN (SELECT p FROM PatientEntity p JOIN p.loginDetails ld WHERE ld.email = :email)")
    void deleteAllByEmail(String email);
}
