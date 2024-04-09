package de.boadu.leichtathletik.vereinsdatenbank.competitionresult;

import de.boadu.leichtathletik.vereinsdatenbank.competitionresult.dto.CompetitionResult;
import de.boadu.leichtathletik.vereinsdatenbank.competitionresult.dto.PersonalBest;

import java.util.HashMap;
import java.util.List;

public interface CompetitionResultService {

    List<PersonalBest> getPersonalBestsOf(int startpassnummer);

    List<PersonalBest> getSeasonalBestOf(int startpassnummer, int year);

    Integer getCompetitionCountOf(int startpassnummer);

    Integer getDisciplineCountOf(int startpassnummer);

    List<Integer> getCompetitionYearsOf(int startpassnummer);

    HashMap<String, List<CompetitionResult>> getCompetitionResultsByYearOf(int startpassnummer, int year);


}
