package pl.medisite.infrastructure.security;


import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name ="medisite_role")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private int id;

    @Column(name = "role")
    private String role;

}
