package pl.medisite.infrastructure.database.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
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
}
