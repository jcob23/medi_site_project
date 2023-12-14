package pl.medisite.controller.buisness;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;



@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatientDTO {

    @Id
    @NotBlank
    @Email(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")
    String email;

    @Size(min = 4)
    String password;

    String name;

    String surname;

    String role;

    @Size(min = 7, max = 15)
    @Pattern(regexp = "^[+]\\d{2}\\s\\d{3}\\s\\d{3}\\s\\d{3}$")
    String phone;

    public Map<String,String> asMap(){
        Map<String, String > result = new HashMap<>();
        Optional.ofNullable(email).ifPresent(value -> result.put("email",email));
        Optional.ofNullable(password).ifPresent(value -> result.put("password",password));
        Optional.ofNullable(name).ifPresent(value -> result.put("name",name));
        Optional.ofNullable(surname).ifPresent(value -> result.put("surname",surname));
        Optional.ofNullable(phone).ifPresent(value -> result.put("phone",phone));
        return result;
    }


}
