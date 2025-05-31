package com.lifesimulator.lifesimulator.models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class PersonJob {

    public PersonJob(){}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Person person;

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

    public Person getPerson() {
        return person;
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

    public void setPerson(Person person) {
        this.person = person;
    }

    public void setYearsInJob(Double yearsInJob) {
        this.yearsInJob = yearsInJob;
    }
}
