package de.boadu.leichtathletik.vereinsdatenbank.competitionresult.repository;

import de.boadu.leichtathletik.vereinsdatenbank.competitionresult.dao.CompetitionResultDAO;
import de.boadu.leichtathletik.vereinsdatenbank.competitionresult.dto.DiciplineDTO;
import de.boadu.leichtathletik.vereinsdatenbank.competitionresult.dto.PersonalBestDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;

public interface CompetitionResultRepository extends JpaRepository<CompetitionResultDAO, BigInteger> {

    List<DiciplineDTO> findDistinctDiciplineByStartpassnummer(int startpassnummer);

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
    PersonalBestDTO findPersonalBestByDisciplineAndStartpassnummer(@Param("dicipline") String dicipline,
                                                                   @Param("startpassnummer") int startpassnummer);
}
