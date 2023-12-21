package pl.medisite.infrastructure.database.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.medisite.infrastructure.database.entity.DiseaseEntity;

import java.util.Set;

public interface DiseaseRepository extends JpaRepository<DiseaseEntity,Long> {
    @Query("SELECT d FROM DiseaseEntity d" +
            " JOIN d.patient p" +
            " JOIN p.loginDetails u" +
            " WHERE u.email = :email")
    Set<DiseaseEntity> getDiseases(@Param("email") String email, Sort sort);
}
