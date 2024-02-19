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

        List<AthleteDTO> foundAthletes = this.athleteRepository.findAthleteByNameIgnoreCase(name);

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
    public List<Athlete> getAthletesBySurname(String surname) {

        List<Athlete> athletesBySurname = new ArrayList<>();

        List<AthleteDTO> foundAthletesBySurname = this.athleteRepository.findAthleteBySurnameIgnoreCase(surname);

        if(foundAthletesBySurname == null || foundAthletesBySurname.isEmpty()){

            return athletesBySurname;
        }

        for(AthleteDTO athleteBySurname: foundAthletesBySurname){

            Athlete athlete = this.createAthlete(athleteBySurname);

            athletesBySurname.add(athlete);

        }

        return athletesBySurname;
    }

    @Override
    public Athlete getAthleteByStartpassnummer(int startpassnummer) {

        AthleteDTO foundAthleteByStartpassnummer = this.athleteRepository.findAthleteByStartpassnummer(startpassnummer);

        if(foundAthleteByStartpassnummer == null){

            Athlete athleteNotFound = new Athlete(0, null, null,"");

            return athleteNotFound;
        }

        return this.createAthlete(foundAthleteByStartpassnummer);
    }

    @Override
    public List<Athlete> getAthletesByAgeGroup(String ageGroup) {

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

        for(AthleteDTO athleteByAgeGroup: foundAthletes){

            Athlete athlete = this.createAthlete(athleteByAgeGroup);

            athletesByAgeGroup.add(athlete);

        }

        return athletesByAgeGroup;
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
