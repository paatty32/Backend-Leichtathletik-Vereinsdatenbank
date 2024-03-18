package de.boadu.leichtathletik.vereinsdatenbank.competitionresult.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigInteger;
import java.sql.Timestamp;

@Entity
@Table(name="wettkampfergebnis")
public class CompetitionResultDAO {

    @Id
    private BigInteger id;

    @Column(name="datum")
    private Timestamp date;

    @Column(name="ort")
    private String place;

    @Column(name="disziplin")
    private String dicipline;

    @Column(name="ergebnis")
    private Timestamp result;

    @Column(name="halle")
    private boolean isIndoor;

    @Column(name="wind")
    private String wind;

    @Column(name="kommentar")
    private String comment;

    @Column(name="startpassnummer")
    private int startpassnummer;

    @Column(name="altersklasse")
    private String ageGroup;

    @Column(name="link")
    private String resultLink;

    @Override
    public String toString() {
        return this.result + " " + this.place;
    }
}
