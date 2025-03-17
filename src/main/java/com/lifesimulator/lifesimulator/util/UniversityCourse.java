package com.lifesimulator.lifesimulator.util;

public enum UniversityCourse {
    COMPUTER_SCIENCE,
    MEDICINE,
    LAW,
    ENGINEERING,
    BUSINESS_ADMINISTRATION,
    PSYCHOLOGY,
    BIOLOGY,
    MATHEMATICS,
    PHYSICS,
    CHEMISTRY,
    ECONOMICS,
    ARCHITECTURE,
    PHILOSOPHY,
    HISTORY,
    LITERATURE;

    @Override
    public String toString() {
        return name().replace("_", " ");
    }
}
