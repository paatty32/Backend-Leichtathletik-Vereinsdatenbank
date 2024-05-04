package de.boadu.leichtathletik.vereinsdatenbank.athlete;

import de.boadu.leichtathletik.vereinsdatenbank.athlete.dto.AgeGroupDTO;
import de.boadu.leichtathletik.vereinsdatenbank.athlete.dto.AgeGroupLimitsDTO;
import de.boadu.leichtathletik.vereinsdatenbank.athlete.dto.AthleteDTO;
import de.boadu.leichtathletik.vereinsdatenbank.athlete.repository.AgeGroupRepository;
import de.boadu.leichtathletik.vereinsdatenbank.athlete.repository.AthleteRepository;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Service
public class AthleteServiceImpl implements AthleteService {

    private final AthleteRepository athleteRepository;

    private final AgeGroupRepository ageGroupRepository;

    public AthleteServiceImpl(AthleteRepository athleteRepository, AgeGroupRepository ageGroupRepository) {
        this.athleteRepository = athleteRepository;
        this.ageGroupRepository = ageGroupRepository;
    }

    @Override
    public List<Athlete> getAthletesByName(String name) {

        List<Athlete> athletesByName = new ArrayList<>();

        String wildcard = "%";
        String searchedName = name.toLowerCase() + wildcard;

        List<AthleteDTO> foundAthletes = this.athleteRepository.findAthleteByNameIgnoreCase(searchedName);

        if(foundAthletes == null || foundAthletes.isEmpty()){
            return athletesByName;
        }

        for(AthleteDTO foundAthlete: foundAthletes){
            Athlete athlete = this.createAthlete(foundAthlete);
            athletesByName.add(athlete);
        }

        return athletesByName;
    }

    @Override
    public List<Athlete> getAthleteByStartpassnummer(int startpassnummer) {

        String wildcard = "%";

        List<Athlete> athletes = new ArrayList<>();

        String convertedStartpassnummer = String.valueOf(startpassnummer);
        convertedStartpassnummer = convertedStartpassnummer + wildcard;
        List<AthleteDTO> foundAthleteByStartpassnummer = this.athleteRepository.findAthleteByStartpassnummer(convertedStartpassnummer);

        if(foundAthleteByStartpassnummer != null){
            foundAthleteByStartpassnummer.forEach(athlete -> {
                Athlete createdAthlete  = this.createAthlete(athlete);
                athletes.add(createdAthlete);
            });
        }

        return athletes;
    }

    @Override
    public List<Athlete> getAthletesByAgeGroup(String chosenAgeGroup) {

        String ageGroup = chosenAgeGroup;

        List<Athlete> athletesByAgeGroup = new ArrayList<>();

        AgeGroupLimitsDTO ageGroupLimitsByAgeGroup;

        if(ageGroup.equals("Männer") || ageGroup.equals("Frauen")){

            ageGroupLimitsByAgeGroup= this.ageGroupRepository.findAgeGroupLimitsByAgeGroup("Hauptklasse");

        } else {

            ageGroupLimitsByAgeGroup = this.ageGroupRepository.findAgeGroupLimitsByAgeGroup(ageGroup);

        }

        int lowerAgeGroupLimit = ageGroupLimitsByAgeGroup.lowerLimit();
        int upperAgeGroupLimit = ageGroupLimitsByAgeGroup.upperLimit();

        int currentYear = Year.now().getValue();
        int lowerAgeYearLimit = currentYear - lowerAgeGroupLimit;
        int upperAgeYearLimit = currentYear - upperAgeGroupLimit;

        List<AthleteDTO> foundAthletes = getAthletesBetweenYearLimits(ageGroup, lowerAgeYearLimit, upperAgeYearLimit);

        if(foundAthletes == null || foundAthletes.isEmpty()){

            return athletesByAgeGroup;
        }

        for(AthleteDTO foundAthlete: foundAthletes){

            Athlete athlete = this.createAthleteWithAgeGroup(foundAthlete, ageGroup);

            athletesByAgeGroup.add(athlete);

        }

        return athletesByAgeGroup;
    }

    private Athlete createAthleteWithAgeGroup(AthleteDTO athlete, String ageGroup) {

        return new Athlete(athlete.startpassnummer(),
                athlete.name(),
                athlete.surname(),
                ageGroup);
    }

    private List<AthleteDTO> getAthletesBetweenYearLimits(String ageGroup, int lowerAgeYearLimit, int upperAgeYearLimit) {

        List<AthleteDTO> foundAthletes;

        if(ageGroup.equals("Männer")) {

            foundAthletes = this.athleteRepository.findMenAthletesByAgeBetween(lowerAgeYearLimit, upperAgeYearLimit);

        } else if(ageGroup.equals("Frauen")){

            foundAthletes = this.athleteRepository.findWomanAthletesByAgeBetween(lowerAgeYearLimit, upperAgeYearLimit);

        } else {

            foundAthletes = this.athleteRepository.findAthletesByAgeBetween(lowerAgeYearLimit, upperAgeYearLimit);

        }
        return foundAthletes;
    }

    private Athlete createAthlete(AthleteDTO athlete) {

        String athleteAgeGroup = this.getAgeGroup(athlete);

        return new Athlete(athlete.startpassnummer(),
                athlete.name(),
                athlete.surname(),
                athleteAgeGroup);
    }

    private String getAgeGroup(AthleteDTO athlete) {

        Year currentYear = Year.now();
        int currentYearValue = currentYear.getValue();
        int yearOfBirth = athlete.yearOfBirth();
        int actualAge = currentYearValue - yearOfBirth;

        AgeGroupDTO ageGroup = this.ageGroupRepository.findAgeGroup(actualAge);
        String athleteAgeGroup = ageGroup.ageGroup();

        if(isMan(athlete, athleteAgeGroup)){

            athleteAgeGroup = "Männer";

        } else if(isWoman(athlete, athleteAgeGroup)){

            athleteAgeGroup = "Frauen";

        }
        return athleteAgeGroup;
    }

    private boolean isMan(AthleteDTO athlete, String athleteAgeGroup) {
        return athleteAgeGroup.equals("Hauptklasse") && athlete.gender().equals("M");
    }

    private boolean isWoman(AthleteDTO athlete, String athleteAgeGroup) {
        return athleteAgeGroup.equals("Hauptklasse") && athlete.gender().equals("W");
    }

}
