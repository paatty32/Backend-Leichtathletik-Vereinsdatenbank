package de.boadu.leichtathletik.vereinsdatenbank.athlete;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("api/v1/athlete")
public class AthleteController {

    private final AthleteService athleteService;

    public AthleteController(AthleteService athleteService) {
        this.athleteService = athleteService;
    }

    @GetMapping("/name")
    public ResponseEntity<List<Athlete>> getAthleteByName(@RequestParam String name){
        List<Athlete> athletesByName = this.athleteService.getAthletesByName(name);

        if(athletesByName.isEmpty()){
            return new ResponseEntity<>(athletesByName, HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(athletesByName);
    }

    @GetMapping("/startpassnummer")
    public ResponseEntity<List<Athlete>> getAthelteByStartpassnummer(@RequestParam int startpassnummer){
        List<Athlete> athleteByStartpassnummer = this.athleteService.getAthleteByStartpassnummer(startpassnummer);

        if(athleteByStartpassnummer.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(athleteByStartpassnummer);
    }

    @GetMapping("/age-group")
    public ResponseEntity<List<Athlete>> getAthletesByAgeGroup(@RequestParam String ageGroup){
        List<Athlete> athletesByAgeGroup = this.athleteService.getAthletesByAgeGroup(ageGroup);

        if(athletesByAgeGroup.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(athletesByAgeGroup);
    }
}
