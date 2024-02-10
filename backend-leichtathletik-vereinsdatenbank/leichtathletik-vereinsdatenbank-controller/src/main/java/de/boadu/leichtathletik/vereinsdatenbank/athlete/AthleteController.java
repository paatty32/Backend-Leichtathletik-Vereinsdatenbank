package de.boadu.leichtathletik.vereinsdatenbank.athlete;

import de.boadu.leichtathletik.vereinsdatenbank.athlete.dto.AthleteDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin( origins = "http://localhost:4200")
@RequestMapping("api/v1/athlete")
public class AthleteController {

    private final AthleteService athleteService;

    public AthleteController(AthleteService athleteService) {
        this.athleteService = athleteService;
    }

    @GetMapping("/name")
    public ResponseEntity<List<AthleteRecord>> getAthleteByName(@RequestParam String name){

        System.out.println("Received request for name: " + name);

        List<AthleteRecord> athletesByName = this.athleteService.getAthletesByName(name);

        if(athletesByName.isEmpty()){
            System.out.println("No athletes found for name: " + name);
            return new ResponseEntity<>(athletesByName, HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(athletesByName);
    }

    @GetMapping("/surname")
    public ResponseEntity<List<AthleteDTO>> getAthletesBySurname(@RequestParam String surname){

        List<AthleteDTO> athletesBySurname = this.athleteService.getAthletesBySurname(surname);

        if(athletesBySurname.isEmpty()){
            return new ResponseEntity<>(athletesBySurname, HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(athletesBySurname);
    }

    @GetMapping("/startpassnummer")
    public ResponseEntity<AthleteDTO> getAthelteByStartpassnummer(@RequestParam int startpassnummer){

        AthleteDTO athleteByStartpassnummer = this.athleteService.getAthleteByStartpassnummer(startpassnummer);

        if(athleteByStartpassnummer.startpassnummer() == 0){

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }

        return ResponseEntity.ok(athleteByStartpassnummer);

    }


}
