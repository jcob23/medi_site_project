package pl.medisite.infrastructure.database.mapper;

import pl.medisite.controller.DTO.PersonDTO;
import pl.medisite.infrastructure.database.entity.PatientEntity;

public class PatientEntityMapper {
    public static PersonDTO map(PatientEntity patientEntity){
        return new PersonDTO(
                patientEntity.getLoginDetails().getEmail(),
                patientEntity.getName(),
                patientEntity.getSurname(),
                "PATIENT",
                patientEntity.getPhone()
        );
    }
}
