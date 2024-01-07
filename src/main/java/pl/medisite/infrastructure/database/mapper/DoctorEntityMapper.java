package pl.medisite.infrastructure.database.mapper;

import pl.medisite.controller.DTO.PersonDTO;
import pl.medisite.infrastructure.database.entity.DoctorEntity;

public class DoctorEntityMapper {

    public static PersonDTO.DoctorDTO map(DoctorEntity doctor){
        return new PersonDTO.DoctorDTO(
                doctor.getLoginDetails().getEmail(),
                doctor.getName(),
                doctor.getSurname(),
                "DOCTOR",
                doctor.getPhone(),
                doctor.getSpecialization(),
                doctor.getDescription()
        );
    }
}
