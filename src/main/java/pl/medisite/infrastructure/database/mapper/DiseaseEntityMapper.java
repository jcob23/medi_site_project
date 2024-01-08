package pl.medisite.infrastructure.database.mapper;

import pl.medisite.controller.DTO.DiseaseDTO;
import pl.medisite.infrastructure.database.entity.DiseaseEntity;
import pl.medisite.infrastructure.database.entity.PatientEntity;

public class DiseaseEntityMapper {

    public static DiseaseEntity mapNewDisease(DiseaseDTO diseaseDTO, PatientEntity patient){
        return DiseaseEntity.builder()
                .patient(patient)
                .cured(false)
                .name(diseaseDTO.getName())
                .description(diseaseDTO.getDescription())
                .since(diseaseDTO.getSince())
                .build();
    }
}
