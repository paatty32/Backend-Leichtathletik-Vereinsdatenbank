package de.boadu.leichtathletik.vereinsdatenbank.competitionresult;

import de.boadu.leichtathletik.vereinsdatenbank.competitionresult.dto.PersonalBest;

import java.util.List;

public interface CompetitionResultService {

    List<PersonalBest> getPersonalBestsOf(int startpassnummer);

    List<PersonalBest> getSeasonalBestOf(int year, int startpassnummer);

    Integer getCompetitionCountOf(int startpassnummer);

    Integer getDisciplineCountOf(int startpassnummer);

    List<Integer> getCompetitionYearsOf(int startpassnummer);


}
