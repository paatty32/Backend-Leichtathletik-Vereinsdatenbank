package de.boadu.leichtathletik.vereinsdatenbank.competitionresult;

import de.boadu.leichtathletik.vereinsdatenbank.competitionresult.dto.PersonalBest;
import de.boadu.leichtathletik.vereinsdatenbank.competitionresult.dto.PersonalBestDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin( origins = "http://localhost:4200")
@RequestMapping("api/v1/competition")
public class CompetitionResultController {

    private final CompetitionResultService competitionResultService;

    public CompetitionResultController(CompetitionResultService competitionResultService) {
        this.competitionResultService = competitionResultService;
    }

    @GetMapping("/personalbest")
    public ResponseEntity<List<PersonalBest>> getPersonalBestByStartpassnummer(@RequestParam int startpassnummer){

        List<PersonalBest> personalBests = this.competitionResultService.getPersonalBestsOf(startpassnummer);

        if(personalBests.isEmpty()){
            return new ResponseEntity<>(personalBests, HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(personalBests);

    }
}
