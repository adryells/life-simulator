package com.lifesimulator.lifesimulator.util;

public enum Country {
    BRAZIL, ARGENTINA, URUGUAY, CHILE,
    USA, CANADA, MEXICO,
    UK, FRANCE, GERMANY, ITALY, SPAIN, PORTUGAL,
    SOUTH_AFRICA, EGYPT, NIGERIA,
    AUSTRALIA, NEW_ZEALAND,
    CHINA, JAPAN, INDIA, RUSSIA;

    public static Country fromString(String value) {
        try {
            return Country.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static void printAvailableCountries() {
        for (Country country : Country.values()) {
            System.out.println("- " + country.name());
        }
    }
}
