package com.lifesimulator.lifesimulator.util;

import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class ConsoleUniversityCourseSelector implements UniversityCourseSelector {
    private final Scanner scanner;

    ConsoleUniversityCourseSelector(final Scanner scanner){
        this.scanner = scanner;
    }

    @Override
    public UniversityCourse selectCourse(final UniversityCourse[] courses) {
        System.out.println("Choose your course:");
        for (int i = 0; i < courses.length; i++) {
            System.out.println("[" + (i + 1) + "] " + courses[i]);
        }

        int courseChoice;
        while (true) {
            System.out.print("Enter a valid course number: ");
            if (!scanner.hasNextInt()) {
                System.out.println("Invalid choice. Please enter a number.");
                scanner.nextLine();
                continue;
            }
            courseChoice = scanner.nextInt();
            if (courseChoice < 1 || courseChoice > courses.length) {
                System.out.println("Invalid choice. Please try again.");
                continue;
            }
            break;
        }
        return courses[courseChoice - 1];
    }
}