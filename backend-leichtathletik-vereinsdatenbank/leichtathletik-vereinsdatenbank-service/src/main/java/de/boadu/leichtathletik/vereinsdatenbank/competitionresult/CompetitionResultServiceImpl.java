package de.boadu.leichtathletik.vereinsdatenbank.competitionresult;

import de.boadu.leichtathletik.vereinsdatenbank.competitionresult.dto.DiciplineDTO;
import de.boadu.leichtathletik.vereinsdatenbank.competitionresult.dto.PersonalBest;
import de.boadu.leichtathletik.vereinsdatenbank.competitionresult.dto.PersonalBestDTO;
import de.boadu.leichtathletik.vereinsdatenbank.competitionresult.repository.CompetitionResultRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class CompetitionResultServiceImpl implements CompetitionResultService{

    private final CompetitionResultRepository competitionResultRepository;

    public CompetitionResultServiceImpl(CompetitionResultRepository competitionResultRepository) {
        this.competitionResultRepository = competitionResultRepository;
    }

    @Override
    public List<PersonalBest> getPersonalBestsOf(int startpassnummer) {

        List<DiciplineDTO> athleteDisciplines = this.getDisciplinesBy(startpassnummer);

        List<PersonalBest> personalBests = new ArrayList<>();

        String datePattern = "dd.MM.yyyy";
        String sprintResultPattern = "ss.SS";

        athleteDisciplines.forEach(dicipline -> {

            PersonalBestDTO personalBestTemp = this.competitionResultRepository
                    .findPersonalBestByDisciplineAndStartpassnummer(dicipline.dicipline(), startpassnummer);

            String formattedTime = this.formatTimeStamp(personalBestTemp.date(), datePattern);
            String formattedResult = this.formatTimeStamp(personalBestTemp.result(), sprintResultPattern);

            PersonalBest personalBest = new PersonalBest(formattedTime,
                    formattedResult,
                    personalBestTemp.place(),
                    personalBestTemp.dicipline());

            personalBests.add(personalBest);

        });

        return personalBests;

    }

    private List<DiciplineDTO> getDisciplinesBy(int startpassnummer){

        List<DiciplineDTO> athleteDisciplines = this.competitionResultRepository
                .findDistinctDiciplineByStartpassnummer(startpassnummer);

        return athleteDisciplines;
    }

    private String formatTimeStamp(Timestamp result, String pattern){

        DateTimeFormatter resultFormat = DateTimeFormatter.ofPattern(pattern);
        String formattedResult = resultFormat.format(result.toLocalDateTime());

        return  formattedResult;
    }
}
