package de.boadu.leichtathletik.vereinsdatenbank.competitionresult;

import de.boadu.leichtathletik.vereinsdatenbank.competitionresult.dto.CompetitionResult;
import de.boadu.leichtathletik.vereinsdatenbank.competitionresult.dto.PersonalBest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin
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

        List<PersonalBest> seasonalBestsByYear = this.competitionResultService.getSeasonalBestOf(startpassnummer, year);

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

    @GetMapping("/results")
    public ResponseEntity<HashMap<String, List<CompetitionResult>>> getCompetitionResultsOfYearByStartpassnummer(@RequestParam int startpassnummer,
                                                                                                                 @RequestParam int year){

        HashMap<String, List<CompetitionResult>> competitionResults = this.competitionResultService.getCompetitionResultsByYearOf(startpassnummer, year);

        if(competitionResults.isEmpty()){
            return new ResponseEntity<>(competitionResults, HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(competitionResults);

    }
}
