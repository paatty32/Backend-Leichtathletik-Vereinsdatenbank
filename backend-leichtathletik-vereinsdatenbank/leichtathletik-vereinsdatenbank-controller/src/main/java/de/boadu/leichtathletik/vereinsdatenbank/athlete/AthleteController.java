package de.boadu.leichtathletik.vereinsdatenbank.athlete;

import de.boadu.leichtathletik.vereinsdatenbank.athlete.dto.AthleteDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/athlete")
public class AthleteController {

    private final AthleteService athleteService;

    public AthleteController(AthleteService athleteService) {
        this.athleteService = athleteService;
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<AthleteDTO>> getAthleteByName(@PathVariable String name){

        List<AthleteDTO> athletesByName = this.athleteService.getAthletesByName(name);

        if(athletesByName.isEmpty()){
            return new ResponseEntity<>(athletesByName, HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(athletesByName);
    }

    @GetMapping("/surname/{surname}")
    public ResponseEntity<List<AthleteDTO>> getAthletesBySurname(@PathVariable String surname){

        List<AthleteDTO> athletesBySurname = this.athleteService.getAthletesBySurname(surname);

        if(athletesBySurname.isEmpty()){
            return new ResponseEntity<>(athletesBySurname, HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(athletesBySurname);
    }


}
