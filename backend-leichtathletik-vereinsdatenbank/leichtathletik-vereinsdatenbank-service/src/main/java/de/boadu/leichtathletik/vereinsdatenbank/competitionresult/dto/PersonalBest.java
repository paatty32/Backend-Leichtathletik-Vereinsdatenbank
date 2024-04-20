package de.boadu.leichtathletik.vereinsdatenbank.competitionresult.dto;

public record PersonalBest(
        String date,
        String result,
        String place,
        String dicipline
) {
}