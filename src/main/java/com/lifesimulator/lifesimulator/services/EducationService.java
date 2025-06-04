package com.lifesimulator.lifesimulator.services;


import com.lifesimulator.lifesimulator.models.Education;
import com.lifesimulator.lifesimulator.models.Player;
import com.lifesimulator.lifesimulator.repositories.EducationRepository;
import com.lifesimulator.lifesimulator.util.EducationLevel;
import com.lifesimulator.lifesimulator.util.UniversityCourse;
import com.lifesimulator.lifesimulator.util.UniversityCourseSelector;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

record EducationAge(int yearsCompleted, EducationLevel educationLevel){}

@Service
public class EducationService {
    private final Scanner scanner;
    private final EducationRepository educationRepository;
    private final UniversityCourseSelector courseSelector;

    EducationService(final Scanner scanner, final EducationRepository educationRepository, final UniversityCourseSelector courseSelector) {
        this.scanner = scanner;
        this.courseSelector = courseSelector;
        this.educationRepository = educationRepository;
    }

    public void checkEducationProgress(final Player player) {
        final var age = player.getAge();
        final var educationHistory = player.getEducationHistory();
        assert educationHistory != null;

        final var currentEducation = Optional
                .of(educationHistory)
                .filter(l -> !l.isEmpty())
                .map(List::getLast);

        final var pair = currentEducation
                .map(e -> new EducationAge(e.getYearsCompleted(), e.getLevel()))
                .orElse(new EducationAge(0, EducationLevel.NONE));

        final boolean graduated = currentEducation.map(Education::isGraduated).orElse(false);

        switch (pair) {
            case EducationAge(_, var x) when age == 6 && x != EducationLevel.ELEMENTARY
                    -> startEducation(player, EducationLevel.ELEMENTARY, "Escola Primária");
            case EducationAge(var a, var x) when a > 9 && x == EducationLevel.ELEMENTARY && graduated
                    -> startEducation(player, EducationLevel.HIGH_SCHOOL, "Escola Secundária");
            case EducationAge(var a, var x) when a > 3 && x == EducationLevel.HIGH_SCHOOL && graduated
                    -> chooseUniversity(player);
            // NOTE: Java is dumb and requires a default
            default -> { }
        }

        if (age > 6){
            for (var edu : educationHistory) {
                edu.progressYear();
                educationRepository.save(edu);
            }
        }
    }

    private void startEducation(final Player player, final EducationLevel level, final String institution) {
        Education newEducation = new Education(player, level, institution);
        player.addEducation(newEducation);
        educationRepository.save(newEducation);
        System.out.println(player.getName() + " iniciou " + level + " na instituição " + institution);
    }

    private void chooseUniversity(Player player) {
        System.out.println("Apply to university? [Y/N]");
        String wantsToStudy = scanner.nextLine();

        if (wantsToStudy.equalsIgnoreCase("Y")) {
            UniversityCourse[] courses = UniversityCourse.values();
            UniversityCourse chosenCourse = courseSelector.selectCourse(courses);
            startEducation(player, EducationLevel.UNIVERSITY, "Federal University of " + player.getCountry());
            System.out.println("You enrolled in " + chosenCourse + ".");
        }
    }

}
