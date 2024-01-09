package pl.medisite.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.medisite.controller.DTO.DiseaseDTO;
import pl.medisite.infrastructure.database.entity.DiseaseEntity;
import pl.medisite.infrastructure.database.entity.PatientEntity;
import pl.medisite.infrastructure.database.mapper.DiseaseEntityMapper;
import pl.medisite.infrastructure.database.repository.DiseaseRepository;

import java.util.Objects;
import java.util.Set;

@Service
@AllArgsConstructor
public class DiseaseService {

    private DiseaseRepository diseaseRepository;
    private PatientService patientService;

    public DiseaseEntity checkIfDiseaseExist(Integer diseaseId) {
        DiseaseEntity disease = diseaseRepository.getByDiseaseId(diseaseId);
        if( Objects.isNull(disease) )
            throw new EntityNotFoundException("Nie ma choroby o takim id");
        return disease;
    }

    public DiseaseDTO getDisease(Integer diseaseId) {
        return DiseaseEntityMapper.mapDiseaseEntity(checkIfDiseaseExist(diseaseId));
    }

    @Transactional
    public void addDiseaseToPatient(DiseaseDTO diseaseDTO) {
        PatientEntity patient = patientService.checkIfPatientExist(diseaseDTO.getPatientEmail());
        Set<DiseaseEntity> patientDiseases = getDiseases(diseaseDTO.getPatientEmail());
        DiseaseEntity diseaseEntity = DiseaseEntityMapper.mapNewDisease(diseaseDTO, patient);
        patientDiseases.add(diseaseEntity);
        diseaseRepository.saveAllAndFlush(patientDiseases);
    }

    public Set<DiseaseEntity> getDiseases(String email) {
        return diseaseRepository.getDiseases(email, Sort.by(Sort.Direction.ASC, "since"));
    }


    public void updateDisease(DiseaseDTO diseaseDTO) {
        DiseaseEntity existingDisease = checkIfDiseaseExist(diseaseDTO.getDiseaseId());
        existingDisease.setCured(diseaseDTO.getCured());
        existingDisease.setDescription(diseaseDTO.getDescription());
        existingDisease.setName(diseaseDTO.getName());
        existingDisease.setSince(diseaseDTO.getSince());
        diseaseRepository.saveAndFlush(existingDisease);

    }
}
