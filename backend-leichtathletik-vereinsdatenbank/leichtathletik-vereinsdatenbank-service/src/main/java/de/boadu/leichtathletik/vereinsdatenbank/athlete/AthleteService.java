package de.boadu.leichtathletik.vereinsdatenbank.athlete;

import java.util.List;

public interface AthleteService {

    List<Athlete> getAthletesByName(String name);

    List<Athlete> getAthletesBySurname(String name);

    Athlete getAthleteByStartpassnummer(int startpassnummer);

    List<Athlete> getAthletesByAgeGroup(String ageGroup);
}
