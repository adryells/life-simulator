package com.lifesimulator.lifesimulator.services;

import com.lifesimulator.lifesimulator.models.*;
import com.lifesimulator.lifesimulator.repositories.CompanyRepository;
import com.lifesimulator.lifesimulator.repositories.JobRepository;
import com.lifesimulator.lifesimulator.repositories.PersonJobRepository;
import com.lifesimulator.lifesimulator.repositories.PlayerRepository;
import com.lifesimulator.lifesimulator.util.ActionEvent;
import com.lifesimulator.lifesimulator.util.Country;
import com.lifesimulator.lifesimulator.util.Gender;
import com.lifesimulator.lifesimulator.util.RandomEvent;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

@Service
public class GameService {
    private final Scanner scanner;

    private final PlayerRepository playerRepository;

    private final JobRepository jobRepository;

    private final CompanyRepository companyRepository;

    private final PersonJobRepository personJobRepository;

    private final FamilyService familyService;

    private final EducationService educationService;

    private final ApplicationContext applicationContext;

    private Player player;

    private int actionsPerformedThisYear = 0; // TODO: Must be in database

    public GameService(final Scanner scanner, final PlayerRepository playerRepository, final JobRepository jobRepository, final CompanyRepository companyRepository, final PersonJobRepository personJobRepository, final FamilyService familyService, final EducationService educationService, final ApplicationContext applicationContext) {
        this.scanner = scanner;
        this.playerRepository = playerRepository;
        this.jobRepository = jobRepository;
        this.companyRepository = companyRepository;
        this.personJobRepository = personJobRepository;
        this.familyService = familyService;
        this.educationService = educationService;
        this.applicationContext = applicationContext;
    }

