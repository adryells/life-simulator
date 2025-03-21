package com.lifesimulator.lifesimulator.services;


import com.lifesimulator.lifesimulator.models.Education;
import com.lifesimulator.lifesimulator.models.Player;
import com.lifesimulator.lifesimulator.repositories.EducationRepository;
import com.lifesimulator.lifesimulator.util.ConsoleUniversityCourseSelector;
import com.lifesimulator.lifesimulator.util.EducationLevel;
import com.lifesimulator.lifesimulator.util.UniversityCourse;
import com.lifesimulator.lifesimulator.util.UniversityCourseSelector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Scanner;


@Service
public class EducationService {

    @Autowired
    private EducationRepository educationRepository;

    private final Scanner scanner = new Scanner(System.in);

    private UniversityCourseSelector courseSelector = new ConsoleUniversityCourseSelector();

    public void checkEducationProgress(Player player) {
        int age = player.getAge();
        List<Education> educationHistory = player.getEducationHistory();

        Education currentEducation = educationHistory.isEmpty() ? null : educationHistory.get(educationHistory.size() - 1);

        if (age == 6 && (currentEducation == null || currentEducation.getLevel() != EducationLevel.ELEMENTARY)) {
            startEducation(player, EducationLevel.ELEMENTARY, "Escola Primária");
        }

        if (currentEducation != null && currentEducation.getLevel() == EducationLevel.ELEMENTARY) {
            if (currentEducation.getYearsCompleted() >= 9 && currentEducation.isGraduated()) {
                startEducation(player, EducationLevel.HIGH_SCHOOL, "Escola Secundária");
            }
        }

        if (currentEducation != null && currentEducation.getLevel() == EducationLevel.HIGH_SCHOOL) {
            if (currentEducation.getYearsCompleted() >= 3 && currentEducation.isGraduated()) {
                chooseUniversity(player);
            }
        }
        if (age > 6){
            for (Education edu : educationHistory) {
                edu.progressYear();
                educationRepository.save(edu);
            }
        }
    }

    private void startEducation(Player player, EducationLevel level, String institution) {
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
