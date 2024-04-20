package de.boadu.leichtathletik.vereinsdatenbank.competitionresult;

import de.boadu.leichtathletik.vereinsdatenbank.athlete.dto.AthleteDTO;
import de.boadu.leichtathletik.vereinsdatenbank.competitionresult.dto.*;
import de.boadu.leichtathletik.vereinsdatenbank.competitionresult.repository.CompetitionResultRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CompetitionResultServiceTest {

    @Mock
    private CompetitionResultRepository competitionResultRepository;

    @InjectMocks
    private CompetitionResultServiceImpl competitionResultService;

    private AthleteDTO maxMustermann;

    private Timestamp dateTimeStamp;
    private Timestamp resultTimeStamp;
    private Timestamp twoHundredMeterDateTimestamp;
    private Timestamp twoHundredMeterResultTimestamp;

    private PersonalBestDTO hundredMeterPB;
    private PersonalBestDTO twoHundredMeterPB;

    private CompetitionResultDTO hundredMeterResult;
    private CompetitionResultDTO twoHundredMeterResult;

    private DiciplineDTO hundredMeter;
    private DiciplineDTO twoHundredMeter;

    @BeforeEach
    public void setUp(){
        this.maxMustermann = new AthleteDTO(1111, "Max", "Mustermann", 1999, "M");

        this.hundredMeter = new DiciplineDTO("100m");
        this.twoHundredMeter = new DiciplineDTO("200m");

        long date = Timestamp.valueOf("2024-04-09 00:00:00").getTime();
        this.dateTimeStamp = new Timestamp(date);

        long result = Timestamp.valueOf("1900-01-01 00:00:12.71").getTime();
        this.resultTimeStamp = new Timestamp(result);

        long twoHundredMeterdate = Timestamp.valueOf("2024-04-08 00:00:00").getTime();
        this.twoHundredMeterDateTimestamp = new Timestamp(twoHundredMeterdate);

        long twoHundredMeterResult = Timestamp.valueOf("1900-01-01 00:00:22.71").getTime();
        this.twoHundredMeterResultTimestamp = new Timestamp(twoHundredMeterResult);

        this.hundredMeterPB = new PersonalBestDTO(this.dateTimeStamp, resultTimeStamp, "Düsseldorf", "100m");
        this.twoHundredMeterPB = new PersonalBestDTO(twoHundredMeterDateTimestamp, twoHundredMeterResultTimestamp, "Düsseldorf", "200m");

        this.hundredMeterResult = new CompetitionResultDTO(this.dateTimeStamp, resultTimeStamp, "Köln", "");
        this.twoHundredMeterResult = new CompetitionResultDTO(this.twoHundredMeterDateTimestamp, this.twoHundredMeterResultTimestamp, "köln", "");
    }

    @Test
    public void whenPersonalBestOfStartpassnummerExist_thenReturnPersonalBestOfEachDicipline(){
        List<DiciplineDTO> diciplines = new ArrayList<>();
        diciplines.add(this.hundredMeter);
        diciplines.add(this.twoHundredMeter);

        when(this.competitionResultRepository.findDistinctDiciplineByStartpassnummerOrderByDicipline(
                this.maxMustermann.startpassnummer())).thenReturn(diciplines);
        when(competitionResultRepository.findPersonalBestByDiscipline(diciplines.get(0).dicipline(),
                this.maxMustermann.startpassnummer()))
                .thenReturn(this.hundredMeterPB);
        when(competitionResultRepository.findPersonalBestByDiscipline(diciplines.get(1).dicipline(),
                this.maxMustermann.startpassnummer()))
                .thenReturn(this.twoHundredMeterPB);

        List<PersonalBest> personalBestsOf = this.competitionResultService
                .getPersonalBestsOf(this.maxMustermann.startpassnummer());

        assertThat(personalBestsOf.size()).isEqualTo(2);
    }

    @Test
    public void whenPersonalBestOfStartpassnummerDoesntExist_thenReturnEmptyList(){
        when(this.competitionResultRepository.findDistinctDiciplineByStartpassnummerOrderByDicipline(
                this.maxMustermann.startpassnummer())).thenReturn(null);

        List<PersonalBest> emptyList = this.competitionResultService
                .getPersonalBestsOf(this.maxMustermann.startpassnummer());

        assertThat(emptyList.size()).isEqualTo(0);
    }

    @Test
    public void whenSeasonalBestOfStartpassnummerOfAGivinYearExist_thenReturnSeasonalBestts(){
        List<DiciplineDTO> diciplines = new ArrayList<>();
        diciplines.add(this.hundredMeter);
        diciplines.add(this.twoHundredMeter);

        int year = 2004;

        when(this.competitionResultRepository.findDistinctDiciplineByStartpassnummerOrderByDicipline(this.maxMustermann.startpassnummer()))
                .thenReturn(diciplines);
        when(this.competitionResultRepository.findPersonalBestByDisciplineAndYearAndStartpassnummer(year,
                this.maxMustermann.startpassnummer(), diciplines.get(0).dicipline()))
                .thenReturn(this.hundredMeterPB);
        when(this.competitionResultRepository.findPersonalBestByDisciplineAndYearAndStartpassnummer(year,
                this.maxMustermann.startpassnummer(), diciplines.get(1).dicipline()))
                .thenReturn(this.twoHundredMeterPB);

        List<PersonalBest> seasonalBestsOf2004 = this.competitionResultService
                .getSeasonalBestOf(this.maxMustermann.startpassnummer(), year);

        assertThat(seasonalBestsOf2004.size()).isEqualTo(2);
    }

    @Test
    public void whenSeasonalBestOfStartpassnummerDontExist_thenReturnEmptyList(){
        int year = 2004;

        List<PersonalBest> emptyList = this.competitionResultService.getSeasonalBestOf(this.maxMustermann.startpassnummer(), year);

        assertThat(emptyList.size()).isEqualTo(0);
    }

    @Test
    public void whenAthleteHasCompetition_thenReturnCompetitionCount(){
        when(this.competitionResultRepository.countResultByStartpassnummer(this.maxMustermann.startpassnummer()))
                .thenReturn(4);

        Integer competitionCount = this.competitionResultService.getCompetitionCountOf(this.maxMustermann.startpassnummer());

        assertThat(competitionCount).isGreaterThan(0);
    }

    @Test
    public void whenAthleteDoesntHaveCompetition_thenReturnZeroAsCompetitionCount(){
        Integer zeroCompetitionCount = this.competitionResultService.getCompetitionCountOf(this.maxMustermann.startpassnummer());

        assertThat(zeroCompetitionCount).isEqualTo(0);
    }

    @Test
    public void whenAthleteHasDisciplines_thenReturnCountOfDisciplines(){
        when(this.competitionResultRepository.countDiciplineByStartpassnummer(this.maxMustermann.startpassnummer()))
                .thenReturn(3);

        Integer disciplineCount = this.competitionResultService.getDisciplineCountOf(this.maxMustermann.startpassnummer());

        assertThat(disciplineCount).isGreaterThan(0);
    }

    @Test
    public void whenAthleteHasNoDisciplines_thenReturnZeroAsCountOfDisciplines(){
        Integer zeroDisciplineCount = this.competitionResultService.getDisciplineCountOf(this.maxMustermann.startpassnummer());

        assertThat(zeroDisciplineCount).isEqualTo(0);
    }

    @Test
    public void whenAthleteHasCompetitionYears_ThenReturnListOfCompetitionYears(){
        int competitionYear2022 = 2022;
        int competitionYear2023 = 2023;

        List<Integer> competitionYears = new ArrayList<>();
        competitionYears.add(competitionYear2022);
        competitionYears.add(competitionYear2023);

        when(this.competitionResultRepository
                .findCompetitionYearsByStartpassnummer(this.maxMustermann.startpassnummer()))
                .thenReturn(competitionYears);

        List<Integer> competitionYearsOfAthlete = this.competitionResultService
                .getCompetitionYearsOf(this.maxMustermann.startpassnummer());

        assertThat(competitionYearsOfAthlete.size()).isNotZero();
    }

    @Test
    public void whenAthleteHasNoCompetitionYears_thenReturnEmptyList(){
        List<Integer> emptyCompetitionYearsList = this.competitionResultService
                .getCompetitionYearsOf(this.maxMustermann.startpassnummer());

        assertThat(emptyCompetitionYearsList.size()).isZero();
    }

    @Test
    public void whenAthleteHasCompetitionResultsOfAYear_thenReturnCompetitionResultMapWithEntries(){
        int year = 2004;

        List<DiciplineDTO> athletesDiscipline = new ArrayList<>();
        athletesDiscipline.add(this.hundredMeter);
        athletesDiscipline.add(this.twoHundredMeter);

        List<CompetitionResultDTO> hundredMeterResults = new ArrayList<>();
        hundredMeterResults.add(this.hundredMeterResult);

        List<CompetitionResultDTO> twoHundredMeterResults = new ArrayList<>();
        twoHundredMeterResults.add(this.twoHundredMeterResult);

        when(this.competitionResultRepository
                .findDistinctDiciplineByStartpassnummerOrderByDicipline(this.maxMustermann.startpassnummer()))
                .thenReturn(athletesDiscipline);
        when(this.competitionResultRepository
                .findResultByYear(this.maxMustermann.startpassnummer(), year, athletesDiscipline.get(0).dicipline()))
                .thenReturn(hundredMeterResults);
        when(this.competitionResultRepository
                .findResultByYear(this.maxMustermann.startpassnummer(), year, athletesDiscipline.get(1).dicipline()))
                .thenReturn(twoHundredMeterResults);

        HashMap<String, List<CompetitionResult>> competitionResultsByYear = this.competitionResultService
                .getCompetitionResultsByYearOf(this.maxMustermann.startpassnummer(), year);

        assertThat(competitionResultsByYear.size()).isNotZero();
    }

    @Test
    public void whenAthleteHasNoCompetitionResultsOfAYear_thenReturnEmptyMap(){
        int year = 2004;

        List<DiciplineDTO> athletesDiscipline = new ArrayList<>();
        athletesDiscipline.add(this.hundredMeter);
        athletesDiscipline.add(this.twoHundredMeter);

        when(this.competitionResultRepository
                .findDistinctDiciplineByStartpassnummerOrderByDicipline(this.maxMustermann.startpassnummer()))
                .thenReturn(athletesDiscipline);

        HashMap<String, List<CompetitionResult>> emptyCompetitionResultMap = this.competitionResultService
                .getCompetitionResultsByYearOf(this.maxMustermann.startpassnummer(), year);

        assertThat(emptyCompetitionResultMap.size()).isZero();
    }
}
