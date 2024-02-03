package de.boadu.leichtathletik.vereinsdatenbank.athlete.repository;

import de.boadu.leichtathletik.vereinsdatenbank.athlete.dao.AthleteDAO;
import de.boadu.leichtathletik.vereinsdatenbank.athlete.dto.AthleteDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AthleteRepository extends JpaRepository<AthleteDAO, Integer> {

    List<AthleteDTO> findAthleteByNameIgnoreCase(String name);

    List<AthleteDTO>findAthleteBySurnameIgnoreCase(String surname);

    AthleteDTO findAthleteByStartpassnummer(int startpassnumemr);

}
