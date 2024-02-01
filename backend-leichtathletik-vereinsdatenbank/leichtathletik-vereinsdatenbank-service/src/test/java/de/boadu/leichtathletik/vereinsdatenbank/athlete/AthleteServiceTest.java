package de.boadu.leichtathletik.vereinsdatenbank.athlete;

import de.boadu.leichtathletik.vereinsdatenbank.athlete.dao.AthleteDAO;
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

        this.maxMustermann = new AthleteDTO() {
            @Override
            public int getStartpassnummer() {
                return 1111;
            }

            @Override
            public String getName() {
                return "Max";
            }

            @Override
            public String getSurname() {
                return "Mustermann";
            }

            @Override
            public int getYearOfBirth() {
                return 1999;
            }
        };

    }

    @Test
    public void whenAthleteWithNameExist_thenReturnAthlete(){

        String max = "max";

        List<AthleteDTO> athletes = new ArrayList<>();
        athletes.add(this.maxMustermann);

        when(this.athleteRepository.findAthleteByNameIgnoreCase(max)).thenReturn(athletes);

        List<AthleteDTO> athleteByName = this.athleteService.getAthleteByName(max);

        assertThat(athleteByName.get(0).getName()).isEqualTo(athletes.get(0).getName());

    }

}
