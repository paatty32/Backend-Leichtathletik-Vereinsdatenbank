package de.boadu.leichtathletik.vereinsdatenbank.athlete.repository;

import de.boadu.leichtathletik.vereinsdatenbank.athlete.dao.AgeGroupDAO;
import de.boadu.leichtathletik.vereinsdatenbank.athlete.dto.AgeGroupDTO;
import de.boadu.leichtathletik.vereinsdatenbank.athlete.dto.AgeGroupLimitsDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AgeGroupRepository extends JpaRepository<AgeGroupDAO, Integer> {

    @Query("""
    SELECT new de.boadu.leichtathletik.vereinsdatenbank.athlete.dto.AgeGroupDTO(a.ageGroup) 
    FROM AgeGroupDAO a 
    WHERE :age >= a.lowerLimit 
    AND :age <= a.upperLimit
    """ )
    AgeGroupDTO findAgeGroup(@Param("age") int age);

    AgeGroupLimitsDTO findAgeGroupLimitsByAgeGroup(String ageGroup);

}
