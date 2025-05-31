package com.lifesimulator.lifesimulator.models;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Company {

    public Company(){}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Job> availableJobs;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Job> getAvailableJobs() {
        return availableJobs;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvailableJobs(List<Job> availableJobs) {
        this.availableJobs = availableJobs;
    }
}
