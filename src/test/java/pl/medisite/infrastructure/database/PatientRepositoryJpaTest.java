package pl.medisite.infrastructure.database;

import lombok.AllArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.medisite.infrastructure.database.entity.PatientEntity;
import pl.medisite.infrastructure.database.repository.PatientRepository;
import pl.medisite.infrastructure.security.UserRepository;
import pl.medisite.util.PatientFixtures;
import pl.medisite.util.UserFixtures;

import java.util.List;
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PatientRepositoryJpaTest extends AbstractJpaIT{

    private PatientRepository patientRepository;
    private  UserRepository userRepository;


    @Test
    void patientCanBeFoundCorrectly(){
        userRepository.saveAll(List.of(
                UserFixtures.userEntity1(),
                UserFixtures.userEntity3(),
                UserFixtures.userEntity2()));
        List<PatientEntity> patient = List.of(
                PatientFixtures.patientEntity1(userRepository.findByEmail("test1@email.com")),
                PatientFixtures.patientEntity3(userRepository.findByEmail("test2@email.com")),
                PatientFixtures.patientEntity2(userRepository.findByEmail("test3@email.com"))
        );
        patientRepository.saveAll(patient);
        List<PatientEntity> foundPatients = patientRepository.findAll();
        Assertions.assertThat(foundPatients.size()).isEqualTo(5); // 5 ponieważ 2 uzytkowników dodano w migracjach
    }
    @Test
    void patientCanBeFoundByEmailCorrectly(){
        userRepository.saveAll(List.of(
                UserFixtures.userEntity1(),
                UserFixtures.userEntity3(),
                UserFixtures.userEntity2()));
        List<PatientEntity> patient = List.of(
                PatientFixtures.patientEntity1(userRepository.findByEmail("test1@email.com")),
                PatientFixtures.patientEntity2(userRepository.findByEmail("test2@email.com")),
                PatientFixtures.patientEntity3(userRepository.findByEmail("test3@email.com"))
        );
        patientRepository.saveAll(patient);
        PatientEntity foundPatient = patientRepository.findByEmail("test1@email.com");
        Assertions.assertThat(foundPatient).isEqualTo(PatientFixtures.patientEntity1(UserFixtures.userEntity1()));
    }
    @Test
    void patientCanBeDeletedCorrectly(){
        userRepository.save(UserFixtures.userEntity3());
        PatientEntity patient  = PatientFixtures.patientEntity3(userRepository.findByEmail("test3@email.com"));
        patientRepository.save(patient);
        patientRepository.deleteByMail("test3@email.com");
        List<PatientEntity> foundPatients = patientRepository.findAll();
        Assertions.assertThat(foundPatients).doesNotContain(patient);
    }

    @Test
    void patientInformationRetrievedCorrectly(){
        userRepository.saveAll(List.of(
                UserFixtures.userEntity1(),
                UserFixtures.userEntity3(),
                UserFixtures.userEntity2()));
        List<PatientEntity> patient = List.of(
                PatientFixtures.patientEntity1(userRepository.findByEmail("test1@email.com")),
                PatientFixtures.patientEntity2(userRepository.findByEmail("test2@email.com")),
                PatientFixtures.patientEntity3(userRepository.findByEmail("test3@email.com"))
        );
        patientRepository.saveAll(patient);
        PatientEntity foundPatient = patientRepository.findByEmail("test1@email.com");

        Assertions.assertThat(foundPatient.getLoginDetails()).isNotNull();
    }
}
