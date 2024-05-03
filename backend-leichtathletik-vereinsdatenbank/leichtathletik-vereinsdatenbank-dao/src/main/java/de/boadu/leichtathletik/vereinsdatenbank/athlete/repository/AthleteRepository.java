package de.boadu.leichtathletik.vereinsdatenbank.athlete.repository;

import de.boadu.leichtathletik.vereinsdatenbank.athlete.dao.AthleteDAO;
import de.boadu.leichtathletik.vereinsdatenbank.athlete.dto.AthleteDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AthleteRepository extends JpaRepository<AthleteDAO, Integer> {

    @Query("""
    SELECT new de.boadu.leichtathletik.vereinsdatenbank.athlete.dto.AthleteDTO(a.startpassnummer, a.name, a.surname, a.yearOfBirth, a.gender)
    FROM AthleteDAO a
    WHERE LOWER(a.surname) LIKE :name OR LOWER(a.name) LIKE :name
    OR LOWER(CONCAT(a.name, ' ', a.surname)) LIKE :name
    """)
    List<AthleteDTO> findAthleteByNameIgnoreCase(@Param("name") String name);

    @Query("""
    SELECT new de.boadu.leichtathletik.vereinsdatenbank.athlete.dto.AthleteDTO(a.startpassnummer, a.name, a.surname, a.yearOfBirth, a.gender)
    FROM AthleteDAO a
    WHERE CAST(a.startpassnummer AS string) LIKE :startpassnummer
    """)
    List<AthleteDTO> findAthleteByStartpassnummer(@Param("startpassnummer") String startpassnummer);

    @Query("""
    SELECT new de.boadu.leichtathletik.vereinsdatenbank.athlete.dto.AthleteDTO(a.startpassnummer, a.name, a.surname, a.yearOfBirth, a.gender)
    FROM AthleteDAO a
    WHERE :lowerAgeYearLimit >= a.yearOfBirth  
    AND :upperAgeYearLimit <=  a.yearOfBirth 
    """)
    List<AthleteDTO> findAthletesByAgeBetween(@Param("lowerAgeYearLimit") int lowerAgeYearLimit, @Param("upperAgeYearLimit") int upperAgeYearLimit);

    @Query("""
    SELECT new de.boadu.leichtathletik.vereinsdatenbank.athlete.dto.AthleteDTO(a.startpassnummer, a.name, a.surname, a.yearOfBirth, a.gender)
    FROM AthleteDAO a
    WHERE :lowerAgeYearLimit >= a.yearOfBirth  
    AND :upperAgeYearLimit <=  a.yearOfBirth 
    AND a.gender = 'M'
    """)
    List<AthleteDTO> findMenAthletesByAgeBetween(@Param("lowerAgeYearLimit") int lowerAgeYearLimit, @Param("upperAgeYearLimit") int upperAgeYearLimit);

    @Query("""
    SELECT new de.boadu.leichtathletik.vereinsdatenbank.athlete.dto.AthleteDTO(a.startpassnummer, a.name, a.surname, a.yearOfBirth, a.gender)
    FROM AthleteDAO a
    WHERE :lowerAgeYearLimit >= a.yearOfBirth  
    AND :upperAgeYearLimit <=  a.yearOfBirth 
    AND a.gender = 'W'
    """)
    List<AthleteDTO> findWomanAthletesByAgeBetween(@Param("lowerAgeYearLimit") int lowerAgeYearLimit, @Param("upperAgeYearLimit") int upperAgeYearLimit);
}
