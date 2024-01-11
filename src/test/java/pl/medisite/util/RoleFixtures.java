package pl.medisite.util;

import lombok.experimental.UtilityClass;
import pl.medisite.infrastructure.security.RoleEntity;

@UtilityClass
public class RoleFixtures {

    public static RoleEntity adminRole() {
        return RoleEntity.builder()
                .role("TEST_ADMIN")
                .build();
    }

    public static RoleEntity patientRole() {
        return RoleEntity.builder()
                .role("TEST_PATIENT")
                .build();
    }

    public static RoleEntity doctorRole() {
        return RoleEntity.builder()
                .role("TEST_DOCTOR")
                .build();
    }
}
