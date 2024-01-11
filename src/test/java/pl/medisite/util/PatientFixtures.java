package pl.medisite.util;

import lombok.experimental.UtilityClass;
import pl.medisite.infrastructure.database.entity.PatientEntity;
import pl.medisite.infrastructure.security.UserEntity;
@UtilityClass
public class PatientFixtures {

    public static PatientEntity patientEntity1(UserEntity loginDetails) {
        return PatientEntity.builder()
                .id(99)
                .name("test1")
                .surname("Testowy")
                .loginDetails(loginDetails)
                .phone("+48 123 123 123")
                .build();
    }

    public static PatientEntity patientEntity2(UserEntity loginDetails) {
        return PatientEntity.builder()
                .id(100)
                .name("test2")
                .surname("Testowy")
                .loginDetails(loginDetails)
                .phone("+48 123 123 123")
                .build();
    }

    public static PatientEntity patientEntity3(UserEntity loginDetails) {
        return PatientEntity.builder()
                .id(101)
                .name("test3")
                .surname("Testowy")
                .loginDetails(loginDetails)
                .phone("+48 123 123 123")
                .build();
    }
}
