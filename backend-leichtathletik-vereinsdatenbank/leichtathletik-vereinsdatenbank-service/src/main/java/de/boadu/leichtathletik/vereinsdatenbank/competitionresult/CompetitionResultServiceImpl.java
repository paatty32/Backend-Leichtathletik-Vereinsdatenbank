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

    private String datePattern = "dd.MM.yyyy";
    private String sprintResultPattern = "ss.SS";

    public CompetitionResultServiceImpl(CompetitionResultRepository competitionResultRepository) {
        this.competitionResultRepository = competitionResultRepository;
    }

    @Override
    public List<PersonalBest> getPersonalBestsOf(int startpassnummer) {

        List<DiciplineDTO> athleteDisciplines = this.getDisciplinesBy(startpassnummer);

        List<PersonalBest> personalBests = new ArrayList<>();

        athleteDisciplines.forEach(dicipline -> {

            PersonalBestDTO personalBestTemp = this.competitionResultRepository
                    .findPersonalBestByDisciplineAndStartpassnummer(dicipline.dicipline(), startpassnummer);

            String formattedTime = this.formatTimeStamp(personalBestTemp.date(), this.datePattern);
            String formattedResult = this.formatTimeStamp(personalBestTemp.result(), this.sprintResultPattern);

            PersonalBest personalBest = createPersonalBest(formattedTime, formattedResult, personalBestTemp);

            personalBests.add(personalBest);

        });

        return personalBests;

    }

    @Override
    public List<PersonalBest> getSeasonalBestOf(int year, int startpassnummer) {

        List<DiciplineDTO> athleteDisciplines = this.getDisciplinesBy(startpassnummer);

        List<PersonalBest> seasonalBests = new ArrayList<>();

        athleteDisciplines.forEach(dicipline -> {

            PersonalBestDTO seasonBestByDisciplieTemp = this.competitionResultRepository
                    .findPersonalBestByDisciplineAndYearAndStartpassnummer(year, startpassnummer, dicipline.dicipline());

            String formattedTime = this.formatTimeStamp(seasonBestByDisciplieTemp.date(), this.datePattern);
            String formattedResult = this.formatTimeStamp(seasonBestByDisciplieTemp.result(), this.sprintResultPattern);

            PersonalBest seasonBestByDiscipline = createPersonalBest(formattedTime, formattedResult, seasonBestByDisciplieTemp);

            seasonalBests.add(seasonBestByDiscipline);

        });

        return seasonalBests;
    }

    @Override
    public Integer getCompetitionCountOf(int startpassnummer) {

        Integer competitionCount = this.competitionResultRepository
                                        .countResultByStartpassnummer(startpassnummer);

        return competitionCount;
    }

    @Override
    public Integer getDisciplineCountOf(int startpassnummer) {

        Integer disciplineCount = this.competitionResultRepository
                                        .countDiciplineByStartpassnummer(startpassnummer);

        return disciplineCount;
    }

    @Override
    public List<Integer> getCompetitionYearsOf(int startpassnummer) {

        List<Integer> competitionYears = this.competitionResultRepository.findCompetitionYearsByStartpassnummer(startpassnummer);

        return competitionYears;
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

    private PersonalBest createPersonalBest(String formattedTime, String formattedResult, PersonalBestDTO personalBestTemp) {

        PersonalBest personalBest = new PersonalBest(formattedTime,
                formattedResult,
                personalBestTemp.place(),
                personalBestTemp.dicipline());
        return personalBest;

    }
}
