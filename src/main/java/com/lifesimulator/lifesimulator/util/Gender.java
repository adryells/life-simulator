package com.lifesimulator.lifesimulator.util;

public enum Gender {
    MALE, FEMALE, NON_BINARY, OTHER;

    public static Gender fromString(String value) {
        return switch (value.toUpperCase()) {
            case "M" -> MALE;
            case "F" -> FEMALE;
            case "N" -> NON_BINARY;
            case "O" -> OTHER;
            default -> null;
        };
    }
}
