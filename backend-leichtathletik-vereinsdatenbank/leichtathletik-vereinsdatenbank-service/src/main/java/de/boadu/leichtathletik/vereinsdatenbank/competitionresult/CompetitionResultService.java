package de.boadu.leichtathletik.vereinsdatenbank.competitionresult;

import de.boadu.leichtathletik.vereinsdatenbank.competitionresult.dto.PersonalBest;

import java.util.List;

public interface CompetitionResultService {

    List<PersonalBest> getPersonalBestsOf(int startpassnummer);
}
