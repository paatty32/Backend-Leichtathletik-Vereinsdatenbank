package de.boadu.leichtathletik.vereinsdatenbank.competitionresult.dto;

public record CompetitionResult(
        String date,
        String result,
        String place,
        String resultLink
) {
}
