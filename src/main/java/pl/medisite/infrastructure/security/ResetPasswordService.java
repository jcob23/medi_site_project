package pl.medisite.infrastructure.security;

import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

//https://www.baeldung.com/spring-security-registration-i-forgot-my-password
@Service
@AllArgsConstructor
public class ResetPasswordService {
    private JavaMailSender mailSender;

    public void sendMail(String email) throws MessagingException {
        MimeMessage mail = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mail, true);
        messageHelper.setTo(email);
        messageHelper.setSubject("Reset hasła");
        messageHelper.setText("aby zresetowac hasło kliknij w ten link ", true);
        mailSender.send(mail);
    }

}