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
    private final String DISCTANCE_RESULT_PATTERN = "mm:ss.SS";

    public CompetitionResultServiceImpl(CompetitionResultRepository competitionResultRepository) {
        this.competitionResultRepository = competitionResultRepository;
    }

    @Override
    public List<PersonalBest> getPersonalBestsOf(int startpassnummer) {
        List<DiciplineDTO> athleteDisciplines = this.getDisciplinesBy(startpassnummer);

        List<PersonalBest> personalBests = this.getPersonalBestsByDiscipline(startpassnummer, athleteDisciplines);

        return personalBests;
    }

    private List<PersonalBest> getPersonalBestsByDiscipline(int startpassnummer, List<DiciplineDTO> athleteDisciplines) {
        List<PersonalBest> personalBests = new ArrayList<>();

        if(athleteDisciplines != null) {
            athleteDisciplines.forEach(discipline -> {
                PersonalBestDTO personalBestTemp = this.competitionResultRepository.findPersonalBestByDiscipline(
                        discipline.dicipline(), startpassnummer);
                PersonalBest personalBest = this.createPersonalBest(personalBestTemp);
                personalBests.add(personalBest);
            });
        }
        return personalBests;
    }

    @Override
    public List<PersonalBest> getSeasonalBestOf(int startpassnummer, int year) {
        List<DiciplineDTO> athleteDisciplines = this.getDisciplinesBy(startpassnummer);

        List<PersonalBest> seasonalBests = getSeasonalBestsOfYear(startpassnummer, year, athleteDisciplines);

        return seasonalBests;
    }

    private List<PersonalBest> getSeasonalBestsOfYear(int startpassnummer, int year, List<DiciplineDTO> athleteDisciplines) {
        List<PersonalBest> seasonalBests = new ArrayList<>();

        if(athleteDisciplines != null) {
            athleteDisciplines.forEach(dicipline -> {
                PersonalBestDTO seasonBestByDisciplieTemp = this.competitionResultRepository
                        .findPersonalBestByDisciplineAndYearAndStartpassnummer(year, startpassnummer, dicipline.dicipline());

                if (seasonBestByDisciplieTemp != null) {
                    PersonalBest seasonBestByDiscipline = this.createPersonalBest(seasonBestByDisciplieTemp);
                    seasonalBests.add(seasonBestByDiscipline);
                }
            });
        }
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

        HashMap<String, List<CompetitionResult>> competitionResults =
                this.createCompetitionResultsByYear(startpassnummer, year, diciplinesByStartpassnummer);

        return competitionResults;
    }

    private HashMap<String, List<CompetitionResult>> createCompetitionResultsByYear(int startpassnummer, int year, List<DiciplineDTO> diciplinesByStartpassnummer) {
        HashMap<String, List<CompetitionResult>> competitionResults = new HashMap<>();

        diciplinesByStartpassnummer.forEach(dicipline ->{

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
        String formattedDate;
        String formattedResult;

        if (result.dicipline().equals("100m") || result.dicipline().equals("200m") || result.dicipline().equals("60m")){
             formattedDate = this.formatTimeStamp(result.date(), this.DATE_PATTERN);
             formattedResult = this.formatTimeStamp(result.result(), this.SPRINT_RESULT_PATTERN);

             if(result.wind() != 0.0) {
                String resultStringValue = String.valueOf(result.wind());

                if(result.wind() > 0){
                    formattedResult = formattedResult + " (" +  "+" + resultStringValue + ")";
                } else {
                    formattedResult = formattedResult + " (" + resultStringValue + ")";
                }
            }
        } else {
             formattedDate = this.formatTimeStamp(result.date(), this.DATE_PATTERN);
             formattedResult = this.formatTimeStamp(result.result(), this.DISCTANCE_RESULT_PATTERN);
        }

        CompetitionResult competitionResult = new CompetitionResult(formattedDate,
                formattedResult,
                result.place(),
                result.resultLink(),
                result.ageGroup(),
                true);

        return competitionResult;
    }

    private PersonalBest createPersonalBest(PersonalBestDTO personalBestDTO) {
        String formattedDate;
        String formattedResult;

        if (personalBestDTO.dicipline().equals("100m") || personalBestDTO.dicipline().equals("200m")
                || personalBestDTO.dicipline().equals("300m")
                || personalBestDTO.dicipline().equals("60m")
                || personalBestDTO.dicipline().equals("75m")
                || personalBestDTO.dicipline().equals("400m")
                || personalBestDTO.dicipline().equals("50m")){
            formattedDate = this.formatTimeStamp(personalBestDTO.date(), this.DATE_PATTERN);
            formattedResult = this.formatTimeStamp(personalBestDTO.result(), this.SPRINT_RESULT_PATTERN);

            if(personalBestDTO.wind() != 0.0) {
                String resultStringValue = String.valueOf(personalBestDTO.wind());

                if(personalBestDTO.wind() > 0){
                    formattedResult = formattedResult + " (" +  "+" + resultStringValue + ")";
                } else {
                    formattedResult = formattedResult + " (" + resultStringValue + ")";
                }
            }
        } else {
            formattedDate = this.formatTimeStamp(personalBestDTO.date(), this.DATE_PATTERN);
            formattedResult = this.formatTimeStamp(personalBestDTO.result(), this.DISCTANCE_RESULT_PATTERN);
        }

        PersonalBest personalBest = new PersonalBest(formattedDate,
                formattedResult,
                personalBestDTO.place(),
                personalBestDTO.dicipline());

        return personalBest;
    }
}
