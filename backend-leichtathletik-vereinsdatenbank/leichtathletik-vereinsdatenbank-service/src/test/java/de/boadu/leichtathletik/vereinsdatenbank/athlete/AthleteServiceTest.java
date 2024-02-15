package de.boadu.leichtathletik.vereinsdatenbank.athlete;

import de.boadu.leichtathletik.vereinsdatenbank.athlete.dto.AgeGroupDTO;
import de.boadu.leichtathletik.vereinsdatenbank.athlete.dto.AthleteDTO;
import de.boadu.leichtathletik.vereinsdatenbank.athlete.repository.AgeGroupRepository;
import de.boadu.leichtathletik.vereinsdatenbank.athlete.repository.AthleteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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


    @BeforeEach
    public void setUp(){
        this.maxMustermann = new AthleteDTO(1111, "Max", "Mustermann", 1999, "M");
    }

    @Test
    public void whenAthleteWithNameExist_thenReturnAthleteProfiles(){

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

        when(this.athleteRepository.findAthleteBySurnameIgnoreCase(mustermann)).thenReturn(athletesBySurname);
        when(this.ageGroupRepository.findAgeGroup(25)).thenReturn(hauptklasse);

        List<Athlete> foundAthletesBySurname = this.athleteService.getAthletesBySurname(mustermann);
        boolean hasSurname = foundAthletesBySurname.stream().allMatch(athlete -> athlete.surname().equals(mustermann));

        assertThat(hasSurname).isEqualTo(true);
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

        when(this.athleteRepository.findAthleteByStartpassnummer(startpassnummer)).thenReturn(new AthleteDTO(0,null,null,0, "M"));

        AthleteDTO athleteNotFound = this.athleteRepository.findAthleteByStartpassnummer(startpassnummer);

        assertThat(athleteNotFound.startpassnummer()).isEqualTo(0);

    }

}
