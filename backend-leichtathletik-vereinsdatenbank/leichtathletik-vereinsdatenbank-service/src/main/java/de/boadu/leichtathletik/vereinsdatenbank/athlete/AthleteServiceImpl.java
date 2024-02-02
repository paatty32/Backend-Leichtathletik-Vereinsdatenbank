package de.boadu.leichtathletik.vereinsdatenbank.athlete;

import de.boadu.leichtathletik.vereinsdatenbank.athlete.AthleteService;
import de.boadu.leichtathletik.vereinsdatenbank.athlete.dto.AthleteDTO;
import de.boadu.leichtathletik.vereinsdatenbank.athlete.repository.AthleteRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AthleteServiceImpl implements AthleteService {

    private final AthleteRepository athleteRepository;

    public AthleteServiceImpl(AthleteRepository athleteRepository) {
        this.athleteRepository = athleteRepository;
    }

    @Override
    public List<AthleteDTO> getAthletesByName(String name) {

        List<AthleteDTO> athletesByName = this.athleteRepository.findAthleteByNameIgnoreCase(name);

        if(athletesByName == null || athletesByName.isEmpty()){

            List<AthleteDTO> emptyList = new ArrayList<>();

            return emptyList;

        }

        return athletesByName;
    }

    @Override
    public List<AthleteDTO> getAthletesBySurname(String surname) {

        List<AthleteDTO> athleteBySurnameIgnoreCase = this.athleteRepository.findAthleteBySurnameIgnoreCase(surname);

        if(athleteBySurnameIgnoreCase == null || athleteBySurnameIgnoreCase.isEmpty()){

            List<AthleteDTO> emptyList = new ArrayList<>();

            return emptyList;
        }

        return athleteBySurnameIgnoreCase;
    }
}
