package de.boadu.leichtathletik.vereinsdatenbank.competitionresult.dto;

import java.sql.Timestamp;

public record PersonalBestDTO(
        Timestamp date,
        Timestamp result,
        String place,
        String dicipline
) {
}
