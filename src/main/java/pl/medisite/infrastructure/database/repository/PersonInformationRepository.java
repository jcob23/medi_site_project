package pl.medisite.infrastructure.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.medisite.controller.DTO.PersonDTO;

import java.util.List;

public interface PersonInformationRepository extends JpaRepository<PersonDTO, String> {

    @Query(
            nativeQuery = true,
            value = "SELECT u.email, d.name, d.surname, d.phone, d.specialization, d.description, r.role " +
                    "FROM medisite_user u " +
                    "JOIN medisite_doctor d ON u.email = d.email " +
                    "JOIN medisite_role r ON u.role_id = r.role_id;"
    )
    List<PersonDTO.DoctorDTO> findDoctorInformation();

    @Query(
            nativeQuery = true,
            value = "SELECT u.email, p.name, p.surname, p.phone,'' as description,'' as specialization, r.role,'PersonInformation' as dtype" +
                    " FROM medisite_user u" +
                    " JOIN medisite_patient p ON u.email = p.email" +
                    " JOIN medisite_role r ON u.role_id = r.role_id;"
    )
    List<PersonDTO> findPatientInformation();
}
