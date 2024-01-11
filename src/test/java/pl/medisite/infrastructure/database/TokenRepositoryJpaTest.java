package pl.medisite.infrastructure.database;

import lombok.AllArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.medisite.infrastructure.security.ForgotPassword.MediSiteToken;
import pl.medisite.infrastructure.security.ForgotPassword.TokenRepository;
import pl.medisite.util.TokenFixtures;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor(onConstructor = @__(@Autowired))
public class TokenRepositoryJpaTest extends AbstractJpaIT{

    private TokenRepository tokenRepository;


    @Test
    void tokenCanBeFoundCorrectly(){
        List<MediSiteToken> mediSiteTokens = List.of(
                TokenFixtures.token1(), TokenFixtures.token2(), TokenFixtures.token3());
        tokenRepository.saveAll(mediSiteTokens);
        List<MediSiteToken> foundTokens = tokenRepository.findAll();
        Assertions.assertThat(foundTokens.size()).isEqualTo(3);
    }

    @Test
    void tokenCanBeDeletedCorrectly(){
        List<MediSiteToken> mediSiteTokens = List.of(
                TokenFixtures.token1(), TokenFixtures.token2(), TokenFixtures.token3());
        tokenRepository.saveAllAndFlush(mediSiteTokens);
        tokenRepository.deleteById(1);
        List<MediSiteToken> foundTokens = tokenRepository.findAll();
        Assertions.assertThat(foundTokens).doesNotContain(TokenFixtures.token1());
    }

    @Test
    void tokenCanBeDeletedByExpirationDateCorrectly(){
        List<MediSiteToken> mediSiteTokens = List.of(
                TokenFixtures.token1(), TokenFixtures.token2(), TokenFixtures.token3());
        tokenRepository.saveAll(mediSiteTokens);
        tokenRepository.deleteByExpirationTimeBefore(LocalDateTime.of(2024,2,2,0,0));
        List<MediSiteToken> foundTokens = tokenRepository.findAll();
        Assertions.assertThat(foundTokens.size()).isEqualTo(1);
    }


}
