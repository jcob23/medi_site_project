package pl.medisite.infrastructure.database.repository;

import jakarta.persistence.NamedNativeQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.medisite.controller.buisness.PersonDTO;

import java.util.List;

public interface PersonRepository extends JpaRepository<PersonDTO, String> {

    @Query(
            nativeQuery = true,
            value = "SELECT u.email,u.password, d.name, d.surname, d.phone, r.role" +
                    " FROM medisite_user u" +
                    " JOIN medisite_doctor d ON u.email = d.email" +
                    " JOIN medisite_role r ON u.role_id = r.role_id;"

    )
    List<PersonDTO> findPersonsInformation();

    @Query(
            nativeQuery = true,
            value = "SELECT u.email,u.password, d.name, d.surname, d.phone, r.role" +
                    " FROM medisite_user u" +
                    " JOIN medisite_patient d ON u.email = d.email" +
                    " JOIN medisite_role r ON u.role_id = r.role_id;"

    )
    List<PersonDTO> findPersonsInformation2();
}
