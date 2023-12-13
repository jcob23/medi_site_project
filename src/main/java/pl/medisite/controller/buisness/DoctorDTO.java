package pl.medisite.controller.buisness;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDTO extends PersonDTO {


    private String specialization;

    public Map<String, String> asMap () {
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
