package pl.medisite.controller.DTO;

import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewPatientDTO {
    //Class used only for creating new user
    @Id
    @NotBlank
    @Email(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")
    String email;

    @Size(min = 4)
    String password;

    @NotBlank
    String name;

    @NotBlank
    String surname;

    String role;

    @Size(min = 7, max = 15)
    @Pattern(regexp = "^[+]\\d{2}\\s\\d{3}\\s\\d{3}\\s\\d{3}$")
    String phone;

    public void setName(String name) {
        this.name = name.stripTrailing();
    }

    public void setSurname(String surname) {
        this.surname = surname.stripTrailing();
    }

    public void setPhone(String phone) {
        this.phone = phone.stripTrailing();
    }

    public Map<String, String> asMap() {
        Map<String, String> result = new HashMap<>();
        result.put("email", email == null ? "test@email.com" : email);
        result.put("password", password == null ? "1234" : password);
        result.put("name", name == null ? "test" : name);
        result.put("surname", surname == null ? "test" : surname);
        result.put("phone", phone == null ? "+48 123 123 123" : phone);
        return result;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class NewDoctorDTO extends NewPatientDTO {

        private String specialization;

        private String description;

        public NewDoctorDTO(
                @NotBlank
                @Email(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$") String email,
                @Size(min = 4) String password,
                @NotBlank String name,
                @NotBlank String surname,
                String role,
                @Size(min = 7, max = 15) @Pattern(regexp = "^[+]\\d{2}\\s\\d{3}\\s\\d{3}\\s\\d{3}$") String phone,
                String specialization,
                String description) {
            super(email, password, name, surname, role, phone);
            this.specialization = specialization;
            this.description = description;
        }

        public Map<String, String> asMap() {
            Map<String, String> result = new HashMap<>();
            Optional.ofNullable(email).ifPresent(value -> result.put("email", email));
            Optional.ofNullable(password).ifPresent(value -> result.put("password", password));
            Optional.ofNullable(name).ifPresent(value -> result.put("name", name));
            Optional.ofNullable(surname).ifPresent(value -> result.put("surname", surname));
            Optional.ofNullable(phone).ifPresent(value -> result.put("phone", phone));
            Optional.ofNullable(specialization).ifPresent(value -> result.put("specialization", specialization));
            return result;
        }
    }
}
