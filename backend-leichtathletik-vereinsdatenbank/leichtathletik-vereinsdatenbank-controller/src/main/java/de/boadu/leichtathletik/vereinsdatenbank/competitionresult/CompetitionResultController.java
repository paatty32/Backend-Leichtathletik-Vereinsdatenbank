package de.boadu.leichtathletik.vereinsdatenbank.competitionresult;

import de.boadu.leichtathletik.vereinsdatenbank.competitionresult.dto.PersonalBest;
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

    @GetMapping("/seasonbest")
    public ResponseEntity<List<PersonalBest>> getSeasonBestByStartpassnummeranYear(@RequestParam int startpassnummer,
                                                                                   @RequestParam int year){

        List<PersonalBest> seasonalBestsByYear = this.competitionResultService.getSeasonalBestOf(year, startpassnummer);

        if(seasonalBestsByYear.isEmpty()){
            return new ResponseEntity<>(seasonalBestsByYear, HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(seasonalBestsByYear);
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> getCompetitionCountByStartpassnummer(@RequestParam int startpassnummer){

        Integer competitionCount = this.competitionResultService.getCompetitionCountOf(startpassnummer);

        return ResponseEntity.ok(competitionCount);
    }

    @GetMapping("/countdiscipline")
    public ResponseEntity<Integer> getDisciplineCountByStartpassnummer(@RequestParam int startpassnummer){

        Integer disciplineCount = this.competitionResultService.getDisciplineCountOf(startpassnummer);

        if(disciplineCount == 0){
            return new ResponseEntity<>(disciplineCount, HttpStatus.NOT_FOUND);

        }

        return ResponseEntity.ok(disciplineCount);

    }

    @GetMapping("/competitionyears")
    public ResponseEntity<List<Integer>> getCompetitionYearsByStartpassnummer(@RequestParam int startpassnummer){

        List<Integer> competitionYears = this.competitionResultService.getCompetitionYearsOf(startpassnummer);

        if(competitionYears.isEmpty()){
            return new ResponseEntity<>(competitionYears, HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(competitionYears);

    }
}
