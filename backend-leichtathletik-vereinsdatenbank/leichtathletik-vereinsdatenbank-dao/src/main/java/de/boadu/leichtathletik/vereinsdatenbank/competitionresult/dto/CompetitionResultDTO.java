package de.boadu.leichtathletik.vereinsdatenbank.competitionresult.dto;

import java.sql.Timestamp;

public record CompetitionResultDTO(
        Timestamp date,
        Timestamp result,
        String place,
        String resultLink,
        String ageGroup,
        String dicipline
) {
}
