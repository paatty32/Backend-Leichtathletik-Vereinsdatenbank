package de.boadu.leichtathletik.vereinsdatenbank.competitionresult;

import de.boadu.leichtathletik.vereinsdatenbank.competitionresult.dto.*;
import de.boadu.leichtathletik.vereinsdatenbank.competitionresult.repository.CompetitionResultRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class CompetitionResultServiceImpl implements CompetitionResultService{

    private final CompetitionResultRepository competitionResultRepository;

    private final String DATE_PATTERN = "dd.MM.yyyy";
    private final String SPRINT_RESULT_PATTERN = "ss.SS";

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

            PersonalBest personalBest = createPersonalBest(personalBestTemp);

            personalBests.add(personalBest);

        });

        return personalBests;

    }

    @Override
    public List<PersonalBest> getSeasonalBestOf(int startpassnummer, int year) {

        List<DiciplineDTO> athleteDisciplines = this.getDisciplinesBy(startpassnummer);

        List<PersonalBest> seasonalBests = new ArrayList<>();

        athleteDisciplines.forEach(dicipline -> {

            PersonalBestDTO seasonBestByDisciplieTemp = this.competitionResultRepository
                    .findPersonalBestByDisciplineAndYearAndStartpassnummer(year, startpassnummer, dicipline.dicipline());

            if(seasonBestByDisciplieTemp != null) {

                PersonalBest seasonBestByDiscipline = this.createPersonalBest(seasonBestByDisciplieTemp);
                
                seasonalBests.add(seasonBestByDiscipline);
            }

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

    @Override
    public HashMap<String, List<CompetitionResult>> getCompetitionResultsByYearOf(int startpassnummer, int year) {
        List<DiciplineDTO> diciplinesByStartpassnummer = this.competitionResultRepository
                .findDistinctDiciplineByStartpassnummerOrderByDicipline(startpassnummer);

        HashMap<String, List<CompetitionResult>> competitionResults = new HashMap<>();

        diciplinesByStartpassnummer.forEach( dicipline ->{
            
            List<CompetitionResult> results = new ArrayList<>();
            
            List<CompetitionResultDTO> resultByDicipline = this.competitionResultRepository
                                                            .findResultByYear(startpassnummer, year, dicipline.dicipline());
            if(!resultByDicipline.isEmpty()) {
                resultByDicipline.forEach(result -> {

                    CompetitionResult competitionResult = this.createCompetitionResult(result);

                    results.add(competitionResult);

                });
                competitionResults.put(dicipline.dicipline(), results);
            }
        });


        return competitionResults;
    }

    private List<DiciplineDTO> getDisciplinesBy(int startpassnummer){

        List<DiciplineDTO> athleteDisciplines = this.competitionResultRepository
                                        .findDistinctDiciplineByStartpassnummerOrderByDicipline(startpassnummer);

        return athleteDisciplines;
    }

    private String formatTimeStamp(Timestamp result, String pattern){

        DateTimeFormatter resultFormat = DateTimeFormatter.ofPattern(pattern);
        String formattedResult = resultFormat.format(result.toLocalDateTime());

        return  formattedResult;
    }

    private CompetitionResult createCompetitionResult(CompetitionResultDTO result) {

        String formattedDate = this.formatTimeStamp(result.date(), this.DATE_PATTERN);
        String formattedResult = this.formatTimeStamp(result.result(), this.SPRINT_RESULT_PATTERN);

        CompetitionResult competitionResult = new CompetitionResult(formattedDate,
                formattedResult,
                result.place(),
                result.resultLink());

        return competitionResult;
    }

    private PersonalBest createPersonalBest(PersonalBestDTO personalBestDTO) {

        String formattedTime = this.formatTimeStamp(personalBestDTO.date(), this.DATE_PATTERN);
        String formattedResult = this.formatTimeStamp(personalBestDTO.result(), this.SPRINT_RESULT_PATTERN);

        PersonalBest personalBest = new PersonalBest(formattedTime,
                formattedResult,
                personalBestDTO.place(),
                personalBestDTO.dicipline());

        return personalBest;

    }
}
