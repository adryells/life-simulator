package com.lifesimulator.lifesimulator.services;

import com.lifesimulator.lifesimulator.models.Person;
import com.lifesimulator.lifesimulator.models.Player;
import com.lifesimulator.lifesimulator.models.Relationship;
import com.lifesimulator.lifesimulator.models.RelationshipType;
import com.lifesimulator.lifesimulator.repositories.PersonRepository;
import com.lifesimulator.lifesimulator.repositories.RelationshipRepository;
import com.lifesimulator.lifesimulator.repositories.RelationshipTypeRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class FamilyService {
    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private RelationshipRepository relationshipRepository;

    @Autowired
    private RelationshipTypeRepository relationshipTypeRepository;

    private void ensureRelationshipTypesExist() {
        if (relationshipTypeRepository.findByName("Parent") == null) {
            relationshipTypeRepository.save(new RelationshipType("Parent"));
        }
        if (relationshipTypeRepository.findByName("Spouse") == null) {
            relationshipTypeRepository.save(new RelationshipType("Spouse"));
        }
    }


    public void generateFamily(Player player) {
        Faker faker = new Faker();

        int motherAge = ThreadLocalRandom.current().nextInt(18, 51);
        LocalDate motherBirth = subtractYearsRandomized(player.getBirth(), motherAge);

        int fatherAge = ThreadLocalRandom.current().nextInt(18, 71);
        LocalDate fatherBirth = subtractYearsRandomized(player.getBirth(), fatherAge);

        Person mother = new Person(faker.name().fullName(), motherBirth, player.getCountry(), "Female");
        Person father = new Person(faker.name().fullName(), fatherBirth, player.getCountry(), "Male");

        System.out.println("Mother: " + mother.getName() + " born in " + motherBirth);
        mother.generateInitialStats();
        System.out.println("Father: " + father.getName() + " born in " + fatherBirth);
        father.generateInitialStats();

        ensureRelationshipTypesExist();

        RelationshipType parentType = relationshipTypeRepository.findByName("Parent");
        RelationshipType spouseType = relationshipTypeRepository.findByName("Spouse");

        Relationship motherToChild = new Relationship(mother, player, parentType);
        Relationship fatherToChild = new Relationship(father, player, parentType);
        Relationship parentsRelationship = new Relationship(father, mother, spouseType);

        personRepository.save(mother);
        personRepository.save(father);
        relationshipRepository.save(motherToChild);
        relationshipRepository.save(fatherToChild);
        relationshipRepository.save(parentsRelationship);
    }

    private LocalDate subtractYearsRandomized(LocalDate date, int years) {
        LocalDate newDate = date.minus(years, ChronoUnit.YEARS);
        int randomMonth = ThreadLocalRandom.current().nextInt(1, 13);
        int randomDay = ThreadLocalRandom.current().nextInt(1, newDate.withMonth(randomMonth).lengthOfMonth() + 1);
        return LocalDate.of(newDate.getYear(), randomMonth, randomDay);
    }

}
