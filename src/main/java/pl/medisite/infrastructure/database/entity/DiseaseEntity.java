package pl.medisite.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.medisite.controller.DTO.DiseaseDTO;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "patient_diseases")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiseaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "disease_id")
    private int disease_id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "cured")
    private Boolean cured;

    @Column(name = "since")
    private LocalDate since;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private PatientEntity patient;




}
