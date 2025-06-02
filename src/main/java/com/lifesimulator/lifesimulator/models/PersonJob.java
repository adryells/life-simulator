package com.lifesimulator.lifesimulator.models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class PersonJob {

    public PersonJob(){}

    public PersonJob(Player player, Job job, Double hourlyWage, Integer hoursPerWeek, LocalDate startDate){
        this.player=player; // TODO: Must be person, i'll refact
        this.job=job;
        this.performance=0;
        this.hourlyWage=hourlyWage;
        this.hoursPerWeek=hoursPerWeek;
        this.startDate=startDate;
        this.yearsInJob=0.0;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Player player;

    @ManyToOne
    private Job job;

    private Integer performance;

    private Double hourlyWage;

    private Integer hoursPerWeek;

    private LocalDate startDate;

    private LocalDate endDate;

    private Double yearsInJob;

    public Double getHourlyWage() {
        return hourlyWage;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Job getJob() {
        return job;
    }

    public Double getYearsInJob() {
        return yearsInJob;
    }

    public Integer getHoursPerWeek() {
        return hoursPerWeek;
    }

    public Integer getPerformance() {
        return performance;
    }

    public Player getPlayer() {
        return player;
    }

    public void setHourlyWage(Double hourlyWage) {
        this.hourlyWage = hourlyWage;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setHoursPerWeek(Integer hoursPerWeek) {
        this.hoursPerWeek = hoursPerWeek;
    }

    public void setPerformance(Integer performance) {
        this.performance = performance;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setYearsInJob(Double yearsInJob) {
        this.yearsInJob = yearsInJob;
    }

    public void calculatePerformance() {
        double happiness = player.getHappyness() != null ? player.getHappyness() : 0;
        double health = player.getHealth() != null ? player.getHealth() : 0;
        double iq = player.getIq() != null ? player.getIq() : 0;
        double stress = player.getStress();

        double averageAttributes = (happiness + health + iq) / 3.0;
        double stressFactor = 1 - (stress / 100.0);
        double jobExperienceBonus = this.yearsInJob * 2.0;

        double rawPerformance = (averageAttributes * stressFactor) + jobExperienceBonus;

        this.performance = (int) Math.max(0, Math.min(100, rawPerformance));
    }

}
