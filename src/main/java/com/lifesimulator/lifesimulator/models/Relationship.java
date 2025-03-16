package com.lifesimulator.lifesimulator.models;

import jakarta.persistence.*;

@Entity
@Table(name = "relationships")
public class Relationship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "person1_id", nullable = false)
    private Person person1;

    @ManyToOne
    @JoinColumn(name = "person2_id", nullable = false)
    private Person person2;

    @ManyToOne
    @JoinColumn(name = "relationship_type_id", nullable = false)
    private RelationshipType relationshipType;

    public Relationship(){}

    public Relationship(Person person1, Person person2, RelationshipType relationshipType) {
        this.person1 = person1;
        this.person2 = person2;
        this.relationshipType = relationshipType;
    }

    public int getId() {
        return id;
    }

    public Person getPerson1() {
        return person1;
    }

    public Person getPerson2() {
        return person2;
    }

    public RelationshipType getRelationshipType() {
        return relationshipType;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPerson1(Person person1) {
        this.person1 = person1;
    }

    public void setPerson2(Person person2) {
        this.person2 = person2;
    }

    public void setRelationshipType(RelationshipType relationshipType) {
        this.relationshipType = relationshipType;
    }
}
