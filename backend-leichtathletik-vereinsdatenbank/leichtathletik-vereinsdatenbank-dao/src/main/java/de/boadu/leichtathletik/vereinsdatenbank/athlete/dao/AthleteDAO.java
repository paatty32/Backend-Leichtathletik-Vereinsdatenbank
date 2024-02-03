package de.boadu.leichtathletik.vereinsdatenbank.athlete.dao;

import de.boadu.leichtathletik.vereinsdatenbank.competitionresault.CompetitionResultsDAO;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="Athlete")
public class AthleteDAO {

    @Id
    @Column(name="startpassnummer")
    public int startpassnummer;

    @Column(name="nachname")
    public String surname;

    @Column(name="vorname")
    public String name;

    @Column(name="jahrgang")
    public int yearOfBirth;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "startpassnummer" )
    public List<CompetitionResultsDAO> competitionResults;

}
