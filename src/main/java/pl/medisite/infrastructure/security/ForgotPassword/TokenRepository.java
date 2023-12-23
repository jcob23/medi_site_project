package pl.medisite.infrastructure.security.ForgotPassword;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.time.LocalDateTime;
import java.util.List;

public interface TokenRepository extends JpaRepository<MediSiteToken, Long> {

    @Modifying
    List<MediSiteToken> deleteByExpirationTimeBefore(LocalDateTime currentDateTime);


}
