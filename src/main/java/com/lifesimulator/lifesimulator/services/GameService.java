package com.lifesimulator.lifesimulator.services;

import com.lifesimulator.lifesimulator.models.Player;
import com.lifesimulator.lifesimulator.repositories.PlayerRepository;
import com.lifesimulator.lifesimulator.util.ActionEvent;
import com.lifesimulator.lifesimulator.util.Country;
import com.lifesimulator.lifesimulator.util.Gender;
import com.lifesimulator.lifesimulator.util.RandomEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

@Service
public class GameService {
    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    FamilyService familyService;

    @Autowired
    private EducationService educationService;

    private Player player;

    private final Scanner scanner = new Scanner(System.in);

    int actionsPerformedThisYear = 0;

    public void startGame(){
        System.out.println("Starting game...");
        System.out.println("Would you like to load a saved game? [Y/N]");
        String loadChoice = scanner.nextLine();

        if (loadChoice.equalsIgnoreCase("Y")) {
            loadPlayer(scanner);
        } else {
            createNewPlayer(scanner);
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
            System.out.println("[2] - Actions");
            System.out.println("[3] - Exit");

            int choice = scanner.nextInt();

            if (choice == 1) {
                ageUp(player);
                updateStats(player);
            } else if (choice == 2) {
                actions();
            } else if (choice == 3) {
                break;
            } else {
                System.out.println("Invalid option, try again.");
            }
        }
    }

    private int readIntInput(String prompt) {
        System.out.println(prompt);
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid choice. Please enter a number.");
            scanner.nextLine();
        }
        return scanner.nextInt();
    }

    private void actions() {
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

    private void loadPlayer(Scanner scanner) {
        List<Player> players = playerRepository.findAll();
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

    private LocalDate promptForBirthDate(Scanner scanner) {
        LocalDate birthDate = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
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

    private Gender promptForGender(Scanner scanner){
        return promptForInput(
                scanner,
                () -> {},
                "Type your gender: [M] - Male | [F] - Female | [N] - Non-binary | [O] - Other",
                Gender::fromString,
                "Invalid gender, try again."
        );
    }

    private Country promptForCountry(Scanner scanner){
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

    private void createNewPlayer(Scanner scanner) {
        System.out.println("Type your name: ");
        String name = scanner.nextLine().trim();
        Gender gender = promptForGender(scanner);
        Country country = promptForCountry(scanner);
        LocalDate birthDate = promptForBirthDate(scanner);

        int startYear = birthDate.getYear();
        player = new Player(name, birthDate, country, gender, startYear);
        player.generateInitialStats();
        playerRepository.save(player);
        familyService.generateFamily(player);
    }


    private void ageUp(Player player) {
        player.incrementYear();
        actionsPerformedThisYear = 0;
        educationService.checkEducationProgress(player);
        playerRepository.save(player);
        handleRandomEvents(player);
    }


    private void updateStats(Player player) {
        int age = player.getCurrentYear() - player.getBirth().getYear();

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
        player.getStats();
    }

    private void handleRandomEvents(Player player) {
        RandomEvent event = RandomEvent.values()[ThreadLocalRandom.current().nextInt(RandomEvent.values().length)];
        System.out.println("Event: " + event.getMessage());
        event.apply(player);
        playerRepository.save(player);
    }
}
