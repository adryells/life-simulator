package com.lifesimulator.lifesimulator.services;

import com.lifesimulator.lifesimulator.models.Player;
import com.lifesimulator.lifesimulator.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class GameService {
    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    FamilyService familyService;

    private Player player;

    public void startGame(){
        System.out.println("Starting game...");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Would you like to load a saved game? [Y/N]");
        String loadChoice = scanner.nextLine();

        if (loadChoice.equalsIgnoreCase("Y")) {
            loadPlayer(scanner);
        } else {
            createNewPlayer(scanner);
        }

        while (true) {
            if (player.isDead()){
                System.out.println("O jogador " + player.getName() + " está morto.");
                return;
            }
            System.out.println("\n--- Year: " + player.getCurrentYear() + " ---");
            System.out.println("Your age: " + (player.getCurrentYear() - player.getBirth().getYear()));
            System.out.println("Choose an action:");
            System.out.println("[1] - Age Up a Year");
            System.out.println("[2] - Exit");

            int choice = scanner.nextInt();
            if (choice == 1) {
                ageUp(player);
                updateStats(player);
            } else {
                break;
            }
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

        int choice = -1;
        while (choice < 1 || choice > players.size()) {
            System.out.print("Enter a valid player number: ");
            choice = scanner.nextInt();
            if (choice >= 1 && choice <= players.size()) {
                player = players.get(choice - 1);
                if (player.isDead()){
                    System.out.println("Invalid choice. Please try again.");
                } else {
                    System.out.println("Loaded player: " + player.getName());
                }
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }


    private void createNewPlayer(Scanner scanner) {
        System.out.println("Type your name: ");
        String name = scanner.nextLine();
        System.out.println("Type your gender: [M] - Male [F] - Female");
        String gender = scanner.nextLine();
        System.out.println("Type your country: [1] - Brazil [2] - Argentina [3] - Uruguay [4] - Chile");
        String country = scanner.nextLine();
        System.out.println("Type your birth date in format: dd-MM-yyyy ");
        String birth = scanner.nextLine();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate birthDate = LocalDate.parse(birth, formatter);
        int startYear = birthDate.getYear();

        player = new Player(name, birthDate, country, gender, startYear);
        player.generateInitialStats();
        playerRepository.save(player);
        familyService.generateFamily(player);
    }

    private void ageUp(Player player) {
        player.incrementYear();
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
        /*TODO:
        Deve haver bem mais eventos.
        Alguns eventos talvez só façam sentido em algumas faixas de idade.
        Eventos INTERATIVOS.
        Possivelmente Event se tornará uma tabela com EventType, EventStatus e tal.
        Eventos em português indicam que o jogo está em português, futuramente ampliar para inglês e espanhol
         */
        String[] events = {
                "Aconteceu um acidente, sua saúde diminuiu!", // Evento de perda de saúde
                "Você teve uma doença fatal, morreu!", // Evento de morte
                "Você fez novos amigos e ficou mais feliz!", // Evento de aumento de felicidade
                "Você começou a estudar mais e seu IQ aumentou!", // Evento de aumento de IQ
                "Você praticou exercícios e sua saúde aumentou!", // Evento de aumento de saúde
                "Você passou por um momento difícil e sua felicidade diminuiu!", // Evento de perda de felicidade,
                "Você teve um aumento de estresse devido ao trabalho!" // Evento para aumentar o estresse
        };

        String event = events[ThreadLocalRandom.current().nextInt(events.length)];

        System.out.println("Event: " + event);

        switch (event) {
            case "Aconteceu um acidente, sua saúde diminuiu!":
                player.setHealth(Math.max(0, player.getHealth() - ThreadLocalRandom.current().nextInt(5, 21)));
                break;
            case "Você teve uma doença fatal, morreu!":
                player.setDead(true);
                player.setHealth(0);
                break;
            case "Você fez novos amigos e ficou mais feliz!":
                player.setHappyness(Math.min(100, player.getHappyness() + ThreadLocalRandom.current().nextInt(5, 16)));
                break;
            case "Você começou a estudar mais e seu IQ aumentou!":
                player.setIq(Math.min(100, player.getIq() + ThreadLocalRandom.current().nextInt(3, 11)));
                break;
            case "Você praticou exercícios e sua saúde aumentou!":
                player.setHealth(Math.min(100, player.getHealth() + ThreadLocalRandom.current().nextInt(3, 11)));
                break;
            case "Você passou por um momento difícil e sua felicidade diminuiu!":
                player.setHappyness(Math.max(0, player.getHappyness() - ThreadLocalRandom.current().nextInt(5, 16)));
                break;
            case "Você teve um aumento de estresse devido ao trabalho!":
                player.setStress(Math.min(100, player.getStress() + ThreadLocalRandom.current().nextInt(5, 15)));
                break;
        }

        playerRepository.save(player);
    }

}
