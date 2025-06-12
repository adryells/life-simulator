package com.lifesimulator.lifesimulator.services;

import com.lifesimulator.lifesimulator.models.*;
import com.lifesimulator.lifesimulator.repositories.PersonRepository;
import com.lifesimulator.lifesimulator.repositories.RelationshipRepository;
import com.lifesimulator.lifesimulator.repositories.RelationshipTypeRepository;
import com.lifesimulator.lifesimulator.util.Gender;
import net.datafaker.Faker;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class FamilyService {
    private final PersonRepository personRepository;
    private final RelationshipRepository relationshipRepository;
    private final RelationshipTypeRepository relationshipTypeRepository;

    public FamilyService(final PersonRepository personRepository, final RelationshipRepository relationshipRepository, final RelationshipTypeRepository relationshipTypeRepository) {
        this.personRepository = personRepository;
        this.relationshipRepository = relationshipRepository;
        this.relationshipTypeRepository = relationshipTypeRepository;
    }

    private void ensureRelationshipTypesExist() {
        final var relations = new String[]{"Parent", "Spouse"};

        for (var relation : relations) {
            if (relationshipTypeRepository.findByName(relation) == null) {
                relationshipTypeRepository.save(new RelationshipType(relation));
            }
        }
    }

    // TODO: Move to HouseTypeService, UtilService or something like that
    private static HouseType getRandomHouseType() {
        return HouseType.values()[ThreadLocalRandom.current().nextInt(HouseType.values().length)];
    }

    public void generateFamily(Player player) {
        final var faker = new Faker();

        final int motherAge = ThreadLocalRandom.current().nextInt(18, 51);
        LocalDate motherBirth = subtractYearsRandomized(player.getBirth(), motherAge);

        final int fatherAge = ThreadLocalRandom.current().nextInt(18, 71);
        LocalDate fatherBirth = subtractYearsRandomized(player.getBirth(), fatherAge);

        final var houseType = getRandomHouseType();
        final var mother = new Person(faker.name().fullName(), motherBirth, player.getCountry(), Gender.FEMALE, houseType);
        final var father = new Person(faker.name().fullName(), fatherBirth, player.getCountry(), Gender.MALE, houseType);

        System.out.println("Mother: " + mother.getName() + " born in " + motherBirth);
        mother.generateInitialStats();
        System.out.println("Father: " + father.getName() + " born in " + fatherBirth);
        father.generateInitialStats();

        ensureRelationshipTypesExist();

        final var parentType = relationshipTypeRepository.findByName("Parent");
        final var spouseType = relationshipTypeRepository.findByName("Spouse");

        final var motherToChild = new Relationship(mother, player, parentType);
        final var fatherToChild = new Relationship(father, player, parentType);
        final var parentsRelationship = new Relationship(father, mother, spouseType);

        personRepository.save(mother);
        personRepository.save(father);
        relationshipRepository.save(motherToChild);
        relationshipRepository.save(fatherToChild);
        relationshipRepository.save(parentsRelationship);
    }

    private LocalDate subtractYearsRandomized(LocalDate date, int years) {
        final var newDate = date.minusYears(years);
        final int randomMonth = ThreadLocalRandom.current().nextInt(1, 13);
        final int randomDay = ThreadLocalRandom.current().nextInt(1, newDate.withMonth(randomMonth).lengthOfMonth() + 1);
        return LocalDate.of(newDate.getYear(), randomMonth, randomDay);
    }

}
