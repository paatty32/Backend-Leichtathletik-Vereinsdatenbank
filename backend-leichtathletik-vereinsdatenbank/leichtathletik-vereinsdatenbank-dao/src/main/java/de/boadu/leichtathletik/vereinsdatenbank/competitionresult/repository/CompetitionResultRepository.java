package de.boadu.leichtathletik.vereinsdatenbank.competitionresult.repository;

import de.boadu.leichtathletik.vereinsdatenbank.competitionresult.dao.CompetitionResultDAO;
import de.boadu.leichtathletik.vereinsdatenbank.competitionresult.dto.DiciplineDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.List;

public interface CompetitionResultRepository extends JpaRepository<CompetitionResultDAO, BigInteger> {

    List<DiciplineDTO> findDistinctDiciplineByStartpassnummer(int startpassnummer);
}
