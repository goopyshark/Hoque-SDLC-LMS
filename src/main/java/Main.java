package main.java;

import java.util.Optional;
import java.util.Scanner;

/**
 * main.java.Main entry point for the main.java.LMS CLI application.
 *
 * REQUIRED BEHAVIOR:
 * - User can enter a file location (path), program reads file, creates main.java.Patron objects, adds to list.
 * - Menu options: add patron, remove patron, print all, exit (plus a file load option).
 * - Program loops until user selects exit.
 * - After add/remove, the updated patron list is printed immediately.
 */
public class Main {

    public static void main(String[] args) {

        // Create the main.java.LMS object that manages patrons
        LMS lms = new LMS();

        // Scanner for user input from the terminal (CLI)
        Scanner scanner = new Scanner(System.in);

        // Startup banner
        System.out.println("===============================================");
        System.out.println("   Library Management System (main.java.LMS) - CLI");
        System.out.println("===============================================");

        // On startup, prompt user to optionally load a file immediately.
        // This helps satisfy the requirement that the user can enter a file location and see patrons load.
        System.out.print("Enter path to patron file to load (or press Enter to skip): ");
        String startupPath = scanner.nextLine().trim();

        if (!startupPath.isEmpty()) {
            lms.loadPatronsFromFile(startupPath);
            lms.printAllPatrons();
        }

        // Menu loop continues until user chooses Exit
        boolean running = true;
        while (running) {
            printMenu();

            // Read user choice as a line, then parse safely
            System.out.print("Choose an option: ");
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1" -> { // Load patrons from file
                    System.out.print("Enter path to patron file: ");
                    String path = scanner.nextLine().trim();
                    lms.loadPatronsFromFile(path);
                    lms.printAllPatrons();
                }
                case "2" -> { // Add new patron manually
                    addPatronFlow(lms, scanner);

                    // Per requirements: after adding, print updated list
                    lms.printAllPatrons();
                }
                case "3" -> { // Remove a patron by ID
                    System.out.print("Enter 7-digit main.java.Patron ID to remove: ");
                    String id = scanner.nextLine().trim();

                    if (!id.matches("\\d{7}")) {
                        System.out.println("ERROR: ID must be exactly 7 digits.");
                        break;
                    }

                    boolean removed = lms.removePatronById(id);
                    if (removed) {
                        System.out.println("main.java.Patron removed successfully.");
                    } else {
                        System.out.println("No patron found with that ID.");
                    }

                    // Per requirements: after removing, print updated list
                    lms.printAllPatrons();
                }
                case "4" -> { // Print all patrons
                    lms.printAllPatrons();
                }
                case "5" -> { // Exit
                    System.out.println("Exiting main.java.LMS. Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid option. Please choose 1-5.");
            }
        }

        // Clean up resources
        scanner.close();
    }

    /**
     * Prints the on-screen menu. (Separated for readability and maintainability.)
     */
    private static void printMenu() {
        System.out.println();
        System.out.println("------------- MENU -------------");
        System.out.println("1) Load patrons from file");
        System.out.println("2) Add a new patron");
        System.out.println("3) Remove a patron by ID");
        System.out.println("4) Print all patrons");
        System.out.println("5) Exit");
        System.out.println("--------------------------------");
    }

    /**
     * Handles the user interaction needed to add a patron manually.
     * Validates inputs and prevents duplicate IDs.
     */
    private static void addPatronFlow(LMS lms, Scanner scanner) {

        // Collect and validate ID
        String id;
        while (true) {
            System.out.print("Enter 7-digit main.java.Patron ID: ");
            id = scanner.nextLine().trim();

            if (!id.matches("\\d{7}")) {
                System.out.println("ERROR: ID must be exactly 7 digits.");
                continue;
            }
            if (lms.containsId(id)) {
                System.out.println("ERROR: A patron with that ID already exists.");
                continue;
            }
            break;
        }

        // Collect and validate name
        String name;
        while (true) {
            System.out.print("Enter main.java.Patron Name: ");
            name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("ERROR: Name cannot be empty.");
                continue;
            }
            break;
        }

        // Collect and validate address
        String address;
        while (true) {
            System.out.print("Enter main.java.Patron Address: ");
            address = scanner.nextLine().trim();
            if (address.isEmpty()) {
                System.out.println("ERROR: Address cannot be empty.");
                continue;
            }
            break;
        }

        // Collect and validate fine amount
        String fineStr;
        while (true) {
            System.out.print("Enter Overdue Fine Amount (0 - 250): ");
            fineStr = scanner.nextLine().trim();

            // Use main.java.LMS validation method for consistency
            Optional<String> error = lms.validatePatronFields(id, name, address, fineStr);
            if (error.isPresent()) {
                System.out.println("ERROR: " + error.get());
                continue;
            }
            break;
        }

        double fine = Double.parseDouble(fineStr);

        // Create patron and add to system
        Patron patron = new Patron(id, name, address, fine);
        boolean added = lms.addPatron(patron);

        if (added) {
            System.out.println("main.java.Patron added successfully.");
        } else {
            // This should rarely happen because we check duplicates earlier
            System.out.println("ERROR: Could not add patron (duplicate ID).");
        }
    }
}