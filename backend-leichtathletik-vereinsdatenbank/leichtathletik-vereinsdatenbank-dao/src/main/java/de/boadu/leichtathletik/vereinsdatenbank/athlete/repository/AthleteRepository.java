package de.boadu.leichtathletik.vereinsdatenbank.athlete.repository;

import de.boadu.leichtathletik.vereinsdatenbank.athlete.dao.AthleteDAO;
import de.boadu.leichtathletik.vereinsdatenbank.athlete.dto.AthleteDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AthleteRepository extends JpaRepository<AthleteDAO, Integer> {

    List<AthleteDTO> findAthleteByNameIgnoreCase(String name);

    List<AthleteDTO>findAthleteBySurnameIgnoreCase(String surname);

    AthleteDTO findAthleteByStartpassnummer(int startpassnumemr);

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
