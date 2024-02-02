package de.boadu.leichtathletik.vereinsdatenbank.athlete;

import de.boadu.leichtathletik.vereinsdatenbank.athlete.AthleteService;
import de.boadu.leichtathletik.vereinsdatenbank.athlete.dto.AthleteDTO;
import de.boadu.leichtathletik.vereinsdatenbank.athlete.repository.AthleteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AthleteServiceImpl implements AthleteService {

    private final AthleteRepository athleteRepository;

    public AthleteServiceImpl(AthleteRepository athleteRepository) {
        this.athleteRepository = athleteRepository;
    }

    @Override
    public List<AthleteDTO> getAthletesByName(String name) {

        return this.athleteRepository.findAthleteByNameIgnoreCase(name);
    }

    @Override
    public List<AthleteDTO> getAthletesBySurname(String surname) {
        return this.athleteRepository.findAthleteBySurnameIgnoreCase(surname);
    }
}
