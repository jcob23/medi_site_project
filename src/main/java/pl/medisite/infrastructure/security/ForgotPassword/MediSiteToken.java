package pl.medisite.infrastructure.security.ForgotPassword;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@Entity
@Table(name = "user_token")
@AllArgsConstructor
@NoArgsConstructor
public class MediSiteToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private long id;


    @Column(name = "token")
    private UUID uuid;

    @Column(name = "expiration_time")
    private LocalDateTime expirationTime;

}
