package pl.medisite.infrastructure.database;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import pl.medisite.infrastructure.database.entity.DiseaseEntity;
import pl.medisite.infrastructure.database.entity.PatientEntity;
import pl.medisite.infrastructure.database.repository.DiseaseRepository;
import pl.medisite.infrastructure.database.repository.PatientRepository;
import pl.medisite.util.DiseaseFixtures;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DiseaseRepositoryJpaTest extends AbstractJpaIT {

    DiseaseRepository diseaseRepository;

    PatientRepository patientRepository;


    @Test
    void shouldGetDiseasesByUserEmail() {
        // given
        PatientEntity patient2 = patientRepository.findByEmail("user2@medisite.pl");
        DiseaseEntity disease2 = DiseaseFixtures.disease2(patient2);

        diseaseRepository.save(disease2);

        // when
        Set<DiseaseEntity> diseases = diseaseRepository.getDiseases("user2@medisite.pl", Sort.unsorted());

        // then
        assertThat(diseases).hasSize(1);
    }

    @Test
    void thatReturnsEmptySetWhenUserHasNoDiseases() {
        // when
        Set<DiseaseEntity> diseases = diseaseRepository.getDiseases("test2@email.com", Sort.unsorted());

        // then
        assertThat(diseases).isEmpty();
    }

    @Test
    void thatGetDiseaseByDiseaseIdWorksCorrectly() {
        // given
        PatientEntity patient1 = patientRepository.findByEmail("user1@medisite.pl");
        DiseaseEntity disease = DiseaseFixtures.disease1(patient1);
        diseaseRepository.save(disease);

        // when
        DiseaseEntity retrievedDisease = diseaseRepository.getByDiseaseId(disease.getDiseaseId());

        // then
        assertThat(retrievedDisease).isNotNull();
        assertThat(retrievedDisease.getDiseaseId()).isEqualTo(disease.getDiseaseId());
    }

    @Test
    void thatReturnNullWhenDiseaseIdNotExists() {
        // when
        DiseaseEntity retrievedDisease = diseaseRepository.getByDiseaseId(-1);

        // then
        assertThat(retrievedDisease).isNull();
    }

    @Test
    void thatDiseaseEntitySavesCorrectly() {
        // given
        PatientEntity patient1 = patientRepository.findByEmail("user1@medisite.pl");
        DiseaseEntity disease = DiseaseFixtures.disease1(patient1);
        // when
        DiseaseEntity savedDisease = diseaseRepository.save(disease);
        // then
        assertThat(savedDisease).isNotNull();
        assertThat(savedDisease.getDiseaseId()).isNotNull();
    }


}
