package de.boadu.leichtathletik.vereinsdatenbank.competitionresult;

import de.boadu.leichtathletik.vereinsdatenbank.competitionresult.dto.DiciplineDTO;
import de.boadu.leichtathletik.vereinsdatenbank.competitionresult.repository.CompetitionResultRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompetitionResultServiceImpl implements CompetitionResultService{

    private final CompetitionResultRepository competitionResultRepository;

    public CompetitionResultServiceImpl(CompetitionResultRepository competitionResultRepository) {
        this.competitionResultRepository = competitionResultRepository;
    }

    private List<DiciplineDTO> getDisciplinesBy(int startpassnummer){

        List<DiciplineDTO> athleteDisciplines = this.competitionResultRepository
                                                    .findDistinctDiciplineByStartpassnummer(startpassnummer);

        return athleteDisciplines;
    }
}
