package pl.medisite.infrastructure.security;

import jakarta.persistence.*;
import lombok.*;
import pl.medisite.infrastructure.security.ForgotPassword.MediSiteToken;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(of = "email")
@ToString(exclude = "role")
@Table(name = "medisite_user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private RoleEntity role;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "token_id")
    private MediSiteToken token;
}
