Library Management System (LMS)

Overview

The Library Management System (LMS) is a command-line Java application developed using the Software Development Life Cycle (SDLC) approach. The system allows a librarian to manage patron records without the use of a database or graphical interface. All data is stored in memory during program execution.
The application supports loading patrons from a text file, adding new patrons manually, removing patrons by ID, and printing all patrons. The system runs continuously until the user selects the exit option.

Requirements:

Java Runtime Environment (Java 17 or newer)

Command-line access (Terminal or Command Prompt)

How to Run:

Open a terminal in the folder containing the JAR file.

Run:

java -jar lms-1.0.jar

When prompted, enter the path to a patron file or press Enter to skip.

Patron File Format:

One patron per line, fields separated by dashes (-):

1234567-John Smith-123 Main St Orlando FL-25

Rules:

ID must be exactly 7 digits

Name and address cannot be empty

Fine must be between 0 and 250

Invalid records are skipped during file loading.

Menu Options

Load patrons from file

Add a new patron

Remove a patron by ID

Print all patrons

Exit

After adding or removing a patron, the updated list is automatically displayed.

Code Structure

Main.java – Program entry point and menu control

LMS.java – Core logic, validation, and in-memory storage

Patron.java – Patron data model

Deployment

The LMS is deployed as an executable JAR and is run using the java -jar command on any system with a compatible Java runtime.