    // TODO: This function must be in a util package or something like that
    private int readIntInput(String prompt) {
        System.out.println(prompt);
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid choice. Please enter a number.");
            scanner.nextLine();
        }
        return scanner.nextInt();
    }

    // TODO: This function must be in a util package or something like that
    private <T> T promptForInput(Scanner scanner, Runnable prePrompt, String prompt, Function<String, T> converter, String errorMessage) {
        T result = null;
        while (result == null) {
            prePrompt.run();
            System.out.println(prompt);
            String input = scanner.nextLine().trim();
            result = converter.apply(input);
            if (result == null) {
                System.out.println(errorMessage);
            }
        }
        return result;
    }

    private LocalDate promptForBirthDate() {
        LocalDate birthDate = null;
        final var formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        while (birthDate == null) {
            System.out.println("Type your birth date in format: dd-MM-yyyy ");
            String birth = scanner.nextLine().trim();
            try {
                birthDate = LocalDate.parse(birth, formatter);
            } catch (Exception e) {
                System.out.println("Invalid date format, try again.");
            }
        }

        return birthDate;
    }

    private Gender promptForGender() {
        return promptForInput(
                scanner,
                () -> {
                },
                "Type your gender: [M] - Male | [F] - Female | [N] - Non-binary | [O] - Other",
                Gender::fromString,
                "Invalid gender, try again."
        );
    }

    private Country promptForCountry() {
        return promptForInput(scanner,
                () -> {
                    System.out.println("Type your country from the list below:");
                    Country.printAvailableCountries();
                },
                "Enter your country:",
                Country::fromString,
                "Invalid country, try again."
        );
    }

    // Maybe this function also must be in another package (like "event" package?)
    private void handleRandomEvents() {
        final var event = RandomEvent.values()[ThreadLocalRandom.current().nextInt(RandomEvent.values().length)];
        System.out.println("Event: " + event.getMessage());
        event.apply(player);
        playerRepository.save(player);
    }

    public void startGame() {
        System.out.println("Starting game...");
        System.out.println("Would you like to load a saved game? [Y/N]");
        String loadChoice = scanner.nextLine();

        // Must accept "yes" as valid answer
        if (loadChoice.equalsIgnoreCase("Y")) {
            loadPlayer();
        } else {
            createNewPlayer();
        }

        while (true) {
            if (player.isDead()) {
                System.out.println("O jogador " + player.getName() + " está morto.");
                return;
            }

            System.out.println("\n--- Year: " + player.getCurrentYear() + " ---");
            System.out.println("Your age: " + player.getAge());
            System.out.println("Choose an action:");
            System.out.println("[1] - Age Up a Year");
            System.out.println("[2] - Go to actions");
            System.out.println("[3] - Go to work");
            System.out.println("[4] - Exit");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> ageUp();
                case 2 -> goToActions();
                case 3 -> goToWork();
                case 4 -> {
                    System.out.println("Bye Bye, see you later!");
                    System.exit(SpringApplication.exit(applicationContext, () -> 1));
                }
                default -> System.out.println("Invalid option, try again.");
            }
        }
    }

    private void loadPlayer() {
        final var players = playerRepository.findAll();

        if (players.isEmpty()) {
            System.out.println("No saved players found.");
            return;
        }

        System.out.println("Choose a player to load:");
        for (int i = 0; i < players.size(); i++) {
            System.out.println("[" + (i + 1) + "] " + players.get(i).getName() + " (ID: " + players.get(i).getId() + ")");
        }

        int choice;
        while (true) {
            System.out.print("Enter a valid player number: ");
            if (!scanner.hasNextInt()) {
                System.out.println("Invalid choice. Please enter a number.");
                scanner.nextLine();
                continue;
            }

            choice = scanner.nextInt();
            if (choice < 1 || choice > players.size()) {
                System.out.println("Invalid choice. Please try again.");
                continue;
            }

            player = players.get(choice - 1);
            if (player.isDead()) {
                System.out.println("Invalid choice. Player is dead. Please try again.");
                continue;
            }

            System.out.println("Loaded player: " + player.getName());
            break;
        }

    }

    private void createNewPlayer() {
        System.out.println("Type your name: ");
        final var name = scanner.nextLine().trim();
        final var gender = promptForGender();
        final var country = promptForCountry();
        final var birthDate = promptForBirthDate();
        final int startYear = birthDate.getYear();

        player = new Player(name, birthDate, country, gender, startYear);
        player.generateInitialStats();

        playerRepository.save(player);
        familyService.generateFamily(player);
    }

    private void goToActions() {
        ActionEvent[] actions = ActionEvent.values();
        if (actionsPerformedThisYear >= 2) {
            System.out.println("Já atingiu as 2 ações anuais, volte no próximo ano!");
            return;
        }

        while (actionsPerformedThisYear < 2) {
            System.out.println("Choose an action: \n[0] EXIT ");
            for (int i = 0; i < actions.length; i++) {
                System.out.println("[" + (i + 1) + "] " + actions[i]);
            }

            int actionChoice = readIntInput("Your choice:");

            if (actionChoice == 0) {
                break;
            }
            if (actionChoice > actions.length) {
                System.out.println("Invalid choice. Please try again.");
                continue;
            }

            actionsPerformedThisYear++;
            ActionEvent action = actions[actionChoice - 1];
            action.apply(player);
            System.out.println("Event: " + action.getMessage());
        }
    }

    private void goToWork() {
        int age = player.getAge();
        int remainsYearsToCanWork = 14 - age;

        if (player.getAge() < 14) {
            System.out.println("You can't work yet. Try again in " + remainsYearsToCanWork + " years");
            return;
        }

        boolean inWorkSection = true;

        while (inWorkSection) {
            System.out.println("Select a option: ");
            System.out.println("[0] Exit");
            System.out.println("[1] Apply for a job");
            System.out.println("[2] Go to your job area");

            int choice = readIntInput("Your choice:");

            if (choice == 0) {
                inWorkSection = false;
            }
            if (choice == 1) {
                goToApplicationArea();
            }
            if (choice == 2){
                goToMyJobArea();
            }
        }
    }

    private void goToMyJobArea() {
        Optional<PersonJob> optionalJob = personJobRepository.findFirstByPlayerAndEndDateIsNullOrderByStartDateDesc(player);

        if (optionalJob.isEmpty()) {
            System.out.println("You are currently unemployed.");
            return;
        }

        PersonJob currentJob = optionalJob.get();

        while (true) {
            System.out.println("\nCurrent job: " + currentJob.getJob().getTitle());
            System.out.println("1. Ask for a raise");
            System.out.println("2. Adjust weekly work hours");
            System.out.println("3. Quit job");
            System.out.println("4. Work harder");
            System.out.println("0. Go back");

            int choice = readIntInput("Choose an option: ");

            switch (choice) {
                case 1:
                    if (currentJob.getPerformance() >= 70) {
                        double newWage = currentJob.getHourlyWage() * 1.10;
                        currentJob.setHourlyWage(newWage);
                        System.out.println("Raise granted! New hourly wage: R$" + newWage);
                    } else {
                        System.out.println("Your performance is too low to ask for a raise.");
                    }
                    break;

                case 2:
                    int newHours = readIntInput("Enter new weekly work hours (8-80): ");
                    if (newHours >= 8 && newHours <= 80) {
                        currentJob.setHoursPerWeek(newHours);
                        System.out.println("Work hours updated to " + newHours + " hours/week.");
                    } else {
                        System.out.println("Invalid number of hours.");
                    }
                    break;

                case 3:
                    currentJob.setEndDate(LocalDate.of(player.getCurrentYear(), 1, 1));
                    personJobRepository.save(currentJob);
                    System.out.println("You have quit your job: " + currentJob.getJob().getTitle());
                    return;

                case 4:
                    int boost = new Random().nextInt(6) + 5;
                    int newPerf = Math.min(100, currentJob.getPerformance() + boost);
                    currentJob.setPerformance(newPerf);
                    System.out.println("You worked harder! Performance increased to " + newPerf + "%.");
                    break;

                case 0:
                    return;

                default:
                    System.out.println("Invalid choice.");
            }

            personJobRepository.save(currentJob);
        }
    }


    private void goToApplicationArea() {
        generateJobsIfDoesntExists();

        List<Job> jobs = jobRepository.findAll();
        for (int i = 0; i < jobs.size(); i++) {
            Job job = jobs.get(i);
            System.out.println("ID: " + (i + 1) + " Job: " + job.getTitle() + " - Hourly wage: " + job.getHourlyWage() + " Minimum age:" + job.getMinAge());
            System.out.println("Company: " + job.getCompany().getName() + " - Previous years of xp: " + job.getRequiredPreviousYearsExperience() + " Previous job:" + job.getRequiredPreviousJob());
            System.out.println("Type: " + job.getType());
        }

        int choice = readIntInput("Choose a job to apply for:");

        if (choice > 0 && choice <= jobs.size()) {
            Job selectedJob = jobs.get(choice - 1);

            Optional<PersonJob> optionalLastJob = personJobRepository.findFirstByPlayerAndEndDateIsNullOrderByStartDateDesc(player);
            optionalLastJob.ifPresent(job -> {
                job.setEndDate(LocalDate.of(player.getCurrentYear(), 1, 1));
                personJobRepository.save(job);
            });

            PersonJob newJob = new PersonJob(
                    player,
                    selectedJob,
                    selectedJob.getHourlyWage(),
                    ThreadLocalRandom.current().nextInt(10, 30),
                    LocalDate.of(player.getCurrentYear(), 1, 1) // TODO: Must be player current year date
            );
            personJobRepository.save(newJob);
            playerRepository.save(player);
            System.out.println("You applied for the job: " + selectedJob.getTitle());
            System.out.println("Current performance: " + newJob.getPerformance() + "%");
        }
    }


    private void generateJobsIfDoesntExists() {
        if (companyRepository.findByName("Test Company") == null){
            Company a = new Company("Test Company");
            companyRepository.save(a);
            jobRepository.save(new Job("Job A", JobType.FREELANCER, 100.0, 14, null, 0, a));
        }
        if (companyRepository.findByName("Test Company 2") == null){
            Company b = new Company("Test Company 2");
            companyRepository.save(b);
            jobRepository.save(new Job("Job B", JobType.PART_TIME, 200.0, 14, null, 0, b));
        }
    }

    private void ageUp() {
        player.incrementYear();
        actionsPerformedThisYear = 0; // Must be in database
        educationService.checkEducationProgress(player);
        playerRepository.save(player);

        updateWorkStats();
        handleRandomEvents();
        updateStats();
    }

    private void updateWorkStats(){
        final var optionalPersonJob = personJobRepository.findFirstByPlayerAndEndDateIsNullOrderByStartDateDesc(player);

        if (optionalPersonJob.isPresent()) {
            final var personJob = optionalPersonJob.get();
            final var newIncome = personJob.getHourlyWage() * personJob.getHoursPerWeek();
            player.setBalance(player.getBalance() + newIncome);

            personJob.setYearsInJob(personJob.getYearsInJob() + 1);
            personJob.calculatePerformance();

            if (personJob.getPerformance() < 30) {
                personJob.setEndDate(LocalDate.of(player.getCurrentYear(), 1, 1));
                System.out.println("You're fired! Performance: " + personJob.getPerformance() + "%");
            }

            // TODO: Implement balance symbol based on contry
            System.out.println("You earned " + newIncome + " in your job.");
            System.out.println("Current balance: R$" + player.getBalance());
            System.out.println("Current performance: " + personJob.getPerformance() + "%");
        }
    }

    private void updateStats() {
        final int age = player.getAge();

        if (player.getHealth() <= 0) {
            player.setDead(true);
            return;
        }

        if (age <= 18) {
            player.setIq(Math.min(100, player.getIq() + ThreadLocalRandom.current().nextInt(1, 6)));
        }

        if (age < 30) {
            player.setBeauty(Math.min(100, player.getBeauty() + ThreadLocalRandom.current().nextInt(0, 3)));
            player.setStress(Math.min(100, player.getStress() + ThreadLocalRandom.current().nextInt(0, 5)));
        } else {
            player.setBeauty(Math.max(0, player.getBeauty() - ThreadLocalRandom.current().nextInt(0, 2)));
            player.setStress(Math.max(0, player.getStress() - ThreadLocalRandom.current().nextInt(0, 3)));
        }

        player.setHappyness(Math.max(0, Math.min(100, player.getHappyness() + ThreadLocalRandom.current().nextInt(-5, 6))));
        player.setHealth(Math.max(0, Math.min(100, player.getHealth() - ThreadLocalRandom.current().nextInt(0, 3))));

        player.printStats();
    }
}
