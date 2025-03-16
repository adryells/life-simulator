package com.lifesimulator.lifesimulator.models;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "players")
public class Player extends Person {
    private int currentYear;

    public Player() {}

    public Player(String name, LocalDate birth, String country, String gender, int startYear) {
        super(name, birth, country, gender);
        this.currentYear = startYear;
    }

    public int getCurrentYear() {
        return currentYear;
    }

    public void incrementYear() {
        this.currentYear++;
    }
}
