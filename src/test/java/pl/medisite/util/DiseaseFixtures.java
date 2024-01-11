package pl.medisite.util;

import lombok.experimental.UtilityClass;
import pl.medisite.infrastructure.database.entity.DiseaseEntity;
import pl.medisite.infrastructure.database.entity.PatientEntity;

import java.time.LocalDate;
@UtilityClass
public class DiseaseFixtures {


    public static DiseaseEntity disease1(PatientEntity patient) {
        return DiseaseEntity.builder()
                .name("diseaseTestName1")
                .since(LocalDate.of(2024, 1, 1))
                .cured(false)
                .description("testDescription")
                .patient(patient)
                .build();
    }

    public static DiseaseEntity disease2(PatientEntity patient) {
        return DiseaseEntity.builder()
                .name("diseaseTestName1")
                .since(LocalDate.of(2024, 2, 1))
                .cured(false)
                .description("testDescription")
                .patient(patient)
                .build();
    }


}
