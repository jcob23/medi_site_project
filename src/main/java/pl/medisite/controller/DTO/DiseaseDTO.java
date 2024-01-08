package pl.medisite.controller.DTO;


import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class DiseaseDTO {

    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

    @PastOrPresent
    private LocalDate since;
}
