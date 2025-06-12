package com.lifesimulator.lifesimulator.models;

import com.lifesimulator.lifesimulator.util.Country;
import com.lifesimulator.lifesimulator.util.Gender;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "players")
public class Player extends Person {
    private int currentYear;

    public Player() {}

    public Player(String name, LocalDate birth, Country country, Gender gender, int startYear, HouseType houseType) {
        super(name, birth, country, gender, houseType);
        this.currentYear = startYear;
    }

    public int getCurrentYear() {
        return currentYear;
    }
    public int getAge() {
        return currentYear - this.getBirth().getYear();
    }

    public void incrementYear() {
        this.currentYear++;
    }
}
