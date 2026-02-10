package main.java;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * main.java.LMS holds the in-memory patron list and provides all business operations:
 * - load patrons from file
 * - add patron manually
 * - remove patron
 * - print patron list
 *
 * IMPORTANT: No database is used; all data exists in memory only.
 */
public class LMS {

    // Keep patrons in a list for ordered printing
    private final List<Patron> patrons;

    // Keep patrons in a map for fast lookup by ID (prevents duplicates)
    private final Map<String, Patron> patronsById;

    public LMS() {
        this.patrons = new ArrayList<>();
        this.patronsById = new HashMap<>();
    }

    /**
     * Loads patrons from a file path.
     * Each line is expected to contain: id-name-address-fine
     *
     * Invalid lines are skipped (with a message), valid records create main.java.Patron objects.
     *
     * @param filePath path to the input text file
     */
    public void loadPatronsFromFile(String filePath) {
        int lineNumber = 0;
        int addedCount = 0;
        int skippedCount = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                lineNumber++;

                // Ignore blank lines to avoid unnecessary errors
                if (line.trim().isEmpty()) {
                    continue;
                }

                // Split the line using dash (-) as the delimiter
                // Expected format:
                // 1234567-John Smith-123 main.java.Main St Orlando FL-25
                String[] parts = line.split("-", -1);

                if (parts.length != 4) {
                    System.out.printf("Skipping line %d: expected 4 fields but got %d%n",
                            lineNumber, parts.length);
                    skippedCount++;
                    continue;
                }

                String id = parts[0].trim();
                String name = parts[1].trim();
                String address = parts[2].trim();
                String fineStr = parts[3].trim();

                // Validate each field; if any is invalid, skip the record
                Optional<String> error = validatePatronFields(id, name, address, fineStr);
                if (error.isPresent()) {
                    System.out.printf("Skipping line %d: %s%n", lineNumber, error.get());
                    skippedCount++;
                    continue;
                }

                double fine = Double.parseDouble(fineStr);

                // Prevent duplicate IDs
                if (patronsById.containsKey(id)) {
                    System.out.printf("Skipping line %d: duplicate ID '%s'%n", lineNumber, id);
                    skippedCount++;
                    continue;
                }

                // Create main.java.Patron object and add to internal collections
                Patron patron = new Patron(id, name, address, fine);
                addPatron(patron);
                addedCount++;
            }

            System.out.printf("%nFile load complete. Added: %d | Skipped: %d%n%n",
                    addedCount, skippedCount);

        } catch (IOException e) {
            System.out.println("ERROR: Unable to read file. Details: " + e.getMessage());
        }
    }

    /**
     * Adds a main.java.Patron to the main.java.LMS (both list and lookup map).
     * Returns true if added, false if ID already existed.
     */
    public boolean addPatron(Patron patron) {
        // If ID exists, do not add duplicates
        if (patronsById.containsKey(patron.getId())) {
            return false;
        }

        patrons.add(patron);
        patronsById.put(patron.getId(), patron);
        return true;
    }

    /**
     * Removes a patron by ID.
     * Returns true if removed, false if ID was not found.
     */
    public boolean removePatronById(String id) {
        Patron existing = patronsById.remove(id);
        if (existing == null) {
            return false; // nothing to remove
        }

        // Remove from list as well (list removal by object uses equals(), which checks ID)
        patrons.remove(existing);
        return true;
    }

    /**
     * Prints all patrons to the console in a clean readable format.
     * If there are no patrons, prints a friendly message.
     */
    public void printAllPatrons() {
        System.out.println("--------------------------------------------------");
        System.out.println("main.java.LMS main.java.Patron List");
        System.out.println("--------------------------------------------------");

        if (patrons.isEmpty()) {
            System.out.println("(No patrons currently in the system.)");
            System.out.println("--------------------------------------------------");
            return;
        }

        // Sort by ID for consistent output (optional but helpful for grading)
        List<Patron> sorted = new ArrayList<>(patrons);
        sorted.sort(Comparator.comparing(Patron::getId));

        for (Patron p : sorted) {
            System.out.println(p);
        }
        System.out.println("--------------------------------------------------");
    }

    /**
     * Validates fields coming from file/manual input.
     * Returns Optional.empty() if valid; otherwise returns Optional containing error message.
     */
    public Optional<String> validatePatronFields(String id, String name, String address, String fineStr) {

        // ID must be exactly 7 digits
        if (id == null || !id.matches("\\d{7}")) {
            return Optional.of("ID must be exactly 7 digits.");
        }

        // Name cannot be empty
        if (name == null || name.isBlank()) {
            return Optional.of("Name cannot be empty.");
        }

        // Address cannot be empty
        if (address == null || address.isBlank()) {
            return Optional.of("Address cannot be empty.");
        }

        // Fine must be parseable double and within allowed range
        double fine;
        try {
            fine = Double.parseDouble(fineStr);
        } catch (NumberFormatException e) {
            return Optional.of("Fine must be a valid number.");
        }

        if (fine < 0 || fine > 250) {
            return Optional.of("Fine must be between 0 and 250.");
        }

        return Optional.empty();
    }

    /**
     * Checks if a patron ID currently exists in the system.
     */
    public boolean containsId(String id) {
        return patronsById.containsKey(id);
    }
}