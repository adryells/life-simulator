package com.lifesimulator.lifesimulator.models;


import jakarta.persistence.*;

@Entity
public class Job {

    public Job(){}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Enumerated(EnumType.STRING)
    private JobType type;

    private Double hourlyWage;

    private Integer minAge;

    @ManyToOne
    private Job requiredPreviousJob;

    private Integer requiredPreviousYearsExperience;

    @ManyToOne
    private Company company;

    public Long getId() {
        return id;
    }
    public Company getCompany() {
        return company;
    }

    public Double getHourlyWage() {
        return hourlyWage;
    }

    public Integer getMinAge() {
        return minAge;
    }

    public Integer getRequiredPreviousYearsExperience() {
        return requiredPreviousYearsExperience;
    }

    public Job getRequiredPreviousJob() {
        return requiredPreviousJob;
    }

    public JobType getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public void setHourlyWage(Double hourlyWage) {
        this.hourlyWage = hourlyWage;
    }

    public void setRequiredPreviousJob(Job requiredPreviousJob) {
        this.requiredPreviousJob = requiredPreviousJob;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMinAge(Integer minAge) {
        this.minAge = minAge;
    }

    public void setRequiredPreviousYearsExperience(Integer requiredPreviousYearsExperience) {
        this.requiredPreviousYearsExperience = requiredPreviousYearsExperience;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public void setType(JobType type) {
        this.type = type;
    }
}
