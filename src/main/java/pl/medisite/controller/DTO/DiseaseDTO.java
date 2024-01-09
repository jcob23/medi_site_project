package pl.medisite.controller.DTO;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiseaseDTO {

    private Integer diseaseId;

    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

    @PastOrPresent
    @NotNull
    private LocalDate since;

    private String patientEmail;

    private Boolean cured;

    public DiseaseDTO(String patientEmail) {
        this.patientEmail = patientEmail;
    }
}
