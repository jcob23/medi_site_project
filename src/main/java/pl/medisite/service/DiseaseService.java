package pl.medisite.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.medisite.controller.DTO.DiseaseDTO;
import pl.medisite.infrastructure.database.entity.DiseaseEntity;
import pl.medisite.infrastructure.database.entity.PatientEntity;
import pl.medisite.infrastructure.database.mapper.DiseaseEntityMapper;
import pl.medisite.infrastructure.database.repository.DiseaseRepository;

import java.util.Set;

@Service
@AllArgsConstructor
public class DiseaseService {

    private DiseaseRepository diseaseRepository;
    private AppointmentService appointmentService;

    public void addDiseaseToPatientByAppointmentId(Integer appointmentId, DiseaseDTO diseaseDTO) {
        PatientEntity patient = appointmentService.getPatientByAppointmentId(appointmentId);
        Set<DiseaseEntity> patientDiseases = getDiseases(patient.getLoginDetails().getEmail());
        DiseaseEntity diseaseEntity = DiseaseEntityMapper.mapNewDisease(diseaseDTO, patient);
        patientDiseases.add(diseaseEntity);
        diseaseRepository.saveAllAndFlush(patientDiseases);
    }

    public Set<DiseaseEntity> getDiseases(String email) {
        return diseaseRepository.getDiseases(email, Sort.by(Sort.Direction.ASC, "since"));
    }

}
