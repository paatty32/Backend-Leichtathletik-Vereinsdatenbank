package de.boadu.leichtathletik.vereinsdatenbank.athlete.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="altersklasse")
public class AgeGroupDAO {

    @Id
    private int id;

    @Column(name = "altersklasse")
    private String ageGroup;

    @Column(name = "untere_grenze")
    private int lowerLimit;

    @Column(name="obere_grenze")
    private int upperLimit;



}
