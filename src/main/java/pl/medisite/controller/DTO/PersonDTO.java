package pl.medisite.controller.DTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonDTO {

    private String email;

    private String name;

    private String surname;

    private String role;

    private String phone;

    @Getter
    @NoArgsConstructor
    public static class DoctorDTO extends PersonDTO {

        private String specialization;

        private String description;

        public DoctorDTO(String email, String name, String surname, String role, String phone, String specialization, String description) {
            super(email, name, surname, role, phone);
            this.specialization = specialization;
            this.description = description;
        }
    }
}

