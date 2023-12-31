package pl.medisite.controller.DTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class PersonDTO {

    @Id
    private  String email;

    private String name;

    private String surname;

    private String role;

    private String phone;

    @Getter
    @Entity
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DoctorDTO extends PersonDTO {

        @Column(name = "specialization")
        private String specialization;

        @Column(name = "description")
        private String description;

    }
}

