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
@Table(name = "patient")
@ToString(exclude = {"appointments","loginDetails"})
public class PatientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "phone")
    private String phone;

    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "email", referencedColumnName = "email")
    private UserEntity loginDetails;

    @OneToMany(mappedBy = "patient")
    private Set<AppointmentEntity> appointments;


}
