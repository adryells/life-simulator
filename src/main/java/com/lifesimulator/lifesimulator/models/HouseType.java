package com.lifesimulator.lifesimulator.models;

public enum HouseType {
    PARENTS_HOUSE(0),
    LOW_COST_HOUSE(6000),
    MEDIUM_COST_HOUSE(18000),
    HIGH_COST_HOUSE(55000);

    private final int cost;

    HouseType(int cost) {
        this.cost = cost;
    }

    public int getCost() {
        return cost;
    }
}
