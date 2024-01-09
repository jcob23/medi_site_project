package pl.medisite.controller.DTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.extern.slf4j.Slf4j;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonDTO {

    private String email;

    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    private String role;

    @Size(min = 7, max = 15)
    @Pattern(regexp = "^[+]\\d{2}\\s\\d{3}\\s\\d{3}\\s\\d{3}$")
    private String phone;

    @Getter
    @Setter
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

