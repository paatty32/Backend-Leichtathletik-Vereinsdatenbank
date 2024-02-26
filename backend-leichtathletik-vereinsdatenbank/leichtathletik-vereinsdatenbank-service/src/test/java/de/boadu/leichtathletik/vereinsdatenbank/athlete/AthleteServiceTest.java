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

        String max = "Max";

        AgeGroupDTO hauptklasse = new AgeGroupDTO("Hauptklasse");

        List<AthleteDTO> athletes = new ArrayList<>();
        athletes.add(this.maxMustermann);

        when(this.athleteRepository.findAthleteByNameIgnoreCase(max)).thenReturn(athletes);
        when(this.ageGroupRepository.findAgeGroup(25)).thenReturn(hauptklasse);

        List<Athlete> athleteByName = this.athleteService.getAthletesByName(max);
        boolean hasName = athleteByName.stream().allMatch(athlete -> athlete.name().equals(max));

        assertThat(hasName).isTrue();

    }

    @Test
    public void whenWomanAthleteWithNameExist_thenReturnAthleteProfiles(){

        String laura = "Laura";

        AgeGroupDTO hauptklasse = new AgeGroupDTO("Hauptklasse");

        List<AthleteDTO> athletes = new ArrayList<>();
        athletes.add(this.lauraMustermann);

        Year mockYearof2024 = Year.of(2024);

        try (MockedStatic<Year> yearMockedStatic = Mockito.mockStatic(Year.class)) {
            yearMockedStatic.when(Year::now).thenReturn(mockYearof2024);

            int actualAge = mockYearof2024.getValue() - lauraMustermann.yearOfBirth();

            when(this.athleteRepository.findAthleteByNameIgnoreCase(laura)).thenReturn(athletes);
            when(this.ageGroupRepository.findAgeGroup(actualAge)).thenReturn(hauptklasse);

            List<Athlete> athleteByName = this.athleteService.getAthletesByName(laura);
            System.out.println(athleteByName);
            boolean hasName = athleteByName.stream().allMatch(athlete -> athlete.name().equals(laura));

            assertThat(hasName).isTrue();
        }

    }

    @Test
    public void whenAthletesNotExist_thenReturnEmptyList(){
        String max = "Max";

        when(this.athleteRepository.findAthleteByNameIgnoreCase(max)).thenReturn(null);

        List<Athlete> athleteByName = this.athleteService.getAthletesByName(max);

        assertThat(athleteByName.size()).isEqualTo(0);

    }

    @Test
    public void whenAthletesWithSurnameExist_thenReturnAthleteProfiles(){

        String mustermann = "Mustermann";

        AgeGroupDTO hauptklasse = new AgeGroupDTO("Hauptklasse");

        List<AthleteDTO> athletesBySurname = new ArrayList<>();
        athletesBySurname.add(this.maxMustermann);

        Year mockYearof2024 = Year.of(2024);

        try (MockedStatic<Year> yearMockedStatic = Mockito.mockStatic(Year.class)) {
            yearMockedStatic.when(Year::now).thenReturn(mockYearof2024);

            int actualAge = mockYearof2024.getValue() - this.maxMustermann.yearOfBirth();

            when(this.athleteRepository.findAthleteBySurnameIgnoreCase(mustermann)).thenReturn(athletesBySurname);
            when(this.ageGroupRepository.findAgeGroup(actualAge)).thenReturn(hauptklasse);

            List<Athlete> foundAthletesBySurname = this.athleteService.getAthletesBySurname(mustermann);
            boolean hasSurname = foundAthletesBySurname.stream().allMatch(athlete -> athlete.surname().equals(mustermann));

            assertThat(hasSurname).isEqualTo(true);

        }
    }

    @Test
    public void whenAthleteWithSurnameNotExist_thenReturnEmptyList(){

        String mustermann = "Mustermann";

        when(this.athleteRepository.findAthleteBySurnameIgnoreCase(mustermann)).thenReturn(null);

        List<Athlete> athleteByName = this.athleteService.getAthletesBySurname(mustermann);

        assertThat(athleteByName.size()).isEqualTo(0);

    }

    @Test
    public void whenAthleteWithStartpassnumemrExist_thenReturnAthleteProfile(){

        int startpassnummer = 1111;
        AgeGroupDTO hauptklasse = new AgeGroupDTO("Hauptklasse");

        when(this.athleteRepository.findAthleteByStartpassnummer(startpassnummer)).thenReturn(this.maxMustermann);
        when(this.ageGroupRepository.findAgeGroup(25)).thenReturn(hauptklasse);

        Athlete athleteByStartpassnummer = this.athleteService.getAthleteByStartpassnummer(startpassnummer);

        assertThat(athleteByStartpassnummer.startpassnummer()).isEqualTo(this.maxMustermann.startpassnummer());
        assertThat(athleteByStartpassnummer.name()).isEqualTo(this.maxMustermann.name());
        assertThat(athleteByStartpassnummer.surname()).isEqualTo(this.maxMustermann.surname());
        assertThat(athleteByStartpassnummer.ageGroup()).isEqualTo("Männer");

    }

    @Test
    public void whenAthleteWithStartpassnummerNotExist_thenReturnAthleteProfileWithStarpassnummerZero(){

        int startpassnummer = 1111;

        when(this.athleteRepository.findAthleteByStartpassnummer(startpassnummer)).thenReturn(null);

        Athlete athleteNotFound = this.athleteService.getAthleteByStartpassnummer(startpassnummer);

        assertThat(athleteNotFound.startpassnummer()).isEqualTo(0);

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
