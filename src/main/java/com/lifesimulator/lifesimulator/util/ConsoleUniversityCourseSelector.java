package com.lifesimulator.lifesimulator.util;

import java.util.Scanner;

public class ConsoleUniversityCourseSelector implements UniversityCourseSelector {
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public UniversityCourse selectCourse(UniversityCourse[] courses) {
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