package pl.medisite.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;
import pl.medisite.infrastructure.security.UserEntity;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(of = "loginDetails")
@Table(name = "doctor")
@ToString(exclude = {"appointments","loginDetails"})
public class DoctorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doctor_id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "phone")
    private String phone;

    @Column(name = "specialization")
    private String specialization;

    @Column(name = "description")
    private String description;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "email", referencedColumnName = "email")
    private UserEntity loginDetails;

    @OneToMany
    @JoinColumn(name = "doctor_id", referencedColumnName = "doctor_id")
    private Set<AppointmentEntity> appointments;

}
