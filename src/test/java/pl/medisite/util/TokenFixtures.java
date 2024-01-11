package pl.medisite.util;

import lombok.experimental.UtilityClass;
import pl.medisite.infrastructure.security.ForgotPassword.MediSiteToken;

import java.time.LocalDateTime;
import java.util.UUID;

@UtilityClass
public class TokenFixtures {
    public static MediSiteToken token1() {
        return MediSiteToken.builder()
                .uuid(UUID.randomUUID())
                .id(1)
                .expirationTime(LocalDateTime.of(2024,1,1,0,0))
                .build();
    }

    public static MediSiteToken token2() {
        return MediSiteToken.builder()
                .uuid(UUID.randomUUID())
                .id(2)
                .expirationTime(LocalDateTime.of(2024,2,1,0,0))
                .build();
    }

    public static MediSiteToken token3() {
        return MediSiteToken.builder()
                .uuid(UUID.randomUUID())
                .id(3)
                .expirationTime(LocalDateTime.of(2024,3,1,0,0))
                .build();
    }
}
