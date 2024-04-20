package de.boadu.leichtathletik.vereinsdatenbank.competitionresult.repository;

import de.boadu.leichtathletik.vereinsdatenbank.competitionresult.dao.CompetitionResultDAO;
import de.boadu.leichtathletik.vereinsdatenbank.competitionresult.dto.CompetitionResultDTO;
import de.boadu.leichtathletik.vereinsdatenbank.competitionresult.dto.DiciplineDTO;
import de.boadu.leichtathletik.vereinsdatenbank.competitionresult.dto.PersonalBestDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;

public interface CompetitionResultRepository extends JpaRepository<CompetitionResultDAO, BigInteger> {

    List<DiciplineDTO> findDistinctDiciplineByStartpassnummerOrderByDicipline(int startpassnummer);

    Integer countResultByStartpassnummer(int startpassnummer);

    @Query("""
    SELECT new de.boadu.leichtathletik.vereinsdatenbank.competitionresult.dto.CompetitionResultDTO(
    c.date, c.result , c.place, c.resultLink)
    FROM CompetitionResultDAO c
    WHERE EXTRACT(YEAR FROM c.date) = :year
    AND
    c.startpassnummer = :startpassnummer
    AND
    c.dicipline = :dicipline
    ORDER BY c.result asc
    """)
    List<CompetitionResultDTO> findResultByYear(@Param("startpassnummer") int startpassnummer,
                                                @Param("year") int year,
                                                @Param("dicipline") String dicipline);

    @Query("""
    SELECT DISTINCT EXTRACT(YEAR FROM c.date)
    FROM CompetitionResultDAO c 
    WHERE c.startpassnummer = :startpassnummer
    ORDER BY EXTRACT(YEAR FROM c.date) DESC
    """)
    List<Integer> findCompetitionYearsByStartpassnummer(@Param("startpassnummer") int startpassnummer);

    @Query("""
    SELECT COUNT(DISTINCT c.dicipline)
    FROM CompetitionResultDAO c
    WHERE c.startpassnummer = :startpassnummer
    """)
    Integer countDiciplineByStartpassnummer(@Param("startpassnummer") int startpassnummer);

    @Query("""
    SELECT new de.boadu.leichtathletik.vereinsdatenbank.competitionresult.dto.PersonalBestDTO(
    c.date, c.result , c.place, c.dicipline)
    FROM CompetitionResultDAO c
    WHERE EXTRACT(YEAR FROM c.date) = :seasonYear
    AND
    c.startpassnummer = :startpassnummer
    AND
    c.dicipline = :dicipline
    ORDER BY c.result asc
    LIMIT 1
    """)
    PersonalBestDTO findPersonalBestByDisciplineAndYearAndStartpassnummer(@Param("seasonYear") int seasonYear,
                                                                                @Param("startpassnummer") int startpassnummer,
                                                                                @Param("dicipline") String dicipline);


    @Query("""
    SELECT new de.boadu.leichtathletik.vereinsdatenbank.competitionresult.dto.PersonalBestDTO(
    c.date, c.result , c.place, c.dicipline)
    FROM CompetitionResultDAO c
    WHERE
    c.startpassnummer = :startpassnummer
    AND
    c.dicipline = :dicipline
    ORDER BY c.result asc
    LIMIT 1
    """)
    PersonalBestDTO findPersonalBestByDiscipline(@Param("dicipline") String dicipline,
                                                 @Param("startpassnummer") int startpassnummer);
}
