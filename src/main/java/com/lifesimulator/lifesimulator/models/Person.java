package com.lifesimulator.lifesimulator.models;

import com.lifesimulator.lifesimulator.util.Country;
import com.lifesimulator.lifesimulator.util.Gender;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Entity
@Table(name = "persons")
@Inheritance(strategy = InheritanceType.JOINED)
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private LocalDate birth;
    private Country country;
    private Gender gender;
    private Integer happyness;
    private Integer health;
    private Integer iq;
    private Integer beauty;
    private int stress;
    private boolean isDead;
    private Double balance; // TODO: Create bank account model? maybe.
    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Education> educationHistory = new ArrayList<>();

    public Person() {}

    public Person(String name, LocalDate birth, Country country, Gender gender) {
        this.name = name;
        this.birth = birth;
        this.country = country;
        this.gender = gender;
        this.iq = 50;
        this.beauty = 50;
        this.happyness = 50;
        this.health = 50;
        this.stress = 0;
        this.balance = 0.0;
        this.isDead = false;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getBirth() {
        return birth;
    }

    public Country getCountry() {
        return country;
    }

    public Gender getGender() {
        return gender;
    }

    public String getName() {
        return name;
    }

    public Integer getBeauty() {
        return beauty;
    }

    public Integer getHappyness() {
        return happyness;
    }

    public Integer getHealth() {
        return health;
    }

    public Integer getIq() {
        return iq;
    }

    public int getStress() {
        return stress;
    }

    public boolean isDead() {
        return isDead;
    }

    public void addEducation(Education education) {
        this.educationHistory.add(education);
    }

    public List<Education> getEducationHistory() {
        return educationHistory;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public void setDead(boolean isDead) {
        this.isDead = isDead;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setBirth(LocalDate birth) {
        this.birth = birth;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBeauty(Integer beauty) {
        this.beauty = beauty;
    }

    public void setHappyness(Integer happyness) {
        this.happyness = happyness;
    }

    public void setHealth(Integer health) {
        this.health = health;
    }

    public void setIq(Integer iq) {
        this.iq = iq;
    }

    public void setStress(int stress) {
        this.stress = stress;
    }

    public void generateInitialStats() {
        happyness = ThreadLocalRandom.current().nextInt(0, 101);
        health = ThreadLocalRandom.current().nextInt(0, 101);
        iq = ThreadLocalRandom.current().nextInt(0, 101);
        beauty = ThreadLocalRandom.current().nextInt(0, 101);

        printStats();
    }

    public void printStats() {
        System.out.printf(
                "%s | IQ=%d Beauty=%d Happyness=%d Health=%d Stress=%d.\n",
                this.getName(), this.getIq(), this.getBeauty(), this.getHappyness(), this.getHealth(), this.getStress()
        );
    }
}
