package de.boadu.leichtathletik.vereinsdatenbank.athlete;

import de.boadu.leichtathletik.vereinsdatenbank.athlete.dto.AthleteDTO;
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

    @InjectMocks
    private AthleteServiceImpl athleteService;

    private AthleteDTO maxMustermann;


    @BeforeEach
    public void setUp(){
        this.maxMustermann = new AthleteDTO(1111, "Max", "Mustermann", 1999);
    }

    @Test
    public void whenAthleteWithNameExist_thenReturnAthlete(){

        String max = "Max";

        List<AthleteDTO> athletes = new ArrayList<>();
        athletes.add(this.maxMustermann);

        when(this.athleteRepository.findAthleteByNameIgnoreCase(max)).thenReturn(athletes);

        List<AthleteDTO> athleteByName = this.athleteService.getAthletesByName(max);
        boolean hasName = athleteByName.stream().allMatch(athlete -> athlete.name().equals(max));

        assertThat(hasName).isTrue();

    }

    @Test
    public void whenAthletesNotExist_thenReturnEmptyList(){
        String max = "Max";

        when(this.athleteRepository.findAthleteByNameIgnoreCase(max)).thenReturn(null);

        List<AthleteDTO> athleteByName = this.athleteService.getAthletesByName(max);

        assertThat(athleteByName.size()).isEqualTo(0);

    }

    @Test
    public void whenAthletesWithSurnameExist_thenReturnAthletes(){

        String mustermann = "Mustermann";

        List<AthleteDTO> athletesBySurname = new ArrayList<>();
        athletesBySurname.add(this.maxMustermann);

        when(this.athleteRepository.findAthleteBySurnameIgnoreCase(mustermann)).thenReturn(athletesBySurname);

        List<AthleteDTO> foundAthletesBySurname = this.athleteService.getAthletesBySurname(mustermann);
        boolean hasSurname = foundAthletesBySurname.stream().allMatch(athlete -> athlete.surname().equals(mustermann));

        assertThat(hasSurname).isEqualTo(true);
    }

    @Test
    public void whenAthleteWithSurnameNotExist_thenReturnEmptyList(){

        String mustermann = "Mustermann";

        when(this.athleteRepository.findAthleteBySurnameIgnoreCase(mustermann)).thenReturn(null);

        List<AthleteDTO> athleteByName = this.athleteService.getAthletesBySurname(mustermann);

        assertThat(athleteByName.size()).isEqualTo(0);

    }

    @Test
    public void whenAthleteWithStartpassnumemrExist_thenReturnAthlete(){

        int startpassnummer = 1111;

        when(this.athleteRepository.findAthleteByStartpassnummer(startpassnummer)).thenReturn(this.maxMustermann);

        AthleteDTO athleteByStartpassnummer = this.athleteService.getAthleteByStartpassnummer(startpassnummer);

        assertThat(athleteByStartpassnummer).isEqualTo(this.maxMustermann);

    }

    @Test
    public void whenAthleteWithStartpassnummerNotExist_thenReturnAthleteWithStarpassnummerZero(){

        int startpassnummer = 1111;

        when(this.athleteRepository.findAthleteByStartpassnummer(startpassnummer)).thenReturn(new AthleteDTO(0,null,null,0));

        AthleteDTO athleteNotFound = this.athleteRepository.findAthleteByStartpassnummer(startpassnummer);

        assertThat(athleteNotFound.startpassnummer()).isEqualTo(0);

    }

}
