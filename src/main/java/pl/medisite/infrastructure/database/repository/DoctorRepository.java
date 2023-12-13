package pl.medisite.infrastructure.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.medisite.infrastructure.database.entity.DoctorEntity;


public interface DoctorRepository extends JpaRepository<DoctorEntity,Long> {
}
