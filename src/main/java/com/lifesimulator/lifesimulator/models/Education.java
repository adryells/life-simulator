package com.lifesimulator.lifesimulator.models;

import com.lifesimulator.lifesimulator.util.EducationLevel;
import jakarta.persistence.*;

@Entity
public class Education {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Player player;

    @Enumerated(EnumType.STRING)
    private EducationLevel level;

    private String institution;
    private int yearsCompleted;
    private double performance;
    private boolean graduated;

    public Education() {
    }

    public Education(Player player, EducationLevel level, String institution) {
        this.player = player;
        this.level = level;
        this.institution = institution;
        this.yearsCompleted = 0;
        this.performance = 50.0;
        this.graduated = false;
    }

    public double calculatePerformance() {
        double finalPerformance = this.player.getIq() - this.player.getStress() * 0.5;

        if (player.getStress() > 80) {
            finalPerformance *= 0.8;
        }

        return Math.max(0, finalPerformance);
    }

    public void progressYear() {
        if (graduated) return;

        performance = calculatePerformance();
        var status = "Aprovado!";

        if (performance < 50) {
            status = "Reprovado! Precisa refazer o ano.";
        } else {
            yearsCompleted++;
        }

        if (yearsCompleted == requiredYears()) {
            graduated = true;
            System.out.println("Parabéns! Você se formou no nível " + level);
        }

        System.out.println(status);
        System.out.printf("Anos completos: %d ", yearsCompleted);
        System.out.printf("Sua performance: %f ", performance);
    }

    private int requiredYears() {
        return switch (level) {
            case ELEMENTARY -> 9;
            case HIGH_SCHOOL -> 3;
            case UNIVERSITY -> 4;
            default -> 0;
        };
    }

    public EducationLevel getLevel() {
        return level;
    }

    public int getYearsCompleted() {
        return yearsCompleted;
    }

    public double getPerformance() {
        return performance;
    }

    public Long getId() {
        return id;
    }

    public Player getPlayer() {
        return player;
    }

    public String getInstitution() {
        return institution;
    }

    public boolean isGraduated() {
        return graduated;
    }

    public void setYearsCompleted(int yearsCompleted) {
        this.yearsCompleted = yearsCompleted;
    }

    public void setLevel(EducationLevel level) {
        this.level = level;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setGraduated(boolean graduated) {
        this.graduated = graduated;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public void setPerformance(double performance) {
        this.performance = performance;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
