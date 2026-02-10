package main.java;

import java.util.Objects;

/**
 * main.java.Patron represents a single library patron record inside the main.java.LMS.
 * Data is stored in memory (no database) as required by the project scope.
 */
public class Patron {

    // Unique 7-digit ID number (stored as String so leading zeros are preserved if needed)
    private final String id;

    // main.java.Patron name (free text, but validated to not be empty)
    private String name;

    // main.java.Patron address (free text, but validated to not be empty)
    private String address;

    // Overdue fine amount in dollars: must be between 0 and 250 inclusive
    private double fineAmount;

    /**
     * Constructor for a main.java.Patron object.
     *
     * @param id         unique 7-digit ID (String to preserve formatting)
     * @param name       patron name
     * @param address    patron address
     * @param fineAmount overdue fine amount (0 to 250)
     */
    public Patron(String id, String name, String address, double fineAmount) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.fineAmount = fineAmount;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public double getFineAmount() {
        return fineAmount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setFineAmount(double fineAmount) {
        this.fineAmount = fineAmount;
    }

    /**
     * Patrons are considered equal if their IDs match.
     * This prevents duplicate IDs when stored in collections.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Patron)) return false;
        Patron patron = (Patron) o;
        return Objects.equals(id, patron.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Nicely formatted string for printing the patron record in the main.java.LMS list.
     */
    @Override
    public String toString() {
        return String.format("ID: %s | Name: %s | Address: %s | Fine: $%.2f",
                id, name, address, fineAmount);
    }
}