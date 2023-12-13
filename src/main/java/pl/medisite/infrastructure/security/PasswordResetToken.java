package pl.medisite.infrastructure.security;

import jakarta.persistence.*;

import java.time.LocalDate;

//https://www.baeldung.com/spring-security-registration-i-forgot-my-password
@Entity
public class PasswordResetToken {

    private static final int EXPIRATION = 60 * 24;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    @OneToOne(targetEntity = UserEntity.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private UserEntity user;

    private LocalDate expiryDate;
}