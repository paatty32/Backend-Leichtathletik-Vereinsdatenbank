package de.boadu.leichtathletik.vereinsdatenbank.athlete;

import de.boadu.leichtathletik.vereinsdatenbank.athlete.dto.AthleteDTO;

import java.util.List;

public interface AthleteService {

    List<AthleteDTO> getAthleteByName(String name);

}
