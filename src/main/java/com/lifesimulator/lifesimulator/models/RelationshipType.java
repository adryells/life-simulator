package com.lifesimulator.lifesimulator.models;

import jakarta.persistence.*;

@Entity
@Table(name = "relationshipTypes")
public class RelationshipType {
    public RelationshipType(){};
    public RelationshipType(String name){
        this.name=name;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
