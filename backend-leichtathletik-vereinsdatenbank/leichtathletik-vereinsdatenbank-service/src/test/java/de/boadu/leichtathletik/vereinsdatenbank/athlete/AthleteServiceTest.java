package de.boadu.leichtathletik.vereinsdatenbank.athlete;

import de.boadu.leichtathletik.vereinsdatenbank.athlete.dto.AgeGroupDTO;
import de.boadu.leichtathletik.vereinsdatenbank.athlete.dto.AgeGroupLimitsDTO;
import de.boadu.leichtathletik.vereinsdatenbank.athlete.dto.AthleteDTO;
import de.boadu.leichtathletik.vereinsdatenbank.athlete.repository.AgeGroupRepository;
import de.boadu.leichtathletik.vereinsdatenbank.athlete.repository.AthleteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AthleteServiceTest {

    @Mock
    private AthleteRepository athleteRepository;

    @Mock
    private AgeGroupRepository ageGroupRepository;

    @InjectMocks
    private AthleteServiceImpl athleteService;

    private AthleteDTO maxMustermann;
    private AthleteDTO jensMustermann;
    private AthleteDTO lauraMustermann;
    private AthleteDTO laraMustermann;
    private AthleteDTO svenMustermann;
    private AthleteDTO rudolfMustermann;

    @BeforeEach
    public void setUp(){

        this.maxMustermann = new AthleteDTO(1111, "Max", "Mustermann", 1999, "M");
        this.jensMustermann = new AthleteDTO(1112, "Jens", "Mustermann", 1995, "M");
        this.lauraMustermann = new AthleteDTO(1113, "Laura","Mustermann", 1996, "W");
        this.laraMustermann = new AthleteDTO(1234, "Lara", "Mustermann", 1997, "W");
        this.svenMustermann = new AthleteDTO(1114, "Sven", "Mustermann", 2002, "M");
        this.rudolfMustermann = new AthleteDTO(2222, "Rudolf", "Mustermann", 2003, "M");

    }

    @Test
    public void whenMenAthleteWithNameExist_thenReturnAthleteProfiles(){

        String max = "max";

        AgeGroupDTO hauptklasse = new AgeGroupDTO("Hauptklasse");

        List<AthleteDTO> athletes = new ArrayList<>();
        athletes.add(this.maxMustermann);

        when(this.athleteRepository.findAthleteByNameIgnoreCase(max + "%")).thenReturn(athletes);
        when(this.ageGroupRepository.findAgeGroup(25)).thenReturn(hauptklasse);

        List<Athlete> athleteByName = this.athleteService.getAthletesByName(max);
        boolean hasName = athleteByName.stream().allMatch(athlete -> athlete.name().equals("Max"));

        assertThat(hasName).isTrue();

    }

    @Test
    public void whenWomanAthleteWithNameExist_thenReturnAthleteProfiles(){

        String laura = "laura";

        AgeGroupDTO hauptklasse = new AgeGroupDTO("Hauptklasse");

        List<AthleteDTO> athletes = new ArrayList<>();
        athletes.add(this.lauraMustermann);

        Year mockYearof2024 = Year.of(2024);

        try (MockedStatic<Year> yearMockedStatic = Mockito.mockStatic(Year.class)) {
            yearMockedStatic.when(Year::now).thenReturn(mockYearof2024);

            int actualAge = mockYearof2024.getValue() - lauraMustermann.yearOfBirth();

            when(this.athleteRepository.findAthleteByNameIgnoreCase(laura + "%")).thenReturn(athletes);
            when(this.ageGroupRepository.findAgeGroup(actualAge)).thenReturn(hauptklasse);

            List<Athlete> athleteByName = this.athleteService.getAthletesByName(laura);
            System.out.println(athleteByName);
            boolean hasName = athleteByName.stream().allMatch(athlete -> athlete.name().equals("Laura"));

            assertThat(hasName).isTrue();
        }

    }

    @Test
    public void whenAthletesNotExist_thenReturnEmptyList(){
        String max = "max";

        when(this.athleteRepository.findAthleteByNameIgnoreCase(max + "%")).thenReturn(null);

        List<Athlete> athleteByName = this.athleteService.getAthletesByName(max);

        assertThat(athleteByName.size()).isEqualTo(0);
    }

    @Test
    public void whenAthleteWithStartpassnumemrExist_thenReturnAthleteProfile(){

        int startpassnummer = 1111;
        String startpassnummerString = String.valueOf(startpassnummer);
        AgeGroupDTO hauptklasse = new AgeGroupDTO("Hauptklasse");

        List<AthleteDTO> athleteList = new ArrayList<>();
        athleteList.add(this.maxMustermann);

        when(this.athleteRepository.findAthleteByStartpassnummer(startpassnummerString + "%")).thenReturn(athleteList);
        when(this.ageGroupRepository.findAgeGroup(25)).thenReturn(hauptklasse);

        List<Athlete> athleteByStartpassnummer = this.athleteService.getAthleteByStartpassnummer(startpassnummer);

        assertThat(athleteByStartpassnummer.get(0).startpassnummer()).isEqualTo(this.maxMustermann.startpassnummer());
        assertThat(athleteByStartpassnummer.get(0).name()).isEqualTo(this.maxMustermann.name());
        assertThat(athleteByStartpassnummer.get(0).surname()).isEqualTo(this.maxMustermann.surname());
        assertThat(athleteByStartpassnummer.get(0).ageGroup()).isEqualTo("Männer");

    }

    @Test
    public void whenAthleteWithStartpassnummerNotExist_thenReturnAthleteProfileWithStarpassnummerZero(){

        int startpassnummer = 1111;
        String startpassnummerString = String.valueOf(startpassnummer);

        List<AthleteDTO> emptyAthleteList = new ArrayList<>();

        when(this.athleteRepository.findAthleteByStartpassnummer(startpassnummerString + "%")).thenReturn(emptyAthleteList);

        List<Athlete> athleteNotFound = this.athleteService.getAthleteByStartpassnummer(startpassnummer);

        assertThat(athleteNotFound.isEmpty());
    }

    @Test
    public void whenAthletesWithAgeGroupMännerExist_thenReturnAthleteProfils() {

        List<AthleteDTO> menAthletes = new ArrayList<>();
        menAthletes.add(this.maxMustermann);
        menAthletes.add(this.jensMustermann);

        AgeGroupLimitsDTO ageGroupLimits = new AgeGroupLimitsDTO(23, 29);
        int upperYearLimit = ageGroupLimits.upperLimit();
        int lowerYearLimit = ageGroupLimits.lowerLimit();

        Year mockYearof2024 = Year.of(2024);

        try (MockedStatic<Year> yearMockedStatic = Mockito.mockStatic(Year.class)) {
            yearMockedStatic.when(Year::now).thenReturn(mockYearof2024);

            int lowerAgeLimit = mockYearof2024.getValue() - lowerYearLimit;
            int uperrAgeLimit = mockYearof2024.getValue() - upperYearLimit;

            when(this.ageGroupRepository.findAgeGroupLimitsByAgeGroup("Hauptklasse")).thenReturn(ageGroupLimits);
            when(this.athleteRepository.findMenAthletesByAgeBetween(lowerAgeLimit, uperrAgeLimit)).thenReturn(menAthletes);

            List<Athlete> foundAthletes = this.athleteService.getAthletesByAgeGroup("Männer");

            boolean isMänner = foundAthletes.stream().allMatch(athlete -> athlete.ageGroup().equals("Männer"));

            assertThat(isMänner).isTrue();
        }

    }

    @Test
    public void whenAthletesWithAgeGroupFrauenExist_thenReturnAthleteProfiles() {

        List<AthleteDTO> womanAthletes = new ArrayList<>();
        womanAthletes.add(this.laraMustermann);
        womanAthletes.add(this.lauraMustermann);

        AgeGroupLimitsDTO ageGroupLimits = new AgeGroupLimitsDTO(23, 29);
        int upperYearLimit = ageGroupLimits.upperLimit();
        int lowerYearLimit = ageGroupLimits.lowerLimit();

        Year mockYearof2024 = Year.of(2024);

        try (MockedStatic<Year> yearMockedStatic = Mockito.mockStatic(Year.class)) {
            yearMockedStatic.when(Year::now).thenReturn(mockYearof2024);

            int lowerAgeLimit = mockYearof2024.getValue() - lowerYearLimit;
            int uperrAgeLimit = mockYearof2024.getValue() - upperYearLimit;

            when(this.ageGroupRepository.findAgeGroupLimitsByAgeGroup("Hauptklasse")).thenReturn(ageGroupLimits);
            when(this.athleteRepository.findWomanAthletesByAgeBetween(lowerAgeLimit, uperrAgeLimit)).thenReturn(womanAthletes);

            List<Athlete> foundAthletes = this.athleteService.getAthletesByAgeGroup("Frauen");

            boolean isFrauen = foundAthletes.stream().allMatch(athlete -> athlete.ageGroup().equals("Frauen"));

            assertThat(isFrauen).isTrue();
        }

    }

    @Test
    public void whenAthletesWithAgeGroupExist_thenReturnAthleteProfiles() {

        List<AthleteDTO> athletes = new ArrayList<>();
        athletes.add(this.rudolfMustermann);
        athletes.add(this.svenMustermann);

        AgeGroupLimitsDTO ageGroupU23Limits = new AgeGroupLimitsDTO(20, 22);
        int upperYearLimit = ageGroupU23Limits.upperLimit();
        int lowerYearLimit = ageGroupU23Limits.lowerLimit();

        Year mockYearof2024 = Year.of(2024);

        try (MockedStatic<Year> yearMockedStatic = Mockito.mockStatic(Year.class)) {
            yearMockedStatic.when(Year::now).thenReturn(mockYearof2024);

            int lowerAgeLimit = mockYearof2024.getValue() - lowerYearLimit;
            int uperrAgeLimit = mockYearof2024.getValue() - upperYearLimit;

            when(this.ageGroupRepository.findAgeGroupLimitsByAgeGroup("U23")).thenReturn(ageGroupU23Limits);
            when(this.athleteRepository.findAthletesByAgeBetween(lowerAgeLimit, uperrAgeLimit)).thenReturn(athletes);

            List<Athlete> foundAthletes = this.athleteService.getAthletesByAgeGroup("U23");

            assertThat(foundAthletes.size()).isEqualTo(2);
        }

    }

    @Test
    public void whenAthletesWithAgeGroupDontExist_thenReturnEmptyList() {

        List<AthleteDTO> athletes = new ArrayList<>();

        AgeGroupLimitsDTO ageGroupU23Limits = new AgeGroupLimitsDTO(20, 22);
        int upperYearLimit = ageGroupU23Limits.upperLimit();
        int lowerYearLimit = ageGroupU23Limits.lowerLimit();

        Year mockYearof2024 = Year.of(2024);

        try (MockedStatic<Year> yearMockedStatic = Mockito.mockStatic(Year.class)) {
            yearMockedStatic.when(Year::now).thenReturn(mockYearof2024);

            int lowerAgeLimit = mockYearof2024.getValue() - lowerYearLimit;
            int uperrAgeLimit = mockYearof2024.getValue() - upperYearLimit;

            when(this.ageGroupRepository.findAgeGroupLimitsByAgeGroup("U23")).thenReturn(ageGroupU23Limits);
            when(this.athleteRepository.findAthletesByAgeBetween(lowerAgeLimit, uperrAgeLimit)).thenReturn(athletes);

            List<Athlete> foundAthletes = this.athleteService.getAthletesByAgeGroup("U23");

            assertThat(foundAthletes.size()).isEqualTo(0);
        }

    }

}
