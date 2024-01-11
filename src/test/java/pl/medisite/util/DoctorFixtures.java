package pl.medisite.util;

import lombok.experimental.UtilityClass;
import pl.medisite.infrastructure.database.entity.DoctorEntity;
import pl.medisite.infrastructure.database.entity.PatientEntity;

@UtilityClass
public class DoctorFixtures {
    public static DoctorEntity doctorEntity1() {
        return DoctorEntity.builder()
                .name("test1")
                .surname("Testowy")
                .specialization("spec1")
                .description("desc1")
                .loginDetails(UserFixtures.userEntity4())
                .phone("+48 123 123 123")
                .build();
    }

    public static DoctorEntity doctorEntity2() {
        return DoctorEntity.builder()
                .name("test2")
                .surname("Testowy")
                .specialization("spec2")
                .description("desc2")
                .loginDetails(UserFixtures.userEntity5())
                .phone("+48 123 123 123")
                .build();
    }

    public static DoctorEntity doctorEntity3() {
        return DoctorEntity.builder()
                .name("test3")
                .surname("Testowy")
                .specialization("spec3")
                .description("desc3")
                .loginDetails(UserFixtures.userEntity6())
                .phone("+48 123 123 123")
                .build();
    }

}
