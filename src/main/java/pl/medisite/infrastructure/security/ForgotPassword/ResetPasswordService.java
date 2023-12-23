package pl.medisite.infrastructure.security.ForgotPassword;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.medisite.infrastructure.security.UserEntity;
import pl.medisite.service.UserService;

import java.time.LocalDateTime;
import java.util.UUID;

//https://www.baeldung.com/spring-security-registration-i-forgot-my-password
@Service
@AllArgsConstructor
@Slf4j
public class ResetPasswordService {

    private JavaMailSender mailSender;
    private UserService userService;
    private TokenRepository tokenRepository;

    public void sendMail(String email) throws MessagingException {
        MimeMessage mail = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mail, true);
        messageHelper.setTo(email);
        messageHelper.setSubject("MediSite Reset hasła");
        String link = createLinkWithToken(email);
        messageHelper.setText("<p>Hej,</p>"
                + "<p>Poprosieś o zresetowanie swojego hasła.</p>"
                + "<p>Kliknij w ten link aby wprowadzić nowe hasło</p>"
                + "<p><a href=\"" + link + "\">Zmień hasło</a></p>"
                + "<br>"
                + "<p>Zignoruj ten mail jeśli pamiętasz swoje hasło, lub to nie ty wykonałeś tą prośbę .</p>", true);
        mailSender.send(mail);
    }

    private String createLinkWithToken(String email){
        UUID token = createToken(email).getUuid();
        return "http://localhost:8190/medisite/forget/reset?token=" + token;
    }
    private MediSiteToken createToken(String email){
        MediSiteToken token = MediSiteToken.builder()
                .uuid(UUID.randomUUID())
                .expirationTime(LocalDateTime.now().plusMinutes(1))
                .build();

        UserEntity user = userService.findByEmail(email);
        user.setToken(token);
        tokenRepository.save(token);
        return token;
    }
    @Scheduled(cron = "0 0 5 * * *")
    @Transactional
    public void clearOldTokens(){
        log.info("###sprzątam!###");
        tokenRepository.deleteByExpirationTimeBefore(LocalDateTime.now());
    }

